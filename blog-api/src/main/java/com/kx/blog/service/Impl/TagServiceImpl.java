package com.kx.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kx.blog.domain.Result;
import com.kx.blog.domain.entity.Tag;
import com.kx.blog.mapper.TagMapper;
import com.kx.blog.service.TagService;
import com.kx.blog.utils.BeanCopyUtils;
import com.kx.blog.vo.TagVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 14:50
 **/
@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;
    /**
     * 查询标签通过文章Id
     * @param id
     * @return
     */
    @Override
    public List<TagVo> findTagsByArticleId(Long id) {
        List<Tag> tags = tagMapper.findTagsByArticleId(id);
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(tags, TagVo.class);
        return tagVoList;
    }
    /**
     * 热门标签
     * @param limit
     * @return
     */
    @Override
    public List<TagVo> hot(int limit) {
        List<Long> hotTagIds = tagMapper.findHotTagIds(limit);
        if(CollectionUtils.isEmpty(hotTagIds)){
            return Collections.emptyList();
        }
        List<Tag> tagList = tagMapper.findHotTagByIds(hotTagIds);
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return tagVoList;


    }
    /**
     * 查询所有标签
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return Result.success(tagVos);
    }
    /**
     * 标签分类
     * @return
     */
    @Override
    public Result findAlLDetail() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return Result.success(tagVos);

    }
    /**
     * 根据标签ID查询文章列表
     * @param id
     * @return
     */
    @Override
    public Result findAlLDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return Result.success(tagVo);
    }

}
