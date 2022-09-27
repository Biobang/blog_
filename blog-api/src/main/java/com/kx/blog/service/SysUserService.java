package com.kx.blog.service;

import com.kx.blog.domain.Result;
import com.kx.blog.domain.entity.SysUser;
import com.kx.blog.vo.UserVo;

public interface SysUserService {
    /**
     * 根据用户名，密码查找用户
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password) ;

    /**
     * 根据用户ID查找用户
     * @param userId
     * @return
     */


    SysUser findUserById(Long userId);

    /**
     * 根据token查找用户
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据用户名查找用户
     * 用于注册校验
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 用户注册时，保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 根据评论人ID去查询评论人信息
     * @param id
     * @return
     */
    UserVo findUserAoById(Long id);
}

