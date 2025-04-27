package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteOptionsResDto {
    private Long optionId; //옵션id
    private String optionText; //항목 내용
    private boolean isVoted; //이 항목에 투요 했는지 여부
    private Long voteCount;// 득표 수
    private double voteRatio; // 비율
}
