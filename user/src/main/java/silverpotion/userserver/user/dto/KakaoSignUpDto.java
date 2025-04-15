package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoSignUpDto {
    private String loginId; //소셜로그인id
    private String email;
    private String nickname;
}
