package com.silverpotion.chatserver.chat.dto;

import com.silverpotion.chatserver.chat.domain.ChatRoom;
import com.silverpotion.chatserver.chat.domain.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private Long id;
    private String title;
    private ChatRoomType type;
    private LocalDateTime createdAt;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    public static ChatRoomDto fromEntity(ChatRoom chatRoom) {
        return new ChatRoomDto(
                chatRoom.getId(),
                chatRoom.getTitle(),
                chatRoom.getType(),
                chatRoom.getCreatedAt(),
                chatRoom.getLastMessageContent(),
                chatRoom.getLastMessageTime()
        );
    }

}