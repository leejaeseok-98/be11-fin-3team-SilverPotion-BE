package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserReturnNameInfoDto {
    //프론트에서 쏴주는 상대방로그인 아이디
    String OpponentId;

}
