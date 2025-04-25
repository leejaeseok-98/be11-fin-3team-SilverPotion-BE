package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostCategory;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostInitDto {
    private Long userId;
    private PostCategory postCategory;
    private Long gatheringId;

    public static PostInitDto fromEntity(Post draftPost) {
        return PostInitDto.builder()
                .userId(draftPost.getWriterId())
                .postCategory(draftPost.getPostCategory())
                .gatheringId(draftPost.getGathering().getId())
                .build();
    }
}
