package silverpotion.userserver.healthData.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SelectAllInOneReqDto {
    //헬스데이터 조회할 아이디
    private String loginId;
    //조회할 데이터 유형(DAY//WEEKAVG//MONTHAVG)
    private String type;
    //조회할 데이터 날짜
    private String date;

}
