package silverpotion.userserver.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.fireBase.domain.TokenRequest;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.healthScore.domain.HealthScore;
import silverpotion.userserver.payment.domain.CashItem;
import silverpotion.userserver.user.dto.*;
import silverpotion.userserver.userDetailHealthInfo.domain.UserDetailHealthInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //활동 지역
    @Column(nullable = false)
    private String region;
    //유저상세건강정보
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserDetailHealthInfo userDetailHealthInfo;
    //유저 건강데이터와 상세건강정보를 기반으로 헬스점수
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HealthScore> healthScores = new ArrayList<>();

//    정지 만료일 (이 날짜 전까지 정지 상태)
    private LocalDateTime banUntil;

    //채팅을 위한 온라인체크 및 마지막으로 본 시간.
    private boolean isOnline;
    private LocalDateTime lastSeenAt;

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
        if(dto.getRegion() != null){
            this.region = dto.getRegion();
        }
    }

    public void changeRole(Role role){
        this.role = role;
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

    public String mySex(){
        return this.sex.toString();
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

    // 일간 건강프롬프트 생성 메서드
    public UserPromptDto healthPromptForDay(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        //헬스리포트는 매일 새벽 1시에 만들어짐. 어제 건강데이터를 기반으로
        HealthData dailyData = this.myHealthData.stream().filter(h->h.getDataType()==DataType.DAY).filter(h->h.getCreatedDate().equals(yesterday))
                .findFirst().orElseThrow(()->new EntityNotFoundException("리포트가 생성되지 않았습니다"));
        //여기서부터 프롬프트
        String promt ="나이 : " + this.myAge() +", 성별 : " + this.sex.toString() +
                ", 어제 평균 걸음 횟수 " + dailyData.getStep() + "어제 평균 심박수 :" + dailyData.getHeartbeat()
                +", 어제 평균 걸은 거리 : " + dailyData.getDistance() + "어제 평균 소모 칼로리 : " + dailyData.getCalory()
                +", 어제  평균 총 수면시간(분) : " + dailyData.getTotalSleepMinutes() + "어제 평균 깊은 수면시간(분) : " +dailyData.getDeepSleepMinutes()
                +", 어제 평균 렘 수면시간(분) : " + dailyData.getRemSleepMinutes() + "어제 평균 얕은 수면시간(분) : " + dailyData.getLightSleepMinutes();

        return UserPromptDto.builder().healthData(dailyData).prompt(promt).build();
    }


    // 주간 건강프롬프트 생성 메서드
    public UserPromptDto healthPromptForWeek(){
        LocalDate today = LocalDate.now();
        //전주에 대한 건강데이터는 매주 월요일에 생성되고, 그 건강데이터를 기반으로 건강리포트가 만들어짐.따라서 이 메서드는 매주 월요일에 호출될 것이니 월요일 기준 오늘만들어진 Week타입 건강데이터를 들고 오면 됨
        HealthData weekData = this.myHealthData.stream().filter(h->h.getDataType()==DataType.WEEKAVG).filter(h->h.getCreatedDate().equals(today))
                .findFirst().orElseThrow(()->new EntityNotFoundException("리포트가 생성되지 않았습니다"));
        //여기서부터 프롬프트
        String promt ="나이 : " + this.myAge() +", 성별 : " + this.sex.toString() +
                ", 이번 주 평균 걸음 횟수 " + weekData.getStep() + "이번 주 평균 심박수 :" + weekData.getHeartbeat()
                +", 이번 주 평균 걸은 거리 : " + weekData.getDistance() + "이번 주 평균 소모 칼로리 : " + weekData.getCalory()
                +", 이번 주  평균 총 수면시간(분) : " + weekData.getTotalSleepMinutes() + "이번 주 평균 깊은 수면시간(분) : " +weekData.getDeepSleepMinutes()
                +", 이번 주 평균 렘 수면시간(분) : " + weekData.getRemSleepMinutes() + "이번주 평균 얉은 수면시간(분) : " + weekData.getLightSleepMinutes();

        return UserPromptDto.builder().healthData(weekData).prompt(promt).build();

    }

    // 월간 건강프롬프트 생성 메서드
    public UserPromptDto healthPromptForMonth(){

                LocalDate today = LocalDate.now();
        HealthData monthData = this.myHealthData.stream().filter(h->h.getDataType()==DataType.MONTHAVG).filter(h->h.getCreatedDate().equals(today))
                .findFirst().orElseThrow(()->new EntityNotFoundException("리포트가 생성되지 않았습니다"));
        //여기서부터 프롬프트
        String promt ="나이 : " + this.myAge() +", 성별 : " + this.sex.toString() +
                ", 이번 달 평균 걸음 횟수 " + monthData.getStep() + "이번 달 평균 심박수 :" + monthData.getHeartbeat()
                +", 이번 달 평균 걸은 거리 : " + monthData.getDistance() + "이번 달 평균 소모 칼로리 : " + monthData.getCalory()
                +", 이번 달  평균 총 수면시간(분) : " + monthData.getTotalSleepMinutes() + "이번 달 평균 깊은 수면시간(분) : " +monthData.getDeepSleepMinutes()
                +", 이번 달 평균 렘 수면시간(분) : " + monthData.getRemSleepMinutes() + "이번 달 평균 얕은 수면시간(분) : " + monthData.getLightSleepMinutes();

        return UserPromptDto.builder().healthData(monthData).prompt(promt).build();

    }
    // 내 체질량지수 리턴 메서드
    public Map<Double,String> makingBmi(){
      int height =  this.getUserDetailHealthInfo().getHeight();
      int weight =  this.getUserDetailHealthInfo().getWeight();
      Double heightM = height/100.0; //키를 m로 변환
      Double bmi = weight/(heightM*heightM);
      String weightCategory ="";

      if(bmi<18.5){
          weightCategory="저체중";
      } else if(bmi<=24.9){
          weightCategory="정상체중";
      } else if(bmi<=29.9){
          weightCategory="과체중";
      } else{
          weightCategory="비만";
      }
        Map<Double,String> bmiInfo = new HashMap<>();
        bmiInfo.put(bmi,weightCategory);
        return bmiInfo;
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
                .healingPotion(this.healingPotion).id(this.id).region(this.region)
                .profileImage(this.profileImage)
                .dependentName(dependentNames)
                .protectorName(protectorNames)
                .build();
    }
    public UserLinkedUserDto toLinkUserDtoFromEntity(){
        return UserLinkedUserDto.builder().userId(this.id).name(this.name).profileImg(this.profileImage).loginId(this.loginId).build();
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
                .nickname(this.nickName).profileImage(this.profileImage).birthday(this.birthday).build();
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
