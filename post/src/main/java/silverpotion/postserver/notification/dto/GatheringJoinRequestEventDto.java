package silverpotion.postserver.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatheringJoinRequestEventDto {
    private Long gatheringId;
    private Long requesterUserId;
    private Long leaderId;
    private String gatheringName;
}
