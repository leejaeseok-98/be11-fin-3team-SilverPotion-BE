package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.post.domain.Post;

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
    private List<String> imageList;
    private String title;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createdTime;
    private String isLike;
    private String isUpdate;

    public static PostListResDto fromEntity(Post post, Long likeCount, Long commentCount, String isLike,UserProfileInfoDto profileInfo){
        return PostListResDto.builder()
                .userId(post.getWriterId())
                .nickname(profileInfo.getNickname())
                .profileImage(profileInfo.getProfileImage())
                .postId(post.getId())
                .imageList(post.getFileUrls())
                .content(post.getContent())
                .likeCount(likeCount)
                .commentCount(commentCount)
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
