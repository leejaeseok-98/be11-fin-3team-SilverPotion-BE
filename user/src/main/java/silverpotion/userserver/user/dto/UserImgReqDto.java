package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//사용자가 화면에서 로그인 아이디 주면 프로필 이미지 리턴하기 위해
public class UserImgReqDto {
    private String loginId;
}
