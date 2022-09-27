package com.kx.blog.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kx.blog.domain.Result;
import com.kx.blog.domain.entity.Category;
import com.kx.blog.mapper.CategoryMapper;
import com.kx.blog.service.CategoryService;
import com.kx.blog.utils.BeanCopyUtils;
import com.kx.blog.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 根据ID查询文章分类（文章详情）
     * @param id
     * @return
     */
    @Override
    public CategoryVo findCategoryById(Long id){
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = new CategoryVo();
        //因为category,categoryVo属性一样所以可以使用 BeanUtils.copyProperties
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
    /**
     * 查询所有分类列表
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return Result.success(categoryVos);
    }
    /**
     * 导航-文章分类
     * @return
     */
    @Override
    public Result findAlLDetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return Result.success(categoryVos);
    }
    /**
     * 根据ID查询文章分类
     * @param id
     * @return
     */
    @Override
    public Result findAlLDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return Result.success(categoryVo);
    }

}

