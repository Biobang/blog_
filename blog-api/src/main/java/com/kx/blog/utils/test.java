package com.kx.blog.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 21:24
 **/
public class test {
    private static final String slat = "kx!@#";
    public static void main(String[] args) {
        String password = "admin";
        String s = DigestUtils.md5Hex(password + slat);
        System.out.println(s);
    }
}
