package silverpotion.userserver.user.controller;


import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.HTTP;
import silverpotion.userserver.admin.domain.Admin;
import silverpotion.userserver.admin.repository.AdminRepository;
import silverpotion.userserver.common.auth.JwtTokenProvider;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.payment.dtos.CashItemOfPaymentListDto;
import silverpotion.userserver.user.domain.Role;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.dto.*;
import silverpotion.userserver.user.service.GoogleService;
import silverpotion.userserver.user.service.KakaoService;
import silverpotion.userserver.user.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/silverpotion/user")
public class    UserController {
    private final UserService userService;
    private final GoogleService googleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final AdminRepository adminRepository;


    public UserController(UserService userService, GoogleService googleService, JwtTokenProvider jwtTokenProvider, KakaoService kakaoService, AdminRepository adminRepository) {
        this.userService = userService;
        this.googleService = googleService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoService = kakaoService;
        this.adminRepository = adminRepository;
    }
//    0.헬스체크용 url(배포용)
    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthCheck(){
        return new ResponseEntity<>("transmission sucess",HttpStatus.OK);
    }

//    1.회원가입
    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@RequestBody UserCreateDto dto){
       Long id =userService.userCreate(dto);
       return new ResponseEntity<>(new CommonDto(HttpStatus.CREATED.value(),"user is created successfully",id), HttpStatus.CREATED);
    }

//    1-2.회원가입 아이디,이메일,닉네임 중복체크
    @GetMapping("/checkDuplicate")
    public ResponseEntity<?> checkDuplicate(@RequestParam String field, @RequestParam String value){
        boolean isDuplicate = userService.isDuplicate(field,value);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "check need",isDuplicate),HttpStatus.OK);
    }

//    2-1.로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){
        Map<String, Object> loginInfo = userService.login(dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"login success", loginInfo),HttpStatus.OK);
    }

//    2-2.로그인(리프레시 토큰 발급)
    @PostMapping("/refresh-token")
    public ResponseEntity<?> recreateAccessToken(@RequestBody UserRefreshDto dto){
        Map<String,Object> loginInfo = userService.recreateAccessToken(dto);
        if(loginInfo.get("token").equals("fail")){
            return new ResponseEntity<>(new CommonDto(HttpStatus.BAD_REQUEST.value(), "cannot recreate accessToken",loginInfo),HttpStatus.BAD_REQUEST);
        } else{
            return new ResponseEntity<>(new CommonDto(HttpStatus.CREATED.value(), "success",loginInfo),HttpStatus.CREATED);
        }
    }

//    로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-User-LoginId")String loginId){
        userService.logout(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"logout success",loginId),HttpStatus.OK);
    }


//    3.회원정보수정(마이프로필 수정)
    @PatchMapping("/update")
    public ResponseEntity<?> userUpdate(@RequestBody UserUpdateDto dto,@RequestHeader("X-User-LoginId")String loginId){
        Long id = userService.update(dto,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "update success", id),HttpStatus.OK);
    }

// 4.내 정보 조회(마이페이지 프로필 조회,여기에 연결된 보호자,피보호자이름도 조회할 수 있게 해놨는데 프론트에서는 아래 5,6번 쓰는게 더 나을 듯)
    @GetMapping("/myprofile")
    public ResponseEntity<?> myProfile(@RequestHeader("X-User-LoginId")String loginId){
        UserMyPageDto dto = userService.userMyPage(loginId);
    return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "success",dto),HttpStatus.OK);
    }

//   5. 연결된 피보호자 조회
    @GetMapping("/myDependentList")
    public ResponseEntity<?> whoMyDependents(@RequestHeader("X-User-LoginId")String loginId){
      List<UserLinkedUserDto>dependents = userService.whoMyDependents(loginId);
    return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"success",dependents ),HttpStatus.OK);
    }

