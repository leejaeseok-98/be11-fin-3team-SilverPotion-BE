package com.silverpotion.chatserver.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto extends com.example.chatserver.common.domain.BaseTimeEntity {
    private Long roomId;
    private String message;
    private String senderLoginId;
}
