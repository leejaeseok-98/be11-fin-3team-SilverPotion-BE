package com.silverpotion.chatserver.chat.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverpotion.chatserver.chat.domain.ChatMessage;
import com.silverpotion.chatserver.chat.domain.ChatParticipant;
import com.silverpotion.chatserver.chat.domain.ChatRoom;
import com.silverpotion.chatserver.chat.domain.ChatRoomType;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.dto.ChatRoomDto;
import com.silverpotion.chatserver.chat.dto.CreateChatRoomRequest;
import com.silverpotion.chatserver.chat.dto.UserDto;
import com.silverpotion.chatserver.chat.repository.ChatMessageRepository;
import com.silverpotion.chatserver.chat.repository.ChatParticipantRepository;
import com.silverpotion.chatserver.chat.repository.ChatRoomRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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


    public ChatRoomDto createRoom(CreateChatRoomRequest request,HttpServletRequest httpRequest) {
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
            return new ChatRoomDto(room.getId(), room.getTitle(), room.getType(), room.getCreatedAt(),room.getLastMessageContent(), room.getLastMessageTime());
        } else {
            // 닉네임 조회
            String otherNickName = userFeign.getNicknameByUserId(otherUserId);
            request.setTitle(otherNickName);

            // 채팅방 생성
            ChatRoom room = new ChatRoom();
            room.setType(request.getType());
            room.setTitle(request.getTitle());
            room.setCreatedAt(LocalDateTime.now());
            chatRoomRepository.save(room);

            // 참여자 생성
            List<ChatParticipant> participants = new ArrayList<>();
            for (Long userId : request.getUserIds()) {
                ChatParticipant participant = new ChatParticipant();
                participant.setUserId(userId);
                participant.setLoginId(userFeign.getLoginIdByUserId(userId));
                participant.setNickname(userFeign.getNicknameByUserId(userId));
                participant.setChatRoom(room);
                participant.setJoinedAt(LocalDateTime.now());
                participant.setConnected(false);
                participants.add(participant);
            }

            chatParticipantRepository.saveAll(participants);

            return new ChatRoomDto(room.getId(), room.getTitle(), room.getType(), room.getCreatedAt(),room.getLastMessageContent(), room.getLastMessageTime());
        }
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

//    //메시지 발생시 DB저장 및 kafka로 메시지 발행(chat-topic)
//    public ChatMessageDto saveAndPublish(Long roomId, ChatMessageDto dto) {
//        // 1. 채팅방 존재 확인
//        ChatRoom room = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
//
//        // 2. DB에 저장
//        ChatMessage message = new ChatMessage();
//        message.setChatRoom(room);
//        message.setSenderId(dto.getSenderId());
//        message.setType(dto.getType());
//        message.setContent(dto.getContent());
//        message.setCreatedAt(LocalDateTime.now());
//        chatMessageRepository.save(message);
//        // 저장된 메시지 반환
//        dto.setId(message.getId());
//        dto.setCreatedAt(message.getCreatedAt());
//        dto.setSenderId(message.getSenderId());
//        dto.setRoomId(message.getChatRoom().getId());
//        System.out.println("🔥 Kafka 직전 DTO = " + dto);
//        // Kafka에 발행
//        try {
//            String json = objectMapper.writeValueAsString(dto);
//            System.out.println("📤 Kafka 발행 JSON = " + json);
//            kafkaTemplate.send("chat-topic", json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // 실제 운영에선 로깅 또는 알림
//        }
//        // 마지막 메시지 갱신
//        room.setLastMessageContent(dto.getContent());
//        room.setLastMessageTime(message.getCreatedAt());
//        chatRoomRepository.save(room);
//
//        return dto;
//    }

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
                .map(ChatRoomDto::fromEntity)
                .collect(Collectors.toList());
    }
}

