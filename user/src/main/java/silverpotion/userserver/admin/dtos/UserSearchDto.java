package silverpotion.userserver.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSearchDto {
    private String nickname;
    private String email;
    private String name;

}
