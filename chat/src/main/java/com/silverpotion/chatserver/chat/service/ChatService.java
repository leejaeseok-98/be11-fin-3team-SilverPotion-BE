package com.silverpotion.chatserver.chat.service;


import com.silverpotion.chatserver.chat.domain.ChatMessage;
import com.silverpotion.chatserver.chat.domain.ChatParticipant;
import com.silverpotion.chatserver.chat.domain.ChatRoom;
import com.silverpotion.chatserver.chat.domain.ReadStatus;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.dto.ChatRoomListResDto;
import com.silverpotion.chatserver.chat.dto.MyChatListResDto;
import com.silverpotion.chatserver.chat.dto.UserDto;
import com.silverpotion.chatserver.chat.repository.ChatMessageRepository;
import com.silverpotion.chatserver.chat.repository.ChatParticipantRepository;
import com.silverpotion.chatserver.chat.repository.ChatRoomRepository;
import com.silverpotion.chatserver.chat.repository.ReadStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatService {
    private final UserFeign userFeign;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    public ChatService(UserFeign userFeign, ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository, ChatMessageRepository chatMessageRepository, ReadStatusRepository readStatusRepository) {
        this.userFeign = userFeign;
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.readStatusRepository = readStatusRepository;
    }

    public void saveMessage(Long roomId, ChatMessageDto chatMessageDto){
//        채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("room cannot be found"));

        Long userId = userFeign.getUserIdByLoginId(chatMessageDto.getSenderLoginId());
//        메시지저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .userId(userId)
                .message(chatMessageDto.getMessage())
                .build();
        chatMessageRepository.save(chatMessage);
//        사용자별로 읽음여부 저장
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for(ChatParticipant c : chatParticipants){
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .userId(c.getUserId())
                    .chatMessage(chatMessage)
                    .isRead(c.getUserId().equals(userId))
                    .build();
            readStatusRepository.save(readStatus);
        }
    }
    //사용자 정보 꺼내기
    // 기존 SecurityContextHolder 기반 유저 추출 메서드 제거
// public Long getCurrentUserId() { ... } 제거

    // 그룹 채팅방 생성
    public void createGroupRoom(String chatRoomName, Long userId) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .isGroupChat("Y")
                .build();
        chatRoomRepository.save(chatRoom);

        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .userId(userId)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    // 그룹 채팅방 목록 조회는 인증 필요 없음
    public List<ChatRoomListResDto> getGroupchatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
        List<ChatRoomListResDto> dtos = new ArrayList<>();
        for (ChatRoom c : chatRooms) {
            ChatRoomListResDto dto = ChatRoomListResDto
                    .builder()
                    .roomId(c.getId())
                    .roomName(c.getName())
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }

    // 그룹 채팅방 참여
    public void addParticipantToGroupChat(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        if ("N".equals(chatRoom.getIsGroupChat())) {
            throw new IllegalArgumentException("그룹채팅이 아닙니다.");
        }

        Optional<ChatParticipant> participant = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId);
        if (participant.isEmpty()) {
            addParticipantToRoom(chatRoom, userId);
        }
    }

    // 채팅 참여자 저장 (변경 없음)
    public void addParticipantToRoom(ChatRoom chatRoom, Long userId) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .userId(userId)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    // 채팅 히스토리 조회
    public List<ChatMessageDto> getChatHistory(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        boolean check = false;
        for (ChatParticipant c : chatParticipants) {
            if (c.getUserId().equals(userId)) {
                check = true;
                break;
            }
        }
        if (!check) throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for (ChatMessage c : chatMessages) {
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(c.getMessage())
                    .build();
            chatMessageDtos.add(chatMessageDto);
        }
        return chatMessageDtos;
    }


    public boolean isRoomPaticipant(Long userId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for (ChatParticipant c : chatParticipants) {
            if (c.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public void messageRead(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndUserId(chatRoom, userId);
        for (ReadStatus r : readStatuses) {
            r.updateIsRead(true);
        }
    }


    public List<MyChatListResDto> getMyChatRooms(Long userId) {
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByUserId(userId);
        List<MyChatListResDto> chatListResDtos = new ArrayList<>();

        for (ChatParticipant c : chatParticipants) {
            Long count = readStatusRepository.countByChatRoomAndUserIdAndIsReadFalse(c.getChatRoom(), userId);
            MyChatListResDto dto = MyChatListResDto.builder()
                    .roomId(c.getChatRoom().getId())
                    .roomName(c.getChatRoom().getName())
                    .isGroupChat(c.getChatRoom().getIsGroupChat())
                    .unReadCount(count)
                    .build();
            chatListResDtos.add(dto);
        }

        return chatListResDtos;
    }


    public void leaveGroupChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        if ("N".equals(chatRoom.getIsGroupChat())) {
            throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
        }

        ChatParticipant participant = chatParticipantRepository
                .findByChatRoomAndUserId(chatRoom, userId)
                .orElseThrow(() -> new EntityNotFoundException("참여자를 찾을 수 없습니다."));

        chatParticipantRepository.delete(participant);

        List<ChatParticipant> remainingParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        if (remainingParticipants.isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }
    }
    //개인 채팅방 개설 or 조회
    public Long getOrCreatePrivateRoom(Long currentUserId, Long otherUserId) {
        // 상대방 정보 조회
        System.out.println("currentUserId : " + currentUserId);
        System.out.println("otherUserId : " + otherUserId);
        Optional<ChatRoom> existingRoom = chatParticipantRepository.findExistingPrivateRoom(currentUserId, otherUserId);

        if (existingRoom.isPresent()) {
            return existingRoom.get().getId();
        }
        String otherUserNickname = userFeign.getUserIdByNickname(otherUserId);
        ChatRoom newRoom = ChatRoom.builder()
                .isGroupChat("N")
                .name(otherUserNickname)
                .build();
        chatRoomRepository.save(newRoom);
        System.out.println("service / newRoomId : "+newRoom.getId());
        addParticipantToRoom(newRoom, currentUserId);
        addParticipantToRoom(newRoom, otherUserId);

        return newRoom.getId();
    }
    public Long getUserIdByLoginId(String loginId){
        return userFeign.getUserIdByLoginId(loginId);
    }

}

