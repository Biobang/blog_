package com.kx.blog.utils;

import com.kx.blog.domain.entity.SysUser;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/31 16:01
 **/
public class UserThreadLocal {
    private UserThreadLocal(){}

    /**
     * 线程变量隔离
     */
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static SysUser get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}
