package silverpotion.postserver.comment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyCommentCreateReqDto {
    private Long parentId;
    private String content;
}

