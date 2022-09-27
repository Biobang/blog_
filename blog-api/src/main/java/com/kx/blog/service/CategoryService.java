package com.kx.blog.service;


import com.kx.blog.domain.Result;
import com.kx.blog.vo.CategoryVo;

public interface CategoryService {

    /**
     * 根据ID查询文章分类（文章详情）
     * @param id
     * @return
     */
    CategoryVo findCategoryById(Long id);

    /**
     * 查询所有分类列表
     * @return
     */
    Result findAll();

    /**
     * 导航-文章分类
     * @return
     */
    Result findAlLDetail();
    /**
     * 根据ID查询文章分类
     * @param id
     * @return
     */
    Result findAlLDetailById(Long id);
}

