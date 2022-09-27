package com.kx.blog.service;

import com.kx.blog.domain.Result;
import com.kx.blog.domain.entity.Article;
import com.kx.blog.vo.ArticleVo;
import com.kx.blog.vo.params.ArticleParam;
import com.kx.blog.vo.params.PageParams;
import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 13:01
 **/

public interface ArticleService {
    /**
     * 主页文章列表
     * @param pageParams
     * @return
     */
    List<ArticleVo> listArticlesPage(PageParams pageParams);

    /**
     * 热门文章列表
     * @param limit
     * @return
     */
    Result listHotArticles(int limit);

    /**
     * 最新文章列表
     * @param limit
     * @return
     */
    Result listNewArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 写文章
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);

    /**
     * 搜索文章
     * @param search
     * @return
     */
    Result searchArticle(String search);


    //以下为优化添加查询
    /**
     * 更新（浏览量、评论数）
     * @param article
     * @return
     */
    Boolean updateNumById(Article article);
    /**
     * 获取文章详情
     * @param articleId
     * @return
     */
    Article getArticleById(Long articleId);

    /**
     * 获取所有文章详情
     * @return
     */
    List<Article> findArticleAll();


}
