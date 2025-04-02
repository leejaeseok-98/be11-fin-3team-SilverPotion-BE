package silverpotion.userserver.healthData.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HealthDataListDto {
    //  하루 평균 심박
    private int heartbeat;
    //  하루 걸음수
    private int step;
    //  하루 소모 칼로리
    private int calory;
    //  하루 활동칼로리
    private int activeCalory;
    //  오늘 걸은 거리
    private int distance;



}
