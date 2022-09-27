package com.kx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kx.blog.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 15:16
 **/
@Repository
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
