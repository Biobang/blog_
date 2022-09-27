package com.kx.blog.controller;

import com.kx.blog.domain.Result;
import com.kx.blog.service.TagService;
import com.kx.blog.vo.TagVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 16:23
 **/
@RestController
@RequestMapping("/tags")
public class HotTagsController {
    @Resource
    private TagService tagService;

    /**
     * 热门标签
     * @return
     */
    @GetMapping("/hot")
    public Result hotTagsArticles(){
        int limit= 6;
        List<TagVo> hot = tagService.hot(6);
        return Result.success(hot);
    }

    /**
     * 所有标签
     * @return
     */
    @GetMapping
    public Result listTags(){
        return tagService.findAll();
    }
    @GetMapping("/detail")
    public Result tagsDetail(){
        return tagService.findAlLDetail();
    }
    @GetMapping("/detail/{id}")
    public Result tagsDetailById(@PathVariable("id") Long id){
        return tagService.findAlLDetailById(id);
    }

}
