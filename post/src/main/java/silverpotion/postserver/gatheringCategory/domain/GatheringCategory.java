package silverpotion.postserver.gatheringCategory.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GatheringCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;

    @OneToMany(mappedBy = "gatheringCategory" )
    @Builder.Default
    private List<GatheringCategoryDetail> gatheringCategoryDetails = new ArrayList<>();
}
