package silverpotion.userserver.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserBanRequestDto {
    private Long userId; //차단당한 유저id
    private Long banDays; //정지 기간
}