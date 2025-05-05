package com.silverpotion.chatserver.notification.controller;

import com.silverpotion.chatserver.chat.domain.MessageType;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.notification.dto.NotificationRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final SseController sseController;

    public NotificationController(SseController sseController) {
        this.sseController = sseController;
    }

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
}
