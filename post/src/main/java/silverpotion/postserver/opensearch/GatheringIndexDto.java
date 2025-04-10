package silverpotion.postserver.opensearch;

import lombok.Builder;
import lombok.Data;
import silverpotion.postserver.gathering.domain.Gathering;

@Data
@Builder
public class GatheringIndexDto {
    private Long id;
    private String gatheringName;
    private String introduce;
    private Long categoryId;
    private String region;
    private String imageUrl;
    private String delYN;

    public static GatheringIndexDto fromEntity(Gathering gathering) {
        return GatheringIndexDto.builder()
                .id(gathering.getId())
                .gatheringName(gathering.getGatheringName())
                .introduce(gathering.getIntroduce())
                .region(gathering.getRegion())
                .imageUrl(gathering.getImageUrl())
                .categoryId(gathering.getGatheringCategory() != null ? gathering.getGatheringCategory().getId() : null)
                .delYN(gathering.getDelYN().name())
                .build();
    }
}
