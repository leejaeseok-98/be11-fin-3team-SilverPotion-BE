package silverpotion.postserver.post.UserClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("silverpotion/user/userId")
    Long getUserIdByLoginId(@RequestParam("loginId") String loginId);

    @GetMapping("silverpotion/user/postUserInfo")
    UserProfileInfoDto getUserProfileInfo(@RequestParam("loginId") String loginId);


}
