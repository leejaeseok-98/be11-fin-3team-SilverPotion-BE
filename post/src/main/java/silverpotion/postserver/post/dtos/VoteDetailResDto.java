package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.comment.dtos.CommentListResDto;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.Vote;
import silverpotion.postserver.post.domain.VoteOptions;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoteDetailResDto {
    private Long voteId;
    private Long writerId;//작성자 id
    private Long voteLikeCount; //좋아요 수
    private String isLike;//좋아요 여부
    private Long commentCount; //댓글 수
    private List<CommentListResDto> commentList; //댓글목록
    private String nickname; //닉네임
    private String profileImage; //프로필 사진
    private List<VoteOptions> voteOption; //투표항목
    private String description;//설명
    private boolean multipleChoice; //복수선택 여부
    private LocalDateTime createTime; //생성시간
    private LocalDateTime closeTime; //마감시간
    private Long participantsCount;//참가자 수

    public static VoteDetailResDto fromEntity(Vote vote, Long voteLikeCount,Long commentCount,String isLike, Long participantsCount,UserProfileInfoDto writerInfo,List<CommentListResDto> commentList) {
        return VoteDetailResDto.builder()
                .voteId(vote.getVoteId())
                .writerId(writerInfo.getUserId())
                .voteOption(vote.getVoteOptions())
                .description(vote.getDescription())
                .multipleChoice(vote.isMultipleChoice())
                .commentList(commentList)
                .voteLikeCount(voteLikeCount)
                .commentCount(commentCount)
                .nickname(writerInfo.getNickname())
                .profileImage(writerInfo.getProfileImage())
                .participantsCount(participantsCount)
                .isLike(isLike)
                .createTime(vote.getCreatedTime())
                .closeTime(vote.getCloseTime())
                .build();
    }

}
