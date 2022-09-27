package com.kx.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 17:51
 **/
@Data
public class HotArticleVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;
    private String title;
}
