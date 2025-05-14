package com.silverpotion.chatserver.chat.service;

import com.silverpotion.chatserver.chat.dto.UserDto;
import com.silverpotion.chatserver.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "silverpotion/user",configuration = FeignConfig.class)
public interface UserFeign {

    @GetMapping("/userId")
    Long getUserIdByLoginId(@RequestParam("loginId") String loginId);

    @GetMapping("/id")
    UserDto getUserById(@RequestParam("id") Long id);

    @GetMapping("/{userId}/nickname")
    String getNicknameByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/loginId")
    String getLoginIdByUserId(@RequestParam("id") Long id);

    @PostMapping("/videoCall/{loginId}")
    void sendVedioCallNotification(@PathVariable("loginId") String loginId);
}
