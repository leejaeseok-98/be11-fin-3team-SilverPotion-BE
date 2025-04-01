package silverpotion.userserver.healthData.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.domain.HeartRateData;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HealthSyncDto {
    //걸음수
    private List<Integer>stepData;
    //소모칼로리
    private Double caloriesBurnedData;
    //bpm,측정시간
    private List<HeartRateData> heartRateData;
    //오늘 걸은 거리
    private Double distanceWalked;
    //활동칼로리
    private Double activeCaloriesBurned;



    public HealthData toEntityFromSync(int averageHeartbeat, User user, LocalDate createdDate){
      //Double형타입을 int로 변환할때 만약 null값이면 NullpointerException발생
        return HealthData.builder()
                .step(this.stepData.get(0)).heartbeat(averageHeartbeat)
                .calory(this.caloriesBurnedData.intValue()).activeCalory(this.activeCaloriesBurned.intValue())
                .distance(this.distanceWalked.intValue()).user(user).createdDate(createdDate)
                .dataType(DataType.DAY) //일일 HealthDataType.
                .build();
    }


}
