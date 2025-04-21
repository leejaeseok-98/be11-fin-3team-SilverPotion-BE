package silverpotion.postserver.comment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentCreateDto {
    private Long postId;
    private Long voteId;
    private String content;
}
