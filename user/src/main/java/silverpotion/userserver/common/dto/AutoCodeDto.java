package silverpotion.userserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutoCodeDto {
    private String phoneNumber;
    private String autoCode; // 인증번호 생성용
    private String inputCode; // 클라이언트가 입력한 인증번호 검증용
}
