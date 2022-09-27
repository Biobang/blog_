package com.kx.blog.vo.params;

import lombok.Data;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 20:03
 **/
@Data
public class LoginParam {
    /**
     * 登录校验
     * 用户名
     * 密码
     */
    private String account;
    private String password;
    private String nickname;
}
