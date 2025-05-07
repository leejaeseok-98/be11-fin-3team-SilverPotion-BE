package silverpotion.userserver.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.user.domain.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDetailDto {
    private Long id;  //회원 아이디
    private String name; // 이름
    private String nickName; // 닉네임
    private String email; // 이메일
    private String phoneNumber; // 폰 번호
    private String birthday; // 생년월일
    private Sex sex; // 성별
    private String region; // 지역
    private String loginId; // 로그인 아이디
    private SocialType socialType; //로그인 아이디 유형
    private Role role; //관리자 여부
    private DelYN delYN; // 삭제 여부
    private BanYN banYN; // 정지 여부
    private LocalDateTime banUntil; // 정기 기간
//    건강 데이터
    private int healingPotion; // 힐링포션 개수
    private int dependentCount; // 피보호자 수
    private int protectorCount;//보호자 수

    public static UserDetailDto detailList(User user,int dependentCount,int protectorCount){
        return UserDetailDto.builder()
                .id(user.getId())
                .name(user.getName())
                .socialType(user.getSocialType())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .region(user.getRegion())
                .role(user.getRole())
                .delYN(user.getDelYN())
                .banYN(user.getBanYN())
                .banUntil(user.getBanUntil())
                .birthday(user.getBirthday())
                .sex(user.getSex())
                .loginId(user.getLoginId())
                .nickName(user.getNickName())
                .healingPotion(user.getHealingPotion())
                .dependentCount(dependentCount)
                .protectorCount(protectorCount)
                .build();
    }
}
