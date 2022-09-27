package com.kx.blog.controller;

import com.kx.blog.domain.Result;
import com.kx.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:退出登录
 * @author: Biobang
 * @date: 2022/7/31 10:48
 **/
@RestController
@RequestMapping("/logout")
public class LogoutController {
    @Autowired
    private LoginService loginService;
    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);

    }

}
