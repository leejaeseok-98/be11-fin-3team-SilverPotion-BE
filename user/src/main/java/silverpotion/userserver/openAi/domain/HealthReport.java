package silverpotion.userserver.openAi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.openAi.dto.HealthReportDto;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class HealthReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDate createdDate;
    @Enumerated(EnumType.STRING)
    private DataType dataType;
    @OneToOne
    @JoinColumn(name = "healthdata_id")
    private HealthData healthData;
    //어느 날이나 기간에 대한 리포트인지
    private String period;


//  지난 헬스리포트 조회용 dto 리턴 메서드
    public HealthReportDto toReportDtoFromEntity(){
        return HealthReportDto.builder().text(this.text).date(this.createdDate).period(this.period).build();
    }




}
