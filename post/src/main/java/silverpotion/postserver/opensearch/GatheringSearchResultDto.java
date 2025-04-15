package silverpotion.postserver.opensearch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GatheringSearchResultDto {
    private Long id;
    private String gatheringName;
    private String region;
    private String imageUrl;
    private String introduce;
    private Long categoryId;
}
