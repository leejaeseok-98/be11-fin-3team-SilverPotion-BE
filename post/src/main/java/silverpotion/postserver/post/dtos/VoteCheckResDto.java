package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.post.domain.VoteOptions;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoteCheckResDto {
    private boolean isVoted; //투표여부
    private List<SelectedOption> voteOptions;//투표항목

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class SelectedOption{
        private Long id;
    }

}
