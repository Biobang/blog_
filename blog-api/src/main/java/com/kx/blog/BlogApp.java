package com.kx.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 11:03
 **/
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.kx.blog.mapper")
@EnableCaching
public class BlogApp {
    public static void main(String[] args) {
        SpringApplication.run(BlogApp.class,args);
    }

}
