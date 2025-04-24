package com.silverpotion.chatserver.chat.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverpotion.chatserver.chat.domain.*;
import com.silverpotion.chatserver.chat.dto.*;
import com.silverpotion.chatserver.chat.repository.ChatMessageRepository;
import com.silverpotion.chatserver.chat.repository.ChatParticipantRepository;
import com.silverpotion.chatserver.chat.repository.ChatRoomRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserFeign userFeign;
    private final ChatMessageRepository chatMessageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;


    public ChatRoomDto createRoom(CreateChatRoomRequest request, HttpServletRequest httpRequest) {
        String loginId = httpRequest.getHeader("X-User-LoginId");
        if (loginId == null || loginId.isBlank()) {
            throw new RuntimeException("X-User-LoginId 헤더가 없습니다.");
        }

        Long myId = userFeign.getUserIdByLoginId(loginId);
        Long otherUserId = request.getUserIds().stream()
                .filter(id -> !id.equals(myId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("상대방 ID를 찾을 수 없습니다."));

        Long user1 = Math.min(myId, otherUserId);
        Long user2 = Math.max(myId, otherUserId);

        Optional<ChatRoom> existingRoomOpt = chatRoomRepository.findSingleChatRoomByUsers(user1, user2);
        if (existingRoomOpt.isPresent()) {
            ChatRoom room = existingRoomOpt.get();
            return new ChatRoomDto(room.getId(), room.getTitle(), room.getType(), room.getCreatedAt(), room.getLastMessageContent(), room.getLastMessageTime());
        }

        // ✅ 필요한 유저 정보는 미리 조회해둔다
        Map<Long, String> nicknameMap = new HashMap<>();
        Map<Long, String> loginIdMap = new HashMap<>();
        for (Long id : request.getUserIds()) {
            nicknameMap.put(id, userFeign.getNicknameByUserId(id));
            loginIdMap.put(id, userFeign.getLoginIdByUserId(id));
        }

        // ✅ 채팅방 생성
        String roomTitle = nicknameMap.get(otherUserId); // 상대방 닉네임을 타이틀로
        ChatRoom room = ChatRoom.builder()
                .type(request.getType())
                .title(roomTitle)
                .createdAt(LocalDateTime.now())
                .build();
        chatRoomRepository.save(room);

        // ✅ 참여자 생성
        List<ChatParticipant> participants = request.getUserIds().stream()
                .map(userId -> {
                    Long otherId = request.getUserIds().stream()
                            .filter(id -> !id.equals(userId))
                            .findFirst()
                            .orElseThrow();

                    return ChatParticipant.builder()
                            .userId(userId)
                            .loginId(loginIdMap.get(userId))
                            .nickname(nicknameMap.get(userId))
                            .otherNickname(nicknameMap.get(otherId))
                            .chatRoom(room)
                            .joinedAt(LocalDateTime.now())
                            .isConnected(false)
                            .build();
                })
                .collect(Collectors.toList());

        chatParticipantRepository.saveAll(participants);

        return new ChatRoomDto(room.getId(), room.getTitle(), room.getType(), room.getCreatedAt(), room.getLastMessageContent(), room.getLastMessageTime());
    }



    //나의 채팅방 조회
    public List<ChatRoomDto> getRoomsByUserId(Long userId) {
        List<ChatParticipant> list = chatParticipantRepository.findByUserId(userId);
        return list.stream()
                .map(cp -> {
                    ChatRoom r = cp.getChatRoom();
                    return new ChatRoomDto(r.getId(), r.getTitle(), r.getType(), r.getCreatedAt(),r.getLastMessageContent(), r.getLastMessageTime());
                })
                .collect(Collectors.toList());
    }


    // 메시지 조회용
    public Page<ChatMessageDto> getMessages(Long roomId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return chatMessageRepository.findByChatRoomId(roomId, pageable)
                .map(ChatMessageDto::fromEntity);
    }

    public void updateLastReadMessage(Long roomId, Long userId, Long messageId) {
        ChatParticipant participant = chatParticipantRepository
                .findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("참여자를 찾을 수 없습니다."));

        // 새로 읽은 메시지 ID가 기존보다 클 때만 업데이트
        if (participant.getLastReadMessageId() == null || participant.getLastReadMessageId() < messageId) {
            participant.updateLastReadMessage(messageId);
        }
    }
    public Long getUserIdByLoginId(String loginId) {
        return userFeign.getUserIdByLoginId(loginId);
    }
    public void setUserConnectionStatus(String loginId, boolean isConnected) {
        Long userId = getUserIdByLoginId(loginId);
        List<ChatParticipant> participants = chatParticipantRepository.findByUserId(userId);

        for (ChatParticipant participant : participants) {
            if (isConnected) {
                participant.connect();
            } else {
                participant.disconnect();
            }
        }
    }
    public long getUnreadMessageCount(Long roomId, Long userId) {
        ChatParticipant participant = chatParticipantRepository
                .findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new IllegalArgumentException("참여자를 찾을 수 없습니다."));

        Long lastReadId = participant.getLastReadMessageId();
        if (lastReadId == null) {
            return chatMessageRepository.countAllMessages(roomId);
        }

        return chatMessageRepository.countUnreadMessages(roomId, lastReadId);
    }
    //채팅방 리스트 조회
    public List<ChatRoomDto> getAllRooms(Long userId) {
        // 유저가 포함된 SINGLE, GROUP 타입 모두 조회
        List<ChatRoom> rooms = chatRoomRepository.findAllByUserId(userId);

        return rooms.stream()
                .map(room-> ChatRoomDto.fromEntity(room,userId))
                .collect(Collectors.toList());
    }
    // 모임생성시 그룹채팅 생성
    public ChatRoomDto createGroupRoom(CreateChatRoomRequest request) {
        // ✅ 사용자 정보 미리 로딩 (닉네임/로그인ID)
        Map<Long, String> nicknameMap = new HashMap<>();
        Map<Long, String> loginIdMap = new HashMap<>();
        for (Long userId : request.getUserIds()) {
            nicknameMap.put(userId, userFeign.getNicknameByUserId(userId));
            loginIdMap.put(userId, userFeign.getLoginIdByUserId(userId));
        }

        // ✅ 채팅방 생성
        ChatRoom room = ChatRoom.builder()
                .type(request.getType()) // ChatRoomType.GROUP
                .title(request.getTitle()) // 모임 이름
                .createdAt(LocalDateTime.now())
                .build();
        chatRoomRepository.save(room);

        // ✅ 참여자 생성
        List<ChatParticipant> participants = request.getUserIds().stream()
                .map(userId -> ChatParticipant.builder()
                        .userId(userId)
                        .loginId(loginIdMap.get(userId))
                        .nickname(nicknameMap.get(userId))
                        .chatRoom(room)
                        .joinedAt(LocalDateTime.now())
                        .isConnected(false)
                        .build())
                .collect(Collectors.toList());

        chatParticipantRepository.saveAll(participants);

        return new ChatRoomDto(
                room.getId(),
                room.getTitle(),
                room.getType(),
                room.getCreatedAt(),
                room.getLastMessageContent(),
                room.getLastMessageTime()
        );
    }

    // 그룹채팅 참여자 추가
    public void addParticipantToRoom(AddChatParticipantRequest request) {
        Long chatRoomId = request.getChatRoomId();
        Long userId = request.getUserId();

        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));

        if (chatParticipantRepository.existsByChatRoomIdAndUserId(chatRoomId, userId)) {
            return;
        }

        UserDto user = userFeign.getUserById(userId);

        ChatParticipant participant = ChatParticipant.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .nickname(user.getNickName())
                .chatRoom(room)
                .joinedAt(LocalDateTime.now())
                .isConnected(false)
                .build();

        chatParticipantRepository.save(participant);
    }


    // 그룹채팅 생생 및 조회시 중복 확인
    public ChatRoomDto findExistingGroupRoom(String title, Long userId) {
        ChatRoom room = chatRoomRepository.findGroupRoomByTitleAndUser(title, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "기존 그룹 채팅방이 존재하지 않습니다."));

        return new ChatRoomDto(
                room.getId(),
                room.getTitle(),
                room.getType(),
                room.getCreatedAt(),
                room.getLastMessageContent(),
                room.getLastMessageTime()
        );
    }
    //참여자 삭제
    public void removeParticipantFromRoom(Long chatRoomId, Long userId) {
        ChatParticipant participant = chatParticipantRepository
                .findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new RuntimeException("채팅방 참여자를 찾을 수 없습니다."));

        chatParticipantRepository.delete(participant);
    }
    // 정모 해체시 채팅방 삭제
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));

        room.setDelYn(DelYN.Y);
    }
}

