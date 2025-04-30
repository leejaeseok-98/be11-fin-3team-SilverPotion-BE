package silverpotion.userserver.healthScore.domain;

import jakarta.persistence.*;
import lombok.*;
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
    //신체상태 점수(심박수,수면,수면단계,BMI) 0.2 가중치
    private int bodyScore;
    //생활습관 점수(흡연,음주,기저질환) 0.4 가중치
    private int habitScore;
    //만들어진 날짜
    LocalDate createdDate;
    //타입
    private Type type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    //habitScore 도출 메서드
    //음주습관 30점(as) + 흡연습관 40점(ss) + 기저질환30점(ds)
    public int whatsMyHabitScore(UserDetailHealthInfo info){
        String alcoholHabit = info.getAlcoholHabit();
        String smokingHabit = info.getSmokingHabit();
        String disease = info.getDisease();

        int as =0;
        int ss =0;
        int ds =0;

        if(alcoholHabit == "전혀 안함"){
            as = 30;
        } else if(alcoholHabit == "월 1-2회"){
            as = 25;
        } else if(alcoholHabit == "주 1-2회"){
            as = 10;
        }else{ //주 3회이상
            as = 0;
        }

        if(smokingHabit == "전혀 안함"){
            ss = 40;
        } else if(smokingHabit == "하루 5개피 이하"){
            ss = 20;
        } else if(smokingHabit == "하루 반갑 이상"){
            ss = 10;
        } else {
            ss = 0;
        }

        if(disease == "없음"){
            ds = 30;
        } else{
         int count = disease.split(",").length;
        }







    }






}
