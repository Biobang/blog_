package com.kx.blog.controller;

import com.kx.blog.common.aop.LogAnnotation;

import com.kx.blog.service.ArticleService;
import com.kx.blog.vo.ArticleVo;
import com.kx.blog.domain.Result;
import com.kx.blog.vo.params.ArticleParam;
import com.kx.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 12:55
 **/
@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    //主页文章列表
    @PostMapping
    /**
     * 加上此注解，代表要对此接口记录日志
     */
    @LogAnnotation(module = "文章", operation = "获取文章列表")
    public Result articles(@RequestBody PageParams pageParams) {
        //ArticleVo 页面接收的数据
        List<ArticleVo> articles = articleService.listArticlesPage(pageParams);

        return Result.success(articles);
    }

    /**
     * 热门文章列表
     * @return
     */
    @PostMapping("/hot")
    public Result hotArticles() {
        int limit = 5;
        return articleService.listHotArticles(limit);
    }

    /**
     * 最新文章列表
     * 缓存设置过期时间为1分钟，默认为一小时
     * @return
     */
    @PostMapping("/new")
    @Cacheable(value = {"newArticle"}, key = "#root.methodName",cacheManager = "cacheManager1Minute")
    public Result newArticles() {
        int limit = 3;
        return articleService.listNewArticles(limit);
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    /**
     * 写文章
     * @param articleParam
     * @return
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }

    /**
     * 搜索文章
     * @param articleParam
     * @return
     */
    @PostMapping("/search")
    public Result search(@RequestBody ArticleParam articleParam){
        String search = articleParam.getSearch();
        return articleService.searchArticle(search);
    }

    /**
     * 修改文章时，获取文章详情
     */
    @PostMapping("{id}")
    public Result articleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }
}
