package com.silverpotion.chatserver.chat.dto;

import com.silverpotion.chatserver.chat.domain.ChatMessage;
import com.silverpotion.chatserver.chat.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;

    public static ChatMessageDto fromEntity(ChatMessage entity) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(entity.getId());
        dto.setRoomId(entity.getChatRoom().getId());
        dto.setSenderId(entity.getSenderId());
        dto.setContent(entity.getContent());
        dto.setType(entity.getType());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
