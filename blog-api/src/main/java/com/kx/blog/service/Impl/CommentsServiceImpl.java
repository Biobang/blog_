package com.kx.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kx.blog.domain.Result;
import com.kx.blog.domain.entity.Comment;
import com.kx.blog.domain.entity.SysUser;
import com.kx.blog.mapper.CommentsMapper;
import com.kx.blog.service.CommentsService;
import com.kx.blog.service.SysUserService;
import com.kx.blog.utils.UserThreadLocal;
import com.kx.blog.vo.CommentVo;
import com.kx.blog.vo.UserVo;
import com.kx.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/8/1 15:21
 **/
@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SysUserService sysUserService;


    @Override
    public Result commentsByArticleId(Long articleId) {
        /**
         * 1. 根据文章id 查询 评论列表 从 comment 表中查询
         * 2. 根据作者的id 查询作者的信息
         * 3. 判断 如果 level = 1 要去查询它有没有子评论
         * 4. 如果有 根据评论id 进行查询 （parent_id）
         */

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //根据文章ID查询
        queryWrapper.eq(Comment::getArticleId, articleId);
        //根据层级查询
        queryWrapper.eq(Comment::getLevel, 1);
        List<Comment> commentList = commentsMapper.selectList(queryWrapper);

        List<CommentVo> commentVoList = copyList(commentList);
        return Result.success(commentVoList);


    }

    @Override
    public Result comment(CommentParam commentParam) {
        //拿到当前用户
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentsMapper.insert(comment);

        //使用redis自增评论数量

        CommentVo commentVo = copy(comment);
        return Result.success(commentVo);

    }

    private List<CommentVo> copyList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;


    }

    public CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserAoById(authorId);
        commentVo.setAuthor(userVo);
        //level信息
        Integer level = comment.getLevel();
        if (level == 1) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 给谁评论
        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserAoById(toUid);
            commentVo.setToUser(toUserVo);
        }


        return commentVo;

    }

    private List<CommentVo> findCommentsByParentId(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        List<Comment> commentList = commentsMapper.selectList(queryWrapper);
        return copyList(commentList);
    }
}
