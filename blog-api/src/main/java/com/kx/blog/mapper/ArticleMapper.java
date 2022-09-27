package com.kx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kx.blog.domain.dos.Archives;
import com.kx.blog.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 13:26
 **/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 每一个标签下都对应着该标签所对应的文章
     * @return
     */
    List<Archives> listArchives();

    /**
     * 归档文章列表
     * @param page
     * @param categoryId
     * @param tagId
     * @param year
     * @param month
     * @return
     */
    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

    /**
     * 更新（浏览量，评论数）
     * @param article
     * @return
     */
    int updateNumById(Article article);
}
