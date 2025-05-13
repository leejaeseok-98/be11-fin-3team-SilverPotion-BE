package com.silverpotion.chatserver.chat.service;

import com.silverpotion.chatserver.chat.dto.UserDto;
import com.silverpotion.chatserver.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserFeign {

    @GetMapping("silverpotion/user/userId")
    Long getUserIdByLoginId(@RequestParam("loginId") String loginId);

    @GetMapping("silverpotion/user/id")
    UserDto getUserById(@RequestParam("id") Long id);

    @GetMapping("silverpotion/user/{userId}/nickname")
    String getNicknameByUserId(@PathVariable("userId") Long userId);

    @GetMapping("silverpotion/user/loginId")
    String getLoginIdByUserId(@RequestParam("id") Long id);

    @PostMapping("silverpotion/user/videoCall/{loginId}")
    void sendVedioCallNotification(@PathVariable("loginId") String loginId);
}
