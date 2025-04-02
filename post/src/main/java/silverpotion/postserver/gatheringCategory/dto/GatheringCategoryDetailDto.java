package silverpotion.postserver.gatheringCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategoryDetail;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GatheringCategoryDetailDto {
    private Long id;
    private Long gatheringCategoryId;
    private String name;

    public GatheringCategoryDetailDto(GatheringCategoryDetail gatheringCategoryDetail) {
        this.id = gatheringCategoryDetail.getId();
        this.gatheringCategoryId = gatheringCategoryDetail.getGatheringCategory().getId();
        this.name = gatheringCategoryDetail.getName();
    }
}