//    6.연결된 보호자 조회
    @GetMapping("/myProtectList")
    public ResponseEntity<?> whoMyProtectors(@RequestHeader("X-User-LoginId")String loginId){
        List<UserLinkedUserDto>protectors = userService.whoMyProtectors(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"success",protectors),HttpStatus.OK);
    }

    //    7.userId 조회(UserClient)
    @GetMapping("/userId")
    public Long getUserIdByLoginId(@RequestParam String loginId){
        return userService.getUserIdByLoginId(loginId);
    }

    //    8.loginId와 nickname 조회(UserClient)
    @GetMapping("/postUserInfo")
    public UserProfileInfoDto getUserProfileInfo(@RequestParam String loginId){
        return userService.getUserProfileInfo(loginId);
    }

    //    9.userId와 nickname 조회(UserClient)
    @GetMapping("/writer/postUserInfo")
    public UserProfileInfoDto getUserProfileInfo(@RequestParam Long userId){
        return userService.getUserProfileInfo(userId);
    }

    //  10. user list 전체 조회
    @GetMapping("/list")
    public ResponseEntity<?> findAllUser(UserListDto dto) {
        List<UserListDto> list = userService.findAll(dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "userList is uploaded successfully", list), HttpStatus.OK);
    }
    // Feign chat. getNicknameByUserId (Feign 용)
    @GetMapping("/{userId}/nickname")
    public String getNickNameByUserId(@PathVariable Long userId) {
        User user = userService.getNickNameByUserId(userId);
        System.out.println("유저 정보조회 login ID:" + user.getNickName());
        return user.getNickName();
    }
    // Feign chat. id로 loginid 찾기 (Feign 용)
    @GetMapping("/loginId")
    public String getLoginIdByUserId(@RequestParam Long id) {
        User user = userService.getLoginIdByUserId(id);
        System.out.println("유저 정보조회 login ID:" + user.getLoginId());
        return user.getLoginId();
    }

    // 11. 프로필 이미지 등록 및 수정
    @PostMapping("/profileImg")
    public ResponseEntity<?> postProfileImage(@RequestHeader("X-User-LoginId")String loginId,UserProfileImgDto dto){
         String s3Url = userService.postProfileImage(loginId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "sucess",s3Url),HttpStatus.OK);
    }

   // 12. 상대프로필 조회
    @GetMapping("/yourProfile/{id}" )
    public ResponseEntity<?> yourProfile(@PathVariable("id") Long id){
                  UserProfileInfoDto dto = userService.yourProfile(id);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"user's profile is uploaded successfully",dto),HttpStatus.OK);
    }

    // 13. 특정 유저 리스트 조회
    @PostMapping("/profile/list")
    public ResponseEntity<?> getUsersByIds(@RequestBody List<Long> userIds){
        List<UserListDto> userListDtos = userService.getUsersByIds(userIds);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"특정 유저 리스트 조회",userListDtos),HttpStatus.OK);
    }

    //14. 내 결제내역 조회하기
    @GetMapping("/mypayment")
    public ResponseEntity<?> getMyPayments(@RequestHeader("X-User-LoginId")String loginId){
                 List<CashItemOfPaymentListDto> list = userService.getMyPayments(loginId);
                 return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"success",list),HttpStatus.OK);
    }

    //15. 내가 보유한 힐링포션 갯수 조회하기
    @GetMapping("/myownpotion")
    public ResponseEntity<?> getMyPotion(@RequestHeader("X-User-LoginId")String loginId){
           int potions = userService.getMyPotion(loginId);
           return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "success",potions),HttpStatus.OK);
    }

    //16. 로그인 아이디 받으면 해당 아이디를 가진 유저의 이름과 닉네임반환(화상채팅 화면용 api)
    @PostMapping("/whatisyourname")
    public ResponseEntity<?> getMyNames(@RequestHeader("X-User-LoginId")String loginId,@RequestBody UserReturnNameInfoDto dto){
        List<String>nameInfo = userService.getMyNames(loginId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "success",nameInfo),HttpStatus.OK);
    }

    //17. 로그인 아이디 주면 프로필 이미지 리턴(화면용 api)
    @PostMapping("whatisyourpicture")
    public ResponseEntity<?> getProfilePicture(@RequestHeader("X-User-LoginId")String loginId,@RequestBody UserImgReqDto dto){
         String imgUrl =  userService.getProfilePicture(loginId,dto);
         return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "sucess",imgUrl),HttpStatus.OK);
    }

    //18.로그인 아이디 주면 이 사람이 건강세부조사 작성했는지 아닌지 여부 리턴(작성했으면 true, 아니면 false)
    @PostMapping("/havedetailhealthinfo")
    public ResponseEntity<?> haveDetailHealthInfo(@RequestHeader("X-User-LoginId")String loginId, @RequestBody UserHaveDetailHealthInfoReqDto dto){
        boolean yesOrNo = userService.haveDetailHealthInfo(loginId, dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "success",yesOrNo),HttpStatus.OK);

    }

