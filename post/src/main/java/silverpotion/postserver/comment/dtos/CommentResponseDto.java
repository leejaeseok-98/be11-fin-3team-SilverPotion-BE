package silverpotion.postserver.comment.dtos;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String content;
    private List<CommentResponseDto> replies = new ArrayList<>();
}
