package silverpotion.userserver.userDetailHealthInfo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.userDetailHealthInfo.domain.UserDetailHealthInfo;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDetailHealthInfoCreateReqDto {
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


    //엔티티변환 메서드
    public UserDetailHealthInfo toEntityFromReqForCreation(User user){
        return UserDetailHealthInfo.builder()
                .user(user)
                .height(this.height)
                .weight(this.weight)
                .disease(this.disease)
                .smokingHabit(this.smokingHabit)
                .alcoholHabit(this.alcoholHabit)
                .exerciseHabit(this.exerciseHabit)
                .targetStep(this.targetStep)
                .targetCalory(this.targetCalory)
                .empathyNeedScore(this.empathyNeedScore)
                .achieveScore(this.achieveScore)
                .peopleConnectivityScore(this.peopleConnectivityScore)
                .energyScore(this.energyScore)
                .build();
    }






}
