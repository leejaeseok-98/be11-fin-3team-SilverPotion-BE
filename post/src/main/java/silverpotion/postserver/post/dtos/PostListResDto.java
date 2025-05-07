package silverpotion.postserver.post.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostCategory;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostListResDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private Long postId;
    private String title;
    private String content;
    private Long likeCount;
    private Long commentCount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdTime;
    private String isLike;
    private String isUpdate;
    private PostCategory postCategory;
    private List<String> imageUrls;

    public static PostListResDto fromEntity(Post post, Long likeCount, Long commentCount, String isLike,UserProfileInfoDto writerInfo){
        return PostListResDto.builder()
                .userId(post.getWriterId())
                .nickname(writerInfo.getNickname())
                .title(post.getTitle())
                .profileImage(writerInfo.getProfileImage())
                .postId(post.getId())
                .content(post.getContent())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .createdTime(post.getCreatedTime())
                .isUpdate(determineUpdateStatus(post))
                .isLike(isLike)
                .imageUrls(post.getFileUrls().stream().toList())
                .postCategory(post.getPostCategory())
                .build();
    }

    // 수정 여부 판단 (createdTime과 updatedTime 비교)
    private static String determineUpdateStatus(Post post) {
        return (post.getUpdatedTime() != null && !post.getUpdatedTime().equals(post.getCreatedTime())) ? "Y" : "N";
    }

}
