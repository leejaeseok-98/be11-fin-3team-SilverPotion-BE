package silverpotion.userserver.userDetailHealthInfo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.userDetailHealthInfo.dtos.UserDetailHealthInfoCreateReqDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class UserDetailHealthInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //키
    private int height;
    //몸무게
    private int weight;
    //질병
    private String disease;
    //흡연 습관
    private String smokingHabit;
    //음주 습관
    private String alcoholHabit;
    //운동 습관
    private String exerciseHabit;
    //목표 걸음수
    private int targetStep;
    //목표 소모칼로리
    private int targetCalory;
    //정서적 위로 필요성
    private int empathyNeedScore;
    //성취성
    private int achieveScore;
    //사교성
    private int peopleConnectivityScore;
    //활동성
    private int energyScore;
    //유저
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



    public void updateInfo(UserDetailHealthInfoCreateReqDto dto){
        this.height = dto.getHeight();
        this.weight = dto.getWeight();
        this.disease = dto.getDisease();
        this.smokingHabit = dto.getSmokingHabit();
        this.alcoholHabit = dto.getAlcoholHabit();
        this.exerciseHabit = dto.getExerciseHabit();
        this.targetStep = dto.getTargetStep();
        this.targetCalory = dto.getTargetCalory();
        this.empathyNeedScore = dto.getEmpathyNeedScore();
        this.achieveScore  = dto.getAchieveScore();
        this.peopleConnectivityScore = dto.getPeopleConnectivityScore();
        this.energyScore = dto.getEnergyScore();
    }







}
