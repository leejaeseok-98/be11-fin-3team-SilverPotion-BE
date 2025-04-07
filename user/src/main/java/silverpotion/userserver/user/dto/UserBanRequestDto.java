package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserBanRequestDto {
    private Long userId; //차단당한 유저id
    private LocalDateTime banUntil; //정지
}
