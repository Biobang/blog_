package com.kx.blog.common.aop;

import com.kx.blog.service.ArticleService;
import com.kx.blog.utils.RedisUtil;
import com.kx.blog.vo.params.CommentParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
/**
 * @Author biobang
 * @Create 2022-07-06 17:46
 */

@Aspect
@Component
public class MyAspect {
    @Autowired
    private ArticleService articleService;
 
    @Autowired
    private RedisUtil redisUtil;


    @Pointcut("execution(public * com.kx.blog.controller.ArticleController.findArticleById(..))")
    public void myPointCut_ViewNum() { }

    @Pointcut("execution(public * com.kx.blog.controller.CommentsController.comment(..))")
    public void myPointCut_CommentNum(){}

 

    @After("myPointCut_ViewNum()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {
        Object[] objs = joinPoint.getArgs();
        Long id = (Long) objs[0];
        //根据id更新浏览量
        redisUtil.zIncrementScore("viewNum", id.toString(), 1);
    }

    @After("myPointCut_CommentNum()")
    public void doAfter_commentNum(JoinPoint joinPoint){
        Object[] objs = joinPoint.getArgs();
        CommentParam commentParam = (CommentParam) objs[0];

        //根据id更新评论数
        redisUtil.zIncrementScore("commentNum",commentParam.getArticleId().toString(),1);
    }
}