package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.post.domain.PostFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostVoteResDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String type;
    private String nickname;
    private String profileImage;
    private List<String> imageUrls;

    private Integer viewCount;       // POST 전용
    private Boolean multipleChoice;  // VOTE 전용
    private List<String> voteOptions;// VOTE 전용
}
