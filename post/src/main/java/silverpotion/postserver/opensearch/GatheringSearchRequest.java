package silverpotion.postserver.opensearch;

import lombok.Data;

@Data
public class GatheringSearchRequest {
    private String keyword;
    private String region;
    private Long categoryId;
}
