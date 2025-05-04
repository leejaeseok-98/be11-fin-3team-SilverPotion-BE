package com.silverpotion.chatserver.notification.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.repository.ChatParticipantRepository;
import com.silverpotion.chatserver.notification.controller.SseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSseService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ChatParticipantRepository chatParticipantRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public void publishToSseTopic(ChatMessageDto dto) {
        log.info("🔥 발행 전 DTO: {}", dto);
        try {
            String message = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send("chat-topic", message);
            log.info("📡 메시지 Kafka 발행됨: {}", message); // 발행된 메시지 확인
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
//    @KafkaListener(topics = "chat-topic", groupId = "chat-consumer-group")
//    public void handleMessage(String messageJson) throws JsonProcessingException {
//        ChatMessageDto dto = objectMapper.readValue(messageJson, ChatMessageDto.class);
//        // 오프라인 유저에게 SSE 푸시 전송 등 수행
//    }

    @KafkaListener(
            topics = "chat-topic",
            groupId = "chat-websocket-group",
            concurrency = "1" // ✅ 명시적으로 한 쓰레드만 사용하게 설정
    )
    public void consumeChatMessage(String messageJson) {
        log.warn("🔥 WebSocket Kafka Consumer 실행됨 @{}", System.identityHashCode(this));
        try {
            // 메시지가 Kafka에서 수신되는지 확인하는 로그 추가
            log.info("📡 수신된 메시지: {}", messageJson);

            ChatMessageDto message = objectMapper.readValue(messageJson, ChatMessageDto.class);

            List<String> loginIds = chatParticipantRepository.findLoginIdsByRoomId(message.getRoomId());
            log.info("🧩 연결된 유저 목록: {}", simpUserRegistry.getUsers().stream().map(SimpUser::getName).toList());
            log.info("📡 전송할 메시지 내용: {}", message);

            // 개인 WebSocket 세션으로 쏘는 방식으로 수정
            for (String loginId : loginIds) {
                log.info("🧩 대상 loginId = {}", loginId);
                boolean hasUser = simpUserRegistry.getUser(loginId) != null;
                log.info("🧩 SimpUserRegistry에 해당 유저 존재? = {}", hasUser);

                if (hasUser) {
                    messagingTemplate.convertAndSendToUser(loginId, "/chat", message);
                    log.info("📡 WebSocket 전송 → /user/{}/chat", loginId);
                }
            }
        } catch (Exception e) {
            log.error("❌ WebSocket Kafka Consumer 오류", e);
        }
    }


}