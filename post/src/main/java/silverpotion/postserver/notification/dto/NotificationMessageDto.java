package silverpotion.postserver.notification.dto;

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
    private String loginId;     // 알림 받을 대상
    private String title;
    private String content;
    private String type;        // 예: JOIN_REQUEST, COMMENT 등
    private Long referenceId;   // 관련 ID (ex. 게시글 ID, 모임 ID 등)
}