package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateUserDto {
    private Long userId;
    private String nickname;

    public static PostCreateUserDto postCreateUserDto(Long userId, String nickname){
        return PostCreateUserDto.builder()
                .userId(userId)
                .nickname(nickname)
                .build();
    }
}
