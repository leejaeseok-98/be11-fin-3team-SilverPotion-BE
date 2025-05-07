package silverpotion.userserver.careRelation.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationMessageDto {
    private String loginId;
    private String title;
    private String content;
    private String type;
    private Long referenceId;
}