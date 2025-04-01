package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.comment.dtos.CommentListResDto;
import silverpotion.postserver.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDetailResDto {
    private Long userId;
    private String nickName;
    private String profileImage;
    private Long postId;
    private List<String> imageList;
    private String content;
    private Long postLikeCount;
    private List<CommentListResDto> commentList;
    private LocalDateTime createdTime;
    private String isUpdate;
    private List<String> hashTag;
    private String isLike;

    public static PostDetailResDto fromEntity(Post post,String profileImage,Long postLikeCount, List<CommentListResDto> commentList, String isLike) {
        return PostDetailResDto.builder()
                .userId(post.getWriterId())
                .nickName(post.getNickName())
                .profileImage(profileImage)
                .postId(post.getId())
                .imageList(post.getFileUrls())
                .content(post.getContent())
                .postLikeCount(postLikeCount)
                .commentList(commentList)
                .createdTime(post.getCreatedTime())
                .isUpdate(determineUpdateStatus(post))
                .isLike(isLike)
                .build();
    }

    // 수정 여부 판단 (createdTime과 updatedTime 비교)
    private static String determineUpdateStatus(Post post) {
        return (post.getUpdatedTime() != null && !post.getUpdatedTime().equals(post.getCreatedTime())) ? "Y" : "N";
    }
}
