package com.silverpotion.chatserver.notification.controller;

import com.silverpotion.chatserver.chat.domain.MessageType;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.notification.domain.Notification;
import com.silverpotion.chatserver.notification.dto.NotificationRequestDto;
import com.silverpotion.chatserver.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final SseController sseController;
    private final NotificationRepository notificationRepository;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody NotificationRequestDto dto) {
        ChatMessageDto message = ChatMessageDto.builder()
                .senderId(0L)
                .senderNickName("알림")
                .roomId(0L) // 아무거나
                .content(dto.getContent())
                .type(MessageType.SYSTEM)
                .build();

        sseController.sendToClientOrQueue(dto.getLoginId(), message);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/list")
    public List<Notification> getMyNotifications(@RequestHeader("X-User-LoginId") String loginId) {
        return notificationRepository.findByLoginIdOrderByCreatedAtDesc(loginId);
    }
}
