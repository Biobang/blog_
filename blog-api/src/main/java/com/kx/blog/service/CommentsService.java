package com.kx.blog.service;

import com.kx.blog.domain.Result;
import com.kx.blog.vo.params.CommentParam;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/8/1 15:16
 **/
public interface CommentsService {
    /**
     * 根据文章id查询所有的评论列表
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);

    /**
     * 评论功能
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
