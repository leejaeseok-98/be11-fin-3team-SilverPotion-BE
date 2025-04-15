package com.silverpotion.chatserver.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverpotion.chatserver.chat.domain.ChatMessage;
import com.silverpotion.chatserver.chat.domain.ChatRoom;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.dto.UserDto;
import com.silverpotion.chatserver.chat.repository.ChatMessageRepository;
import com.silverpotion.chatserver.chat.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final UserFeign userFeign;

    /**
     * 메시지를 DB에 저장하고 Kafka로 퍼블리시
     */
    @Transactional
    public ChatMessageDto saveAndPublish(Long roomId, ChatMessageDto dto) {
        // 1. 채팅방 존재 확인
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        // 2. DB 저장
        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .senderId(dto.getSenderId())
                .content(dto.getContent())
                .type(dto.getType())
                .createdAt(LocalDateTime.now())
                .isEdited(false)
                .isDeleted(false)
                .build();
        chatMessageRepository.save(message);
        System.out.println("💾 저장된 메시지: " + message);
        // 3. Kafka 발행
        try {
            dto.setRoomId(roomId); // Kafka에서 사용하기 위해 세팅
            dto.setId(message.getId());
            dto.setCreatedAt(message.getCreatedAt());

            String json = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send("chat-topic", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return dto;
    }

    /**
     * 메시지 페이징 조회
     */
    public Page<ChatMessageDto> getMessages(Long roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return chatMessageRepository.findByChatRoomId(roomId, pageable)
                .map(ChatMessageDto::fromEntity);
    }

    public Long getUserIdByLoginId(String loginId) {
        return userFeign.getUserIdByLoginId(loginId);
    }
}
