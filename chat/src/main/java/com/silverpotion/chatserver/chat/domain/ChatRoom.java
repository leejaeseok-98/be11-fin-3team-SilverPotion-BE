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
public class ChatRoom  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type; // SINGLE, GROUP

    private String title;

    private LocalDateTime createdAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @Column(name = "last_message_content")
    private String lastMessageContent;

    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYn=DelYN.N;
}
