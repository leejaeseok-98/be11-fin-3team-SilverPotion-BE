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

    //벡터관련값 추가
    //정서지원적인지
    private int empathySupport;
    //사교적인지
    private int connectivitySupport;
    //활동적인지
    private int activitySupport;
    //성취적인지
    private int achievementSupport;
}
