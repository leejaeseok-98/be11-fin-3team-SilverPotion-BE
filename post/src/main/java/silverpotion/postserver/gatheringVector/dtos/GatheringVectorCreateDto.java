package silverpotion.postserver.gatheringVector.dtos;

import jakarta.persistence.SecondaryTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GatheringVectorCreateDto {
    //정서적 지원 필요성
    private double empathySupport;
    //성취성
    private double achievementSupport;
    //사교성
    private double connectivitySupport;
    //활동성
    private double energySupport;

}
