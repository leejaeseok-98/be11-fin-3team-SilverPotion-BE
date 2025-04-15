package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GatheringCreateDto {

    private long gatheringId;

    private long categoryId;

    private String gatheringName;

    private String region;

    private String introduce;

    private Long maxPeople;

    private List<Long> gatheringCategoryDetailIds;

    public Gathering toEntity(GatheringCategory gatheringCategory, Long leaderId) {
        Gathering gathering = Gathering.builder()
                .gatheringCategory(gatheringCategory)
                .leaderId(leaderId)
                .gatheringName(this.gatheringName)
                .region(this.region)
                .introduce(this.introduce)
                .maxPeople(this.maxPeople)
                .build();
        gathering.addLeaderToGatheringPeople(leaderId);

        return gathering;
    }
}