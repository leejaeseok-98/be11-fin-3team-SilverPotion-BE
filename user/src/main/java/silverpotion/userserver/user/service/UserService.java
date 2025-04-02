package silverpotion.userserver.user.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.common.auth.JwtTokenProvider;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.dto.*;
import silverpotion.userserver.user.repository.UserRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("refreshToken")
    private final RedisTemplate<String,Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;
    @Value("${cloud.s3.bucket}")
    private String bucket;
    private final S3Client s3Client;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Qualifier("refreshToken") RedisTemplate<String, Object> redisTemplate, JwtTokenProvider jwtTokenProvider, S3Client s3Client) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.s3Client = s3Client;
    }

    // 1.회원가입
    public Long userCreate(UserCreateDto dto){
        if(userRepository.findByLoginIdAndDelYN(dto.getLoginId(), DelYN.N).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 로그인 아이디입니다");
        }
        if(userRepository.findByNickNameAndDelYN(dto.getNickName(), DelYN.N).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다");
        }
        if(userRepository.findByEmailAndDelYN(dto.getEmail(), DelYN.N).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다");
        }


        //      인증 로직 구현 후에 dto의 암호화된 패스워드로 변경해야 함
        User user = userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return user.getId();
    }

//    2-1.로그인
    public Map<String,Object> login(LoginDto dto){
       User user = userRepository.findByLoginIdAndDelYN(dto.getLoginId(),DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 사용자입니다"));
       if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
       }
       String jwtToken = jwtTokenProvider.createToken(user.getLoginId(), user.getRole().toString());
       String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId(), user.getRole().toString());
       redisTemplate.opsForValue().set(user.getLoginId(), refreshToken, 200, TimeUnit.DAYS);
       Map<String, Object> loginInfo = new HashMap<>();
       loginInfo.put("id", user.getLoginId());
       loginInfo.put("token", jwtToken);
       loginInfo.put("refreshToken", refreshToken);

       return loginInfo;

    }

//    2-2.로그인(리프레쉬토큰 재발급)
    public Map<String,Object> recreateAccessToken(UserRefreshDto dto){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyRt)
                .build()
                .parseClaimsJws(dto.getRefreshToken())
                .getBody();

        Map<String,Object> loginInfo = new HashMap<>();
        Object refreshTokenOfDto = redisTemplate.opsForValue().get(claims.getSubject());
        if(refreshTokenOfDto == null || !refreshTokenOfDto.toString().equals(dto.getRefreshToken())){
            loginInfo.put("token", "fail");
            return loginInfo;
        } //레디스에 리프레시토큰 값이 없었거나 사용자의 리프레시토큰갑과 일치 안하니 accesstoken발급 하지않는다.(그래서 token값에 fail세팅)
        String token = jwtTokenProvider.createToken(claims.getSubject(),claims.get("role").toString());
         loginInfo.put("token",token);
         return loginInfo;
    }

//    3.정보 업데이트
    public Long update(UserUpdateDto dto, String loginId){
      User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
      String newPw = null;
      if(dto.getPassword() != null){
          newPw = passwordEncoder.encode(dto.getPassword());
      }
      user.updateUser(dto,newPw);
      return user.getId();
    }

//    4.내 정보 조회(마이페이지)
    public UserMyPageDto userMyPage(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
        return user.toMyPageDtoFromEntity(user.findNameFromDependentList(),user.findNameFromProtectorsList());
    }

//    5.내 피보호자 조회
    public List<UserLinkedUserDto> whoMyDependents(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()-> new EntityNotFoundException("없는 유저입니다"));
        List<CareRelation> dependents = user.getAsProtectors(); // 내 피보호자는 내가 보호자로 맺은 관계속에 있으니까
        return dependents.stream().map(c->c.getDependent().toLinkUserDtoFromEntity()).toList();

    }

//    6.내 보호자 조회
    public List<UserLinkedUserDto> whoMyProtectors(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()-> new EntityNotFoundException("없는 유저입니다"));
        List<CareRelation> protectors = user.getAsDependents(); //내 보호자는 내가 피보호자로 맺은 관계속에 있으니까
        return protectors.stream().map(c->c.getProtector().toLinkUserDtoFromEntity()).toList();

    }

    //    7.loginId로 userId조회하기
    public Long getUserIdByLoginId(String loginId){
        User user = userRepository.findByLoginId(loginId).orElseThrow(()->new EntityNotFoundException("없는 유저입니다."));
        return user.getId();
    }

    //    8.loginId로 userId와 nickname 조회하기
    public UserProfileInfoDto getUserProfileInfo(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new EntityNotFoundException("없는 유저입니다."));
        Long userId = user.getId();
        String nickname = user.getNickName();
        String profileImage = user.getName();//꼭 프로필이미지로 수정해야함!!!!!
        return UserProfileInfoDto.userProfileInfoDto(userId,nickname,profileImage);
    }
    //  9. 유저 목록 조회
    public Page<UserListDto> findAll(Pageable pageable){
        Page<User> userList = userRepository.findAll(pageable);
        return userList.map(u->u.ListDtoFromEntity());
    }

//    // 10.프로필 이미지 등록 및 수정
//    public String postProfileImage(String loginId,UserProfileImgDto dto){
//        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 유저입니다"));
//        MultipartFile image = dto.getImage();
//        String fileNmae = user.getLoginId() + "-" + "profile - " + image.getOriginalFilename();
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucket)
//                .key(fileNmae)
//                .build();
//        // 로컬 저장없이 바로 s3로. putObject는 s3에 객체를 업로드하는 메서드로 두가지 인자를 받음
//        //putObjectRequest는 업로할 파일의 정보. RequestBody는 업로드할 파일의 실제 내용이고 .fromInputStrem은 MultipartFile이 가진 파일 내용을
//        //직접 스트림으로 꺼내서 s3로 넘기는 방식으로 우리가 수업에서 배운 바이트 배열로 받는 형식보다 큰 파일을 다루는데 있어 더 효율적임. 그리고 추가로 image의 크기도 미리 알려줘야함
//        try{
//            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
//        } catch (IOException e){
//            e.printStackTrace();
//            System.out.println("s3 이미지 업로드 실패");
//        }
//        String s3Url = s3Client.utilities().
//    }
}
