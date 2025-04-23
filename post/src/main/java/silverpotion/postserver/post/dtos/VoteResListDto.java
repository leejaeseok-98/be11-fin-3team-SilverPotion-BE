package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostCategory;
import silverpotion.postserver.post.domain.Vote;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoteResListDto {
    private Long votedId;//id
    private String title;//제목
    private String description;//설명
    private String nickname;
    private String profileImage;
    private Long writerId;
    private LocalDateTime createTime;
    private LocalDateTime closeTime;
    private boolean isParticipating;//참여 여부
    private PostCategory postCategory;
    private Long likeCount;

    public static VoteResListDto fromEntity(Vote vote,UserProfileInfoDto writerInfo,boolean isParticipating) {
        return VoteResListDto.builder()
                .votedId(vote.getVoteId())
                .title(vote.getTitle())
                .description(vote.getDescription())
                .nickname(writerInfo.getNickname())
                .postCategory(vote.getPostCategory())
                .profileImage(writerInfo.getProfileImage())
                .writerId(vote.getWriterId())
                .isParticipating(isParticipating)
                .createTime(vote.getCreatedTime())
                .closeTime(vote.getCloseTime())
                .likeCount(vote.getLikeCount())
                .build();
    }
}
