package silverpotion.userserver.openAi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HealtReportOfDepReqDto {

            //피보호자 id
            Long dependentId;
            //조회할 날짜 입력(yyyy-mm-dd)
            String selectedDate;



}
