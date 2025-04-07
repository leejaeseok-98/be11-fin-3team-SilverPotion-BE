package silverpotion.userserver.healthData.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.healthData.dtos.HealthAvgDataDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AverageData {
    //평균 걸음수
    private int avgStep;
    //평균 심박
    private int avgHeartBeat;
    //평균 운동거리
    private int avgDistancd;
    //평균 소모칼로리
    private int avgCalory;
    //평균 활동칼로리
    private int avgActiveCalory;
    //  오늘 총 수면시간
    private int avgTotalSleepMinutes;
    //  오늘 깊은 수면시간
    private int avgDeepSleepMinutes;
    //  오늘 렘 수면시간
    private int avgRemSleepMinutes;
    //  오늘 얇은 수면시간
    private int avgLightSleepMinutes;



    // 데이터 평균 내는 메서드
    public static AverageData makeAvg(List<HealthData> list){
        //리스트에서 각각의 요소를 평균냄. (null값이나 0을 제외하려면 필터설정을 아래와 같이 걸면 됨)
        // double avgStep = weekList.stream().filter(d->d.getStep() !=null).mapToInt(HealthData::getStep).average().orElse(0);
        //       double avgHeartBeat = weekList.stream().filter(d->d.getHeartbeat() !=0).mapToInt(HealthData::getHeartbeat).average().orElse(0);

        int avgStep1 = (int)list.stream().mapToInt(HealthData::getStep).average().orElse(0);
        int avgHeartBeat1 = (int)list.stream().mapToInt(HealthData::getHeartbeat).average().orElse(0);
        int avgDistancd1 = (int)list.stream().mapToDouble(HealthData::getDistance).average().orElse(0);
        int avgCalory1 = (int)list.stream().mapToDouble(HealthData::getCalory).average().orElse(0);
        int avgActiveCalory1 = (int)list.stream().mapToDouble(HealthData::getActiveCalory).average().orElse(0);
        int avgTotalSleepMinutes1 = (int)list.stream().mapToDouble(HealthData::getTotalSleepMinutes).average().orElse(0);
        int avgDeepSleepMinutes1 = (int)list.stream().mapToDouble(HealthData::getDeepSleepMinutes).average().orElse(0);
        int avgLightSleepMinutes1 = (int)list.stream().mapToDouble(HealthData::getLightSleepMinutes).average().orElse(0);
        int avgRemSleepMinutes1 = (int)list.stream().mapToDouble(HealthData::getRemSleepMinutes).average().orElse(0);

        return AverageData.builder().avgStep(avgStep1).avgHeartBeat(avgHeartBeat1).avgDistancd(avgDistancd1).
                avgCalory(avgCalory1).avgActiveCalory(avgActiveCalory1).avgTotalSleepMinutes(avgTotalSleepMinutes1)
                .avgDeepSleepMinutes(avgDeepSleepMinutes1).avgLightSleepMinutes(avgLightSleepMinutes1).avgRemSleepMinutes(avgRemSleepMinutes1).build();

    }




}
