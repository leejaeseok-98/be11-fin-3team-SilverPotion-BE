package com.silverpotion.chatserver.chat.service;

import com.silverpotion.chatserver.chat.config.FeignTokenConfig;
import com.silverpotion.chatserver.chat.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignTokenConfig.class)
public interface UserFeign {
    @GetMapping(value = "/user/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping(value = "/user/{nickName}")
    UserDto getUserByNickName(@PathVariable String nickName);
}
