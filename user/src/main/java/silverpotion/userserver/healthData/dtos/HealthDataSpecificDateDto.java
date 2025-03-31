package silverpotion.userserver.healthData.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HealthDataSpecificDateDto {
    //    사용자가 날짜를 입력할때 프론트에서 yyyy-mm-dd형식으로 여기 백엔드로 전달해야 함
    private String specificDate;

}