package silverpotion.userserver.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.admin.domain.AdminRole;
import silverpotion.userserver.admin.utils.MaskingUtils;
import silverpotion.userserver.user.domain.BanYN;
import silverpotion.userserver.user.domain.Role;
import silverpotion.userserver.user.domain.User;

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

    public static AdminUserListDto from(User user){
        return AdminUserListDto.builder()
                .id(user.getId())
                .email(MaskingUtils.maskEmail(user.getEmail()))
                .name(MaskingUtils.maskName(user.getName()))
                .birthday(MaskingUtils.maskBirthday(user.getBirthday()))
                .nickname(user.getNickName())
                .region(user.getRegion())
                .role(user.getRole())
                .createdDate(user.getCreatedTime())
                .banYn(user.getBanYN())
                .build();
    }
}
