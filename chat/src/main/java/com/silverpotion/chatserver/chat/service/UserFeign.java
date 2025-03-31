package com.silverpotion.chatserver.chat.service;

import com.silverpotion.chatserver.chat.config.FeignTokenConfig;
import com.silverpotion.chatserver.chat.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignTokenConfig.class)
public interface UserFeign {
    @GetMapping("/user/id/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/user/nickname/{nickname}")
    UserDto getUserByNickname(@PathVariable("nickname") String nickname);
}
