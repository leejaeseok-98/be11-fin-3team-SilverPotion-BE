package com.silverpotion.chatserver.chat.dto;

import com.silverpotion.chatserver.chat.domain.ChatMessage;
import com.silverpotion.chatserver.chat.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String senderNickName;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;

    public static ChatMessageDto fromEntity(ChatMessage entity, String senderNickName) {
        return ChatMessageDto.builder()
                .id(entity.getId())
                .roomId(entity.getChatRoom().getId())
                .senderId(entity.getSenderId())
                .senderNickName(senderNickName)
                .content(entity.getContent())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
