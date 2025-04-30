package silverpotion.userserver.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.admin.domain.AdminRole;
import silverpotion.userserver.user.domain.BanYN;
import silverpotion.userserver.user.domain.Role;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdminUserListDto {
    private Long id;
    private String email;
    private String name;
    private String birthday;
    private String nickname;
    private String region;
    private Role role;
    private LocalDateTime createdDate;
    private BanYN banYn;
}
