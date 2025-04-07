package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.user.domain.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private Long id;
    private String nickName;
    private String birthday;
    private String loginId;
    private String name;

    public UserDto(User user) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.birthday = user.getBirthday();
        this.loginId = user.getLoginId();
        this.name = user.getName();
    }
}