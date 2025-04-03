package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.gathering.domain.Status;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GatheringPeopleUpdateDto {
    private Status status;
}
