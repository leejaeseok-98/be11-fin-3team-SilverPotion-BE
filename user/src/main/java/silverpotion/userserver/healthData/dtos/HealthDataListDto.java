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
    //  오늘 총 수면시간
    private int totalSleepMinutes;
    //  오늘 깊은 수면시간
    private int deepSleepMinutes;
    //  오늘 렘 수면시간
    private int remSleepMinutes;
    //  오늘 얇은 수면시간
    private int lightSleepMinutes;
    // 언제에 대한 데이터인지(프론트 화면에 띄우기 위한 용도)
    private String period;
    // 프로필 이미지
    private String imgUrl;



}
