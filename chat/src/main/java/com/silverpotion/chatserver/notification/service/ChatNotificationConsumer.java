//package com.silverpotion.chatserver.notification.service;
//
//import com.silverpotion.chatserver.notification.dto.NotificationMessage;
//import com.silverpotion.chatserver.notification.repository.SseEmitterRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ChatNotificationConsumer {
//    private final SseEmitterRepository emitterRepository;
//
//    @KafkaListener(topics = "notification-events", groupId = "gathering-service")
//    public void consume(NotificationMessage msg) {
//        if (!emitterRepository.hasUser(msg.getReceiverId())) return;
//
//        emitterRepository.sendToUser(msg.getReceiverId(), msg);
//    }
//}
