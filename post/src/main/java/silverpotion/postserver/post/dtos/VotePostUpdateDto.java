package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VotePostUpdateDto{
    private Long userId; //작성자id
    private String title;
    private String description; // 투표 내용
    private List<VoteOptionDto> voteOptions; //투표항목 - 객체 리스트로 변경
    private boolean multipleChoice; //복수선택 여부

//    내부 static class로 구성
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VoteOptionDto {
        private String optionText;
    }
}

