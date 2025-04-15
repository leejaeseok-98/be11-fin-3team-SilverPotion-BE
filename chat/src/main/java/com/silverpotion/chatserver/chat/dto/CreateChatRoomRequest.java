package com.silverpotion.chatserver.chat.dto;

import com.silverpotion.chatserver.chat.domain.ChatRoomType;
import lombok.Data;

import java.util.List;

@Data
public class CreateChatRoomRequest {
    private List<Long> userIds; // 참여할 유저 ID 목록
    private String title; // 그룹채팅 제목 (1:1일 경우 생략 가능)
    private ChatRoomType type; // SINGLE or GROUP
}
