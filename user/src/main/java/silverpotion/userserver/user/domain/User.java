package silverpotion.userserver.user.domain;

import jakarta.persistence.*;
import lombok.*;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.user.dto.*;

import java.time.LocalDate;
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
    //주소(우편번호)
    @Column(nullable = false)
    private String address;
    //주소(지번주소)
    @Column(nullable = false)
    private String streetAddress;
    //주소(상세주소)
    @Column(nullable = false)
    private String detailAddress;
    //캐시
    private Integer cash;
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
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<HealthData> myHealthData = new ArrayList<>();
    //로그인 타입
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    //소셜 로그인 아이디
    private String socialId;

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
        if(dto.getStreetAddress() != null){
            this.streetAddress = dto.getStreetAddress();
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
                      +", 현재까지 걸은 거리 : " + nowHealthData.getDistance() + "현재까지 소모 칼로리 : " + nowHealthData.getCalory();

       return UserPromptDto.builder().healthData(nowHealthData).prompt(promt).build();

    }




    //    회원탈퇴 메서드
    public void withdraw(){
        this.delYN = DelYN.Y;
    }






//    ----------------------------DTO관련 메서드------------------------------------------------

    public UserMyPageDto toMyPageDtoFromEntity(List<String> dependentNames, List<String>protectorNames){
        return UserMyPageDto.builder().nickName(this.nickName).name(this.name).email(this.email)
                .sex(this.sex.toString()).phoneNumber(this.phoneNumber).birthday(this.birthday)
                .address(this.address).streetAddress(this.streetAddress).detailAddress(this.detailAddress)
                .cash(this.cash).id(this.id)
                .dependentName(dependentNames)
                .protectorName(protectorNames)
                .build();
    }
    public UserLinkedUserDto toLinkUserDtoFromEntity(){
        return UserLinkedUserDto.builder().userId(this.id).name(this.name).build();
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
        return UserProfileInfoDto.builder().userId(this.id).streetAddress(this.streetAddress)
                .nickname(this.nickName).profileImage(this.profileImage).build();
    }


}
