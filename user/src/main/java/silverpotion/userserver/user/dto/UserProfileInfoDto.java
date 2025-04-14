package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class  UserProfileInfoDto {
    private Long userId;
    private String nickname;
    private String address; // ex.도봉구
    private String profileImage;

    public static UserProfileInfoDto userProfileInfoDto(Long userId, String nickname,String profileImage,String address){
        return UserProfileInfoDto.builder()
                .userId(userId)
                .nickname(nickname)
                .profileImage(profileImage)
                .address(address)
                .build();
    }
}
