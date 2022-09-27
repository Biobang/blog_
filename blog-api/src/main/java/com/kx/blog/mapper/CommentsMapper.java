package com.kx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kx.blog.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/8/1 15:22
 **/
@Repository
@Mapper
public interface CommentsMapper extends BaseMapper<Comment> {
}
