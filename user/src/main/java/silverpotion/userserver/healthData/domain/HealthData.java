package silverpotion.userserver.healthData.domain;

import jakarta.persistence.*;
import lombok.*;
import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.healthData.dtos.HealthDataListDto;
import silverpotion.userserver.healthData.dtos.HealthSyncDto;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDate;

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
    private Integer step;
    //  오늘 걸은 거리
    private Double distance;
    //  오늘 총소모 칼로리
    private Double calory;
    //  오늘 활동 칼로리
    private Double activeCalory;
    //  누구의 데이터인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    //  생성날짜
    private LocalDate createdDate;
    //  일주일 평균 계산 메서드





    //    조회용 dto객체 변환 메서드
    public HealthDataListDto toListDtoFromEntity(){
        return HealthDataListDto.builder()
                .step(this.step).heartbeat(this.heartbeat).calory(this.calory)
                .activeCalory(this.activeCalory).distance(this.distance)
                .build();
    }
}
