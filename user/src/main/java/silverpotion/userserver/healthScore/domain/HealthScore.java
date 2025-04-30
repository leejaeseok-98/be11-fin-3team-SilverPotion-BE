package silverpotion.userserver.healthScore.domain;

import jakarta.persistence.*;
import lombok.*;
import silverpotion.userserver.healthScore.dtos.HealthScoreResDto;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.userDetailHealthInfo.domain.UserDetailHealthInfo;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class HealthScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //진짜 헬스점수
    private int totalScore;
    //헬스점수 계산에 필요한 하위점수들
    //활동 점수(걸음수,칼로리,운동습관) 0.4 가중치
    private int activityScore;
    //신체상태 점수(심박수,수면,수면단계,BMI) 0.3 가중치
    private int bodyScore;
    //생활습관 점수(흡연,음주,기저질환) 0.3 가중치
    private int habitScore;
    //만들어진 날짜
    private LocalDate createdDate;
    //타입
    private Type type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



//    dto변환메서드
    public HealthScoreResDto toDto(){
        return HealthScoreResDto.builder().totalScore(this.totalScore).activityScore(this.activityScore)
                .bodyScore(this.bodyScore).habitScore(this.habitScore).createdDate(this.createdDate).build();
    }


}
