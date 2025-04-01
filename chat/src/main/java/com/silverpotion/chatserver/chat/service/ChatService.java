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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        UserDto user = userFeign.getUserById(chatMessageDto.getUserId());
//        메시지저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .userId(user.getId())
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
                    .isRead(c.getUserId().equals(user))
                    .build();
            readStatusRepository.save(readStatus);
        }
    }
    //사용자 정보 꺼내기
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return Long.parseLong(authentication.getName()); // 또는 커스텀 UserDetails에서 ID 추출
        }
        throw new RuntimeException("인증된 사용자 정보 없음");
    }
    //그룹채팅 만들기
    public void createGroupRoom(String chatRoomName) {
        Long userId = getCurrentUserId(); // SecurityContext에서 ID 추출
        UserDto user = userFeign.getUserById(userId);

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .isGroupChat("Y")
                .build();
        chatRoomRepository.save(chatRoom);

        // 채팅 참여자로 개설자(userId) 추가
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .userId(userId) // 엔티티에서 userId 필드로 바뀌어야 함
                .build();
        chatParticipantRepository.save(chatParticipant);
    }


    public List<ChatRoomListResDto> getGroupchatRooms(){
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
        List<ChatRoomListResDto> dtos = new ArrayList<>();
        for(ChatRoom c : chatRooms){
            ChatRoomListResDto dto = ChatRoomListResDto
                    .builder()
                    .roomId(c.getId())
                    .roomName(c.getName())
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }

    public void addParticipantToGroupChat(Long roomId){
//        채팅방조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("room cannot be found"));
        // SecurityContext에서 userId 뽑기 (String -> Long 파싱)
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        // user-service에서 유저 정보 가져오기
        UserDto user = userFeign.getUserById(userId);
        if(chatRoom.getIsGroupChat().equals("N")){
            throw new IllegalArgumentException("그룹채팅이 아닙니다.");
        }
//        이미 참여자인지 검증
        Optional<ChatParticipant> participant = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId);
        if(!participant.isPresent()){
            addParticipantToRoom(chatRoom, userId);
        }
    }
//        ChatParticipant객체생성 후 저장
    public void addParticipantToRoom(ChatRoom chatRoom, Long userId){
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .userId(userId)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatMessageDto> getChatHistory(Long roomId){
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        // 현재 로그인한 userId 추출
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        boolean check = false;
        for(ChatParticipant c : chatParticipants){
            if(c.getUserId().equals(userId)){
                check = true;
            }
        }
        if(!check)throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");
//        특정 room에 대한 message조회
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for(ChatMessage c : chatMessages){
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(c.getMessage())
                    .userId(c.getUserId())
                    .build();
            chatMessageDtos.add(chatMessageDto);
        }
        return chatMessageDtos;
    }

    public boolean isRoomPaticipant(String email, Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("room cannot be found"));
        // SecurityContext에서 userId 뽑기 (String -> Long 파싱)
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for(ChatParticipant c : chatParticipants){
            if(c.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }

    public void messageRead(Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("room cannot be found"));
        // SecurityContext에서 userId 뽑기 (String -> Long 파싱)
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndUserId(chatRoom, userId);
        for(ReadStatus r : readStatuses){
            r.updateIsRead(true);
        }
    }

    public List<MyChatListResDto> getMyChatRooms(){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByUserId(userId);
        List<MyChatListResDto> chatListResDtos = new ArrayList<>();
        for(ChatParticipant c : chatParticipants){
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

    public void leaveGroupChatRoom(Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("room cannot be found"));
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        if(chatRoom.getIsGroupChat().equals("N")){
            throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
        }
        ChatParticipant c = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId).orElseThrow(()->new EntityNotFoundException("참여자를 찾을 수 없습니다."));
        chatParticipantRepository.delete(c);

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        if(chatParticipants.isEmpty()){
            chatRoomRepository.delete(chatRoom);
        }
    }

    public Long getOrCreatePrivateRoom(Long otherUserId) {
        Long currentUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        // 유저 정보 가져오기
        UserDto currentUser = userFeign.getUserById(currentUserId);
        UserDto otherUser = userFeign.getUserById(otherUserId);

        // 기존 채팅방이 존재하는지 확인
        Optional<ChatRoom> existingRoom = chatParticipantRepository.findExistingPrivateRoom(currentUserId, otherUserId);
        if (existingRoom.isPresent()) {
            return existingRoom.get().getId();
        }

        // 채팅방 생성
        ChatRoom newRoom = ChatRoom.builder()
                .isGroupChat("N")
                .name(currentUser.getNickName() + "-" + otherUser.getNickName()) // 사람이 읽기 좋게!
                .build();
        chatRoomRepository.save(newRoom);

        // 참여자 추가
        addParticipantToRoom(newRoom, currentUserId);
        addParticipantToRoom(newRoom, otherUserId);

        return newRoom.getId();
    }


}

