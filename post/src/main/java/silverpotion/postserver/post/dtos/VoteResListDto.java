package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.Vote;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoteResListDto {
    private Long votedId;//id
    private String title;//제목
    private String description;//설명
    private List<String> voteOptions; //투표항목
    private boolean multipleChoice; //복수선택 여부
    private String nickname;
    private String profileImage;
    private Long writerId;

    public static VoteResListDto fromEntity(Vote vote, UserProfileInfoDto writerInfo) {
        return VoteResListDto.builder()
                .votedId(vote.getVoteId())
                .title(vote.getTitle())
                .description(vote.getDescription())
                .voteOptions(vote.getVoteOptions())
                .multipleChoice(vote.isMultipleChoice())
                .nickname(writerInfo.getNickname())
                .profileImage(writerInfo.getProfileImage())
                .writerId(vote.getWriterId())
                .build();
    }
}
