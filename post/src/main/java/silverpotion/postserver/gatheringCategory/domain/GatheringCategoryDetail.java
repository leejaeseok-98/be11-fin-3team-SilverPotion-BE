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
public class GatheringCategoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_category_id")
    private GatheringCategory gatheringCategory;

    private String name;

    @OneToMany(mappedBy = "gatheringCategoryDetail" )
    @Builder.Default
    private List<GatheringDetail> gatheringDetails = new ArrayList<>();
}
