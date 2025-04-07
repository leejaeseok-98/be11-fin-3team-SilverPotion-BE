package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GatheringInfoDto {
    private Long id;
    private String gatheringName;
    private String imageUrl;
    private String region;
    private Long maxPeople;
    private String category;
    private String greetingMessage;
    private Long peopleCount;
}
