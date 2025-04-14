package silverpotion.userserver.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.fireBase.domain.TokenRequest;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.payment.domain.CashItem;
import silverpotion.userserver.user.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class User extends silverpotion.userserver.common.domain.BaseTimeEntity {
    //회원 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //성별
    @Enumerated(EnumType.STRING)
    private Sex sex;
    //권한
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;
    //전화번호
    @Column(nullable = false)
    private String phoneNumber;
    //이름
    @Column(nullable = false)
    private String name;
    //생년월일(나이,19940818 이렇게 받을 것)
    @Column(nullable = false)
    private String birthday;
    //로그인아이디
    private String loginId;
    //비밀번호
    private String password;
    //닉네임
    @Column(nullable = false)
    private String nickName;
    //이메일
    @Column(nullable = false)
    private String email;
    //일반주소
    @Column(nullable = false)
    private String address;
    //상세주소
    @Column(nullable = false)
    private String detailAddress;
    //우편번호
    @Column(nullable = false)
    private String zipcode;
    //캐시(힐링포션)
    private int healingPotion;
    //결제내역
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<CashItem> myPaymentList = new ArrayList<>();
    //프로필 이미지
    private String profileImage;
    //회원탈퇴여부
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;
    //내가 피보호자로 들어가는 관계
    @OneToMany(mappedBy = "dependent")
    @Builder.Default
    private List<CareRelation> asDependents = new ArrayList<>();
    //내가 보호자로 들어가는 관계
    @OneToMany(mappedBy = "protector")
    @Builder.Default
    private List<CareRelation> asProtectors = new ArrayList<>();
    //헬스데이터
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<HealthData> myHealthData = new ArrayList<>();
    //파이어베이스 토큰
    private String fireBaseToken;
    //로그인 타입
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    //소셜 로그인 아이디
    private String socialId;
    //정지 여부
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BanYN banYN = BanYN.N;

//    정지 만료일 (이 날짜 전까지 정지 상태)
    private LocalDateTime banUntil;

//---------------------------일반 메서드--------------------------------------------------------


    //회원 정보 수정 메서드
    public void updateUser(UserUpdateDto dto,String newPw){
        if(dto.getEmail() != null){
            this.email = dto.getEmail();
        }
        if(dto.getPhoneNumber() != null){
            this.phoneNumber = dto.getPhoneNumber();
        }
        if(dto.getNickName() != null){
            this.nickName = dto.getNickName();
        }
        if(dto.getPassword() != null){
            this.password = newPw;
        }
        if(dto.getAddress() != null){
            this.address = dto.getAddress();
        }
        if(dto.getZipcode() != null){
            this.zipcode = dto.getZipcode();
        }
        if(dto.getDetailAddress() != null){
            this.detailAddress = dto.getDetailAddress();
        }
    }

    //   이미지 등록 메서드
    public void changeMyProfileImag(String imgUrl){
        this.profileImage = imgUrl;
    }

    // 나이 계산 메서드
    public int myAge(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthday = LocalDate.parse(this.birthday,formatter);
        LocalDate today = LocalDate.now();
        return Period.between(birthday,today).getYears();
    }

    // 실시간 건강프롬프트 생성 메서드
    public UserPromptDto healthPrompt(){

        LocalDate today = LocalDate.now();
        HealthData nowHealthData = this.myHealthData.stream().filter(h->h.getDataType()== DataType.DAY).filter(h->h.getCreatedDate().equals(today))
                .findFirst().orElseThrow(()->new EntityNotFoundException("아직 리포트가 생성되지 않았습니다"));
        //여기서부터 프롬프트

        String promt ="나이 : " + this.myAge() +", 성별 : " + this.sex.toString() +
                      ", 오늘 걸음 횟수 " + nowHealthData.getStep() + "현재까지 평균 심박수 :" + nowHealthData.getHeartbeat()
                      +", 현재까지 걸은 거리 : " + nowHealthData.getDistance() + "현재까지 소모 칼로리 : " + nowHealthData.getCalory()
                      +", 금일 총 수면시간(분) : " + nowHealthData.getTotalSleepMinutes() + "깊은 수면시간(분) : " +nowHealthData.getDeepSleepMinutes()
                      +", 렘 수면시간(분) : " + nowHealthData.getRemSleepMinutes() + "얉은 수면시간(분) : " + nowHealthData.getLightSleepMinutes();

       return UserPromptDto.builder().healthData(nowHealthData).prompt(promt).build();

    }

    //파이어베이스 토큰 저장 메서드
    public void getFireBaseToken(TokenRequest tokenRequest){
        this.fireBaseToken = tokenRequest.getToken();
    }

    // 내가 보유한 힐링포션(캐시템) 개수 업데이트
    public void updateMyHealingPotion(int a){
        this.healingPotion += a;
    }

    // 내가 보유한 힐링포션 개수 조회
    public int howManyPotion(){
        return this.healingPotion;
    }

    //    회원탈퇴 메서드
    public void withdraw(){
        this.delYN = DelYN.Y;
    }






//    ----------------------------DTO관련 메서드------------------------------------------------

    public UserMyPageDto toMyPageDtoFromEntity(List<String> dependentNames, List<String>protectorNames){
        return UserMyPageDto.builder().nickName(this.nickName).name(this.name).email(this.email)
                .sex(this.sex.toString()).phoneNumber(this.phoneNumber).birthday(this.birthday)
                .address(this.address).zipcode(this.zipcode).detailAddress(this.detailAddress)
                .healingPotion(this.healingPotion).id(this.id)
                .dependentName(dependentNames)
                .protectorName(protectorNames)
                .build();
    }
    public UserLinkedUserDto toLinkUserDtoFromEntity(){
        return UserLinkedUserDto.builder().userId(this.id).name(this.name).profileImg(this.profileImage).build();
    }

    public List<String> findNameFromDependentList(){
       List<CareRelation> dependents = this.asProtectors;
       List<String> dependentNames = new ArrayList<>();
       for(CareRelation c : dependents){
          dependentNames.add(c.getDependent().getName());
       }
       return dependentNames;
    }

    public List<String> findNameFromProtectorsList(){
        List<CareRelation> protectors = this.asDependents;
        List<String> protectorNames = new ArrayList<>();
        for(CareRelation c : protectors){
            protectorNames.add(c.getProtector().getName());
        }
        return protectorNames;
    }

    public UserListDto ListDtoFromEntity(){
        return UserListDto.builder()
                .id(this.id)
                .name(this.name)
                .nickName(this.nickName)
                .profileImgUrl(this.profileImage)
                .build();
    }

    public UserProfileInfoDto profileInfoDtoFromEntity(){
        return UserProfileInfoDto.builder().userId(this.id).address(this.address)
                .nickname(this.nickName).profileImage(this.profileImage).build();
    }


    public void BanUntil(LocalDateTime banUntil){
        this.banYN = BanYN.Y;
        this.banUntil = banUntil;
    }

    public boolean shouldBeBanned(){
        return banYN == BanYN.Y && LocalDateTime.now().isBefore(banUntil);
    }
    public void setBanYN(BanYN banYN){
        this.banYN = banYN;
    }

    //    비밀번호 변경
    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }

}
