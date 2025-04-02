package silverpotion.postserver.gatheringCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;

import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GatheringCategoryDto {
    private Long id;
    private String name;

    public GatheringCategoryDto(GatheringCategory gatheringCategory) {
        this.id = gatheringCategory.getId();
        this.name = gatheringCategory.getName();
    }
}
