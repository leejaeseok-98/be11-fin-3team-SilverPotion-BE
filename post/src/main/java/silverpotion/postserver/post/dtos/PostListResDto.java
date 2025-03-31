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
    private String isMember;

    public static PostListResDto fromEntity(Post post, Long likeCount, Long commentCount, String isLike, String isMember){
        return PostListResDto.builder()
                .userId(post.getWriterId())
                .nickname(post.getNickName())
                .profileImage()
                .build();
    }

}
