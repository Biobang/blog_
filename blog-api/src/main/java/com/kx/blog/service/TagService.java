package com.kx.blog.service;

import com.kx.blog.domain.Result;
import com.kx.blog.vo.TagVo;

import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 14:51
 **/
public interface TagService {
    /**
     * 查询标签通过文章Id
     * @param id
     * @return
     */
    List<TagVo> findTagsByArticleId(Long id);

    /**
     * 热门标签
     * @param limit
     * @return
     */
    List<TagVo> hot(int limit);

    /**
     * 查询所有标签
     * @return
     */
    Result findAll();

    /**
     * 标签分类
     * @return
     */
    Result findAlLDetail();

    /**
     * 根据标签ID查询文章列表
     * @param id
     * @return
     */
    Result findAlLDetailById(Long id);
}
