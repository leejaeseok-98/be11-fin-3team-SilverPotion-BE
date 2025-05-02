package silverpotion.postserver.gatheringVector.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.postserver.common.domain.BaseTimeEntity;
import silverpotion.postserver.gathering.domain.Gathering;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class GatheringVector extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //정서적 지원 필요성(30점만점)
    private double empathySupport;
    //성취성(30점만점)
    private double achievementSupport;
    //사교성(20점 만점 이나 향후 모임,정모수에 따라 추가)
    private double connectivitySupport;
    //활동성(20점만점)
    private double energySupport;
    @OneToOne
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

}
