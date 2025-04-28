package com.silverpotion.chatserver.notification.service;

import com.silverpotion.chatserver.notification.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationWebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(String loginId, NotificationMessageDto dto) {
        messagingTemplate.convertAndSendToUser(
                loginId,
                "/topic/notifications",
                dto
        );
    }
}
