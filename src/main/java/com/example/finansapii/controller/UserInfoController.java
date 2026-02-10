package com.example.finansapii.controller;

import com.example.finansapii.dto.UserInfoResponse;
import com.example.finansapii.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/userinfo")
    public UserInfoResponse userinfo(@RequestHeader("X-USER-ID") Long userId) {
        return userInfoService.getUserInfo(userId);
    }
}
