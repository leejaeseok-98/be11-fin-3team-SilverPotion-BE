package com.silverpotion.chatserver.chat.dto;

import com.silverpotion.chatserver.chat.domain.ChatParticipant;
import com.silverpotion.chatserver.chat.domain.ChatRoom;
import com.silverpotion.chatserver.chat.domain.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private String title;
    private ChatRoomType type;
    private LocalDateTime createdAt;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    
    public static ChatRoomDto fromEntity(ChatRoom room, Long myId) {
        String title;
        if(room.getType()== ChatRoomType.SINGLE){
            title = room.getChatParticipants().stream()
                    .filter(p -> !p.getUserId().equals(myId))
                    .map(ChatParticipant::getNickname)
                    .findFirst()
                    .orElse("알 수 없음");
        } else {
            title = room.getTitle();
        }


        return ChatRoomDto.builder()
                .id(room.getId())
                .title(title)
                .type(room.getType())
                .createdAt(room.getCreatedAt())
                .lastMessageContent(room.getLastMessageContent())
                .lastMessageTime(room.getLastMessageTime())
                .build();
    }

}