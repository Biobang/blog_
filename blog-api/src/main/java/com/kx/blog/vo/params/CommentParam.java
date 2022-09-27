package com.kx.blog.vo.params;

import lombok.Data;

/**
 * @author M zg
 */
@Data
public class CommentParam {

    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}

