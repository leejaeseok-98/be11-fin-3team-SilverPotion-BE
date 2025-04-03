package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GatheringPeopleCreateDto {

    private Long gatheringId;
    private String greetingMessage;

}
