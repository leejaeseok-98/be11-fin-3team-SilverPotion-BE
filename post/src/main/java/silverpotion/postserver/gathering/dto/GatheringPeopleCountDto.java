package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GatheringPeopleCountDto {
    private Long gatheringId;
    private Long activePeopleCount;
}
