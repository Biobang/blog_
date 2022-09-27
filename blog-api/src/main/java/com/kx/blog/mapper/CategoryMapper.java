package com.kx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kx.blog.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}

