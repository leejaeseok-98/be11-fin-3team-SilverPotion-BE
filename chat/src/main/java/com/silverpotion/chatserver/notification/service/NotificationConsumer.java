package com.silverpotion.chatserver.notification.service;

import com.silverpotion.chatserver.notification.domain.Notification;
import com.silverpotion.chatserver.notification.dto.NotificationMessageDto;
import com.silverpotion.chatserver.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketSender notificationWebSocketSender;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consumeNotification(NotificationMessageDto dto) {
        log.info("🔔 Kafka 알림 수신: {}", dto);

        // 알림 저장
        Notification notification = Notification.builder()
                .loginId(dto.getLoginId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(dto.getType())
                .referenceId(dto.getReferenceId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        // WebSocket으로 실시간 알림 전송
        notificationWebSocketSender.sendNotificationToUser(dto.getLoginId(), dto);
    }
}