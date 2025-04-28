package com.silverpotion.chatserver.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto {
    private String loginId; // 알림 받을 유저의 로그인 ID
    private String title;
    private String content;
    private String type; // 예: JOIN_REQUEST, COMMENT, LIKE, etc.
    private Long referenceId; // 알림 대상 ID (모임 ID, 게시글 ID 등)
}