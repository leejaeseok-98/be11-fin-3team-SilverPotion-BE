package silverpotion.userserver.healthData.domain;

import jakarta.persistence.*;
import lombok.*;
import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.healthData.dtos.HealthAvgDataDto;
import silverpotion.userserver.healthData.dtos.HealthDataListDto;
import silverpotion.userserver.healthData.dtos.HealthSyncDto;
import silverpotion.userserver.openAi.domain.HealthReport;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class HealthData extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //  하루 평균 심박
    private int heartbeat;
    //  오늘 걸음 수
    private int step;
    //  오늘 걸은 거리
    private int distance;
    //  오늘 총소모 칼로리
    private int calory;
    //  오늘 활동 칼로리
    private int activeCalory;
    //  일일,주간,월간 데이터
    @Enumerated(EnumType.STRING)
    private DataType dataType;
    // 누구의 데이터인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    //  생성날짜
    private LocalDate createdDate;




    public void update(HealthSyncDto dto,int averageBpm){
        this.heartbeat =averageBpm;
        this.calory = dto.getCaloriesBurnedData().intValue();
        this.activeCalory = dto.getActiveCaloriesBurned().intValue();
        this.step = dto.getStepData().get(0);
        this.distance = dto.getDistanceWalked().intValue();

    }



    //    조회용 dto객체 변환 메서드
    public HealthDataListDto toListDtoFromEntity(){
        return HealthDataListDto.builder()
                .step(this.step).heartbeat(this.heartbeat).calory(this.calory)
                .activeCalory(this.activeCalory).distance(this.distance)
                .build();
    }

    // 평균조회용 dto객체 변환 메서드
    public HealthAvgDataDto toAvgDtoFromEntity(){
        return HealthAvgDataDto.builder()
                .heartbeat(this.heartbeat).step(this.step)
                .calory(this.calory).activeCalory(this.activeCalory)
                .distance(this.distance)
                .build();
    }



}
