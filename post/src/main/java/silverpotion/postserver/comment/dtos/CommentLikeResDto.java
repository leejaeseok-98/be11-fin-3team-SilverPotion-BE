package silverpotion.postserver.comment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentLikeResDto {
    private Long count;
    private Boolean isLike;
}
