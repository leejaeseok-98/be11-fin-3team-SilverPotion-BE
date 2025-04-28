package silverpotion.userserver.openAi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HealthReportSelectReqDto {
    //조회할 유저의 아이디
    private String loginId;
    //타입(일간 리포트인지 주간 리포트인지 월간 리포트 인지)
    private String type;
    //날짜
    private String date;

}