//    게시물 조회시, 작성자 프로필 조회
    @PostMapping("/post/profileInfo")
    public ResponseEntity<?> PostProfileInfo(@RequestBody List<Long> userIds){
        Map<Long, UserProfileInfoDto> dto= userService.getProfileInfoMap(userIds);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"게시물 작성자 유저 리스트 조회",dto),HttpStatus.OK);
    }

//    구글 로그인
    @PostMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestBody RedirectDto redirectDto){
//        access토큰발급
        AccessTokenDto accessTokenDto = googleService.getAccessToken(redirectDto.getCode());

//        사용자 정보 얻기
        GoogleProfileDto googleProfileDto = googleService.getGoogleProfile(accessTokenDto.getAccess_token());
//        회원가입이 되어있지 않다면 회원가입
        User originalUser = userService.userBySocialId(googleProfileDto.getSub());
        String adminRole = null;
        if(originalUser == null){
            SocialSignUpDto signUpDto = new SocialSignUpDto(
                    googleProfileDto.getSub(),
                    googleProfileDto.getEmail(),
                    googleProfileDto.getName()
            );

            return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "need_sign_up",signUpDto),HttpStatus.OK);
        }

//        회원가입 되어있으면 토큰 발급
        else {
            if(!originalUser.getRole().equals(Role.USER)){
                Admin admin = adminRepository.findByUserId(originalUser.getId()).orElseThrow(()-> new EntityNotFoundException("Admin Not Found"));
                adminRole = (admin.getRole() != null) ? admin.getRole().toString() : "ROLE_NONE";
            }
            String jwtToken = jwtTokenProvider.createToken(originalUser.getLoginId(),originalUser.getRole().toString(),originalUser.getId(),originalUser.getProfileImage(),originalUser.getNickName(), originalUser.getName(),adminRole);
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("id",originalUser.getId());
            loginInfo.put("token", jwtToken);
            loginInfo.put("name",originalUser.getName());
            return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"success",loginInfo),HttpStatus.OK);
        }
    }
    //    카카오 로그인
    @PostMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody RedirectDto redirectDto){
//        access토큰발급
        AccessTokenDto accessTokenDto = kakaoService.getAccessToken(redirectDto.getCode());

//        사용자 정보 얻기
        KakaoProfileDto kakaoProfileDto = kakaoService.getKakaoProfile(accessTokenDto.getAccess_token());
//        회원가입이 되어있지 않다면 회원가입
        User originalUser = userService.userBySocialId(kakaoProfileDto.getId());
        String adminRole = null;

        if(originalUser == null){
            KakaoSignUpDto signUpDto = new KakaoSignUpDto(
                    kakaoProfileDto.getId(),
                    kakaoProfileDto.getKakao_account().getEmail(),
                    kakaoProfileDto.getKakao_account().getProfile().getNickname()
            );
            return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "need_sign_up",signUpDto),HttpStatus.OK);
        }

//        회원가입 되어있으면 토큰 발급
        else {
            if(!originalUser.getRole().equals(Role.USER)){
                Admin admin = adminRepository.findByUserId(originalUser.getId()).orElseThrow(()-> new EntityNotFoundException("Admin Not Found"));
                adminRole = (admin.getRole() != null) ? admin.getRole().toString() : "ROLE_NONE";
            }
            String jwtToken = jwtTokenProvider.createToken(originalUser.getLoginId(),originalUser.getRole().toString(),originalUser.getId(),originalUser.getProfileImage(),originalUser.getNickName(), originalUser.getName(),adminRole);
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("id",originalUser.getId());
            loginInfo.put("token", jwtToken);
            loginInfo.put("name", originalUser.getName());
            return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"success",loginInfo),HttpStatus.OK);
        }
    }

    //  비밀번호 변경
    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestHeader("X-User-LoginId")String loginId,@RequestBody ChangePasswordDto dto){
        Long userId = userService.changePassword(loginId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "password is changed",userId),HttpStatus.OK);
    }

    // 회원탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestHeader("X-User-LoginId")String loginId){
       String nickName = userService.withdraw(loginId);
       return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "goodbye...",nickName),HttpStatus.OK);
    }
    // UserId로 UserDto 반환하는 API
    @GetMapping("/id")
    public ResponseEntity<UserDto> getUserById(@RequestParam("id") Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }
}
