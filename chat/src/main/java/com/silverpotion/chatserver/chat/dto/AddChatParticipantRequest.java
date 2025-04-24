package com.silverpotion.chatserver.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddChatParticipantRequest {
    private Long chatRoomId;
    private Long userId;
}
