package silverpotion.postserver.gatheringVector.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// 유저서비스에 게터링벡터 넘길때 dto
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GatheringVectorForUserServiceDto {
    //GatheringVector아이디가 아닌 Gathering아이디임!
    private Long id;
    //정서적 지원 필요성(30점만점)
    private double empathySupport;
    //성취성(30점만점)
    private double achievementSupport;
    //사교성(20점 만점 이나 향후 모임,정모수에 따라 추가)
    private double connectivitySupport;
    //활동성(20점만점)
    private double energySupport;
}
