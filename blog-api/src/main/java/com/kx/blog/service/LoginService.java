package com.kx.blog.service;


import com.kx.blog.domain.Result;
import com.kx.blog.domain.entity.SysUser;
import com.kx.blog.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 20:00
 **/
@Transactional
public interface LoginService {
    /**
     * 登录
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 根据token检查用户信息
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
