package silverpotion.userserver.fireBase.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//앱으로부터 파이어베이스 토큰을 받는 dto
public class TokenRequest {
    private String token;
    private String phoneNumber;
}
