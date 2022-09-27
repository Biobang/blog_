package com.kx.blog.controller;

import com.kx.blog.domain.Result;
import com.kx.blog.service.LoginService;
import com.kx.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:用户注册
 * @author: Biobang
 * @date: 2022/7/31 11:07
 **/
@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public Result register(@RequestBody LoginParam loginParam){
        return loginService.register(loginParam);

    }

}
