package silverpotion.userserver.healthScore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HealthScoreResDto {
    //조회용 Dto

    private int totalScore;
    //헬스점수 계산에 필요한 하위점수들
    //활동 점수(걸음수,칼로리,운동습관) 0.4 가중치
    private int activityScore;
    //신체상태 점수(심박수,수면,수면단계,BMI) 0.3 가중치
    private int bodyScore;
    //생활습관 점수(흡연,음주,기저질환) 0.3 가중치
    private int habitScore;
    //만들어진 날짜
    LocalDate createdDate;








}
