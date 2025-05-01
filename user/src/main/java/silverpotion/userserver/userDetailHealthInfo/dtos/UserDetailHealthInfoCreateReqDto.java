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
    //고립감 점수
    private int isolationScore;
    //의욕감 점수
    private int driveScore;
    //자존감 점수
    private int selfEsteemScore;
    //에너지 점수
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
                .isolationScore(this.isolationScore)
                .driveScore(this.driveScore)
                .selfEsteemScore(this.selfEsteemScore)
                .energyScore(this.energyScore)
                .build();
    }






}
