package com.kx.blog.controller;

import com.kx.blog.domain.Result;
import com.kx.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/8/1 19:33
 **/
@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public Result listCategory(){
        return categoryService.findAll();
    }
    @GetMapping("/detail")
    public Result categoriesDetail(){
        return categoryService.findAlLDetail();
    }
    @GetMapping("/detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long id){
        return categoryService.findAlLDetailById(id);
    }


}
