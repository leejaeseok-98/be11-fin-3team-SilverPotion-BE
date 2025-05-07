package silverpotion.postserver.post.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.comment.dtos.CommentListResDto;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.Vote;
import silverpotion.postserver.post.domain.VoteAnswer;
import silverpotion.postserver.post.domain.VoteOptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime; //생성시간
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closeTime; //마감시간
    private Long participantsCount;//참가자 수
    private boolean hasVoted; //투표 여부
    private List<VoteOptionDto> voteOptions;//전체 투표항목 결과
    private List<VoteOptionDto> selectOption;//유저가 선택한 투표항목결과

    public static VoteDetailResDto fromEntity(Vote vote, List<VoteAnswer> userAnswer , Long voteLikeCount, Long commentCount, String isLike, Long participantsCount, UserProfileInfoDto writerInfo, List<CommentListResDto> commentList, boolean hasVoted) {
        //투표 총 참여자 수 -> 투표게시물에 옵션리스트를 가져오고 한개의 옵션의 참여자 수를 더해서 총합을 구함
        int totalVoteCount = vote.getVoteOptions().stream().mapToInt(opt ->opt.getAnswers().size()).sum();

        //전체 투표 항목 결과 데이터
        List<VoteOptionDto> voteOptions = vote.getVoteOptions().stream().map(opt -> VoteOptionDto.builder()
                .optionId(opt.getId())
                .optionText(opt.getOptionText())
                .voteCount(opt.getAnswers().size())
                .voteRadio(totalVoteCount == 0 ? 0 : (int) Math.round((double) opt.getAnswers().size() * 100 / totalVoteCount))
                .build())
                .collect(Collectors.toList());

        // 로그인 유저 투표 항목 결과 데이터
        List<VoteOptionDto> voteOptionDtos = userAnswer.stream().map(
                answer -> {
                    VoteOptions option = answer.getVoteOption();
                    return VoteOptionDto.builder()
                            .optionId(option.getId())
                            .optionText(option.getOptionText())
                            .voteCount(option.getAnswers().size())
                            .voteRadio(totalVoteCount == 0 ? 0 : (int) Math.round((double) option.getAnswers().size() * 100 / totalVoteCount))
                            .build();
                }).collect(Collectors.toList());

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
                .hasVoted(hasVoted)
                .voteOptions(voteOptions)
                .selectOption(voteOptionDtos)
                .build();
    }

}
