package silverpotion.postserver.comment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentListResDto {
    private Long id;
    private Long userId;
    private Long parentId;
    private String nickName;
    private String profileImage;
    private String content;
    private Long likeCount;
    private String isUpdate;
    private DelYN isDelete;
    private String isLike;
    private LocalDateTime createdTime;

    public static CommentListResDto fromEntity(Comment comment, Long likeCount, String isLike, UserProfileInfoDto userProfileInfoDto) {
        return CommentListResDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .nickName(userProfileInfoDto.getNickname())
                .profileImage(userProfileInfoDto.getProfileImage())
                .content(comment.getContent())
                .likeCount(likeCount)
                .isUpdate(determineUpdateStatus(comment))
                .isDelete(comment.getDelYN())
                .isLike(isLike)
                .createdTime((comment.getCreatedTime()))
                .build();
    }

    // 수정 여부 판단
    private static String determineUpdateStatus(Comment comment) {
        return (comment.getUpdatedTime() != null && !comment.getUpdatedTime().equals(comment.getCreatedTime())) ? "Y" : "N";
    }
}
