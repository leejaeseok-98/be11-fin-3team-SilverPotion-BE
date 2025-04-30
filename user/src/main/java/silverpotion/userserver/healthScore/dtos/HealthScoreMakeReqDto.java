package silverpotion.userserver.healthScore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HealthScoreMakeReqDto {
//            헬스점수 만들려고 프론트에서 보내는 데이터
//    조회할 사용자
    private String UserId;
//    만든 날짜
    private String date;
//    타입
    private String type;

}
