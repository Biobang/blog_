package com.kx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kx.blog.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 13:49
 **/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> findTagsByArticleId(Long articleId);

    List<Long> findHotTagIds(int limit);

    List<Tag> findHotTagByIds(List<Long> hotTagIds);
}
