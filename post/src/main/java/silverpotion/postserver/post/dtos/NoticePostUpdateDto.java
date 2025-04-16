package silverpotion.postserver.post.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import silverpotion.postserver.post.domain.Post;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoticePostUpdateDto implements PostUpdateDto {
    @JsonIgnore
    private List<MultipartFile> postImg;
    private String title;
    private String content;

    public Post savePost(Long userId){
        return Post.builder()
                .writerId(userId)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
