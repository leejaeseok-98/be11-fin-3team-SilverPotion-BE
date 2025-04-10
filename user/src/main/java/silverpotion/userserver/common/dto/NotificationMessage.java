package silverpotion.userserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private String type; // ex: CHAT, GATHERING, SYSTEM
    private Long receiverId;
    private String title;
    private String content;
    private Long referenceId; // 예: 채팅방 id, 모임 id
    private LocalDateTime createdAt;
}