package silverpotion.postserver.post.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.common.service.FeignConfig;
import silverpotion.postserver.post.dtos.UserListDto;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;

import java.util.List;

@FeignClient(name = "user-service", path = "silverpotion/user", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/userId")
    Long getUserIdByLoginId(@RequestParam("loginId") String loginId);

//    로그인 유저(본인) 프로필 조회
    @GetMapping("/postUserInfo")
    UserProfileInfoDto getUserProfileInfo(@RequestParam("loginId") String loginId);

//    게시물 작성자 프로필 조회
    @GetMapping("/writer/postUserInfo")
    UserProfileInfoDto getUserProfileInfo(@RequestParam("userId") Long userId);

//    유저id로 유저 프로필 리스트 가져오기
    @PostMapping("/profile/list")//post로 하는 이유는 1. url길이 제한(사용자가 많아지면 사용) 2. 보안(url노출) 3. 대량 데이터 전송 적합
    CommonDto getUsersByIds(@RequestBody List<Long> userIds);

    //    유저id로 유저 프로필 리스트 가져오기(set)
    @PostMapping("/post/profileInfo")
    CommonDto PostProfileInfo(@RequestBody List<Long> accessibleUserIds);

    // userId로 닉네임 조회
    @GetMapping("/{userId}/nickname")
    String getNicknameByUserId(@PathVariable("userId") Long userId);

    // userId로 loginId 조회
    @GetMapping("/loginId")
    String getLoginIdByUserId(@RequestParam("id") Long id);

    //userIds로 profileinfos 조회
    @PostMapping("/internal/users/info")
    CommonDto getUserInfos(@RequestBody List<Long> userIds);
}
