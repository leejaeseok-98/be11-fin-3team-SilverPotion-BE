package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteOptionReqDto {
    private Long voteId; //투표게시물id
    private List<Long> optionIds; //여러 옵션 선택 가능
}
