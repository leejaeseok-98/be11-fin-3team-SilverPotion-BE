package com.silverpotion.chatserver.notification.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.notification.controller.SseController;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSseService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final SseController sseController;

    public void publishToSseTopic(ChatMessageDto dto) {
        try {
            String message = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send("chat-topic", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @KafkaListener(topics = "chat-topic", groupId = "chat-consumer-group")
    public void handleMessage(String messageJson) throws JsonProcessingException {
        ChatMessageDto dto = objectMapper.readValue(messageJson, ChatMessageDto.class);
        // 오프라인 유저에게 SSE 푸시 전송 등 수행
    }
}