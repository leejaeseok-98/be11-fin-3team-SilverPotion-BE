package com.silverpotion.chatserver.chat.domain;

import com.example.chatserver.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatParticipant  extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @JoinColumn(name = "chat_room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    private Long userId;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String nickname;

    private boolean isConnected;

    private Long lastReadMessageId;

    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    public void connect() {
        this.isConnected = true;
    }

    public void disconnect() {
        this.isConnected = false;
    }

    public void updateLastReadMessage(Long messageId) {
        if (this.lastReadMessageId == null || this.lastReadMessageId < messageId) {
            this.lastReadMessageId = messageId;
        }
    }
}
