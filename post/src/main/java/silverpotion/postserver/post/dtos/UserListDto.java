package silverpotion.postserver.post.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserListDto {
    private Long id;
    private String name;
    private String nickName;
    private String profileImgUrl;
}
