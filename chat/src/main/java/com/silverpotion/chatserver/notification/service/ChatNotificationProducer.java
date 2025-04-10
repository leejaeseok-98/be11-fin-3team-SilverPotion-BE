//package com.silverpotion.chatserver.notification.service;
//
//import com.silverpotion.chatserver.notification.dto.NotificationMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ChatNotificationProducer {
//    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;
//
//    public void send(NotificationMessage message) {
//        kafkaTemplate.send("notification-events", message);
//    }
//}
