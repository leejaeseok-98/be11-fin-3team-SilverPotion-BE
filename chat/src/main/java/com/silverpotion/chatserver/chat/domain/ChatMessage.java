package com.silverpotion.chatserver.chat.domain;

import com.example.chatserver.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅방 ID (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    // 유저 서비스 분리된 구조: senderId만 저장
    private Long senderId;

    // 메시지 타입: TEXT, IMAGE
    @Enumerated(EnumType.STRING)
    private MessageType type;

    // 내용 (텍스트 or 이미지 URL 등)
    @Lob
    private String content;

    // 생성 시간
    private LocalDateTime createdAt;

    // 메시지 수정/삭제 여부 (추후 확장용)
    private boolean isEdited = false;
    private boolean isDeleted = false;
}
