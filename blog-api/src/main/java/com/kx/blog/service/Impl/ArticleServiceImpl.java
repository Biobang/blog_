package com.kx.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kx.blog.domain.Result;
import com.kx.blog.domain.dos.Archives;
import com.kx.blog.domain.entity.ArticleBody;
import com.kx.blog.domain.entity.ArticleTag;
import com.kx.blog.domain.entity.SysUser;
import com.kx.blog.mapper.ArticleBodyMapper;
import com.kx.blog.mapper.ArticleMapper;
import com.kx.blog.domain.entity.Article;
import com.kx.blog.mapper.ArticleTagMapper;
import com.kx.blog.service.*;
import com.kx.blog.utils.BeanCopyUtils;
import com.kx.blog.utils.UserThreadLocal;
import com.kx.blog.vo.*;
import com.kx.blog.vo.params.ArticleParam;
import com.kx.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Biobang
 * @date: 2022/7/30 13:27
 **/
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ThreadService threadService;


    /**
     * 1、分页查询article数据库表
     */
    @Override
    public List listArticlesPage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<ArticleVo> articleVoList = copyList(articleIPage.getRecords(), true, true);
        return articleVoList;

    }

    private List<ArticleVo> copyList(List<Article> articles, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : articles) {
            articleVoList.add(copy(article, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }


    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag) {
            List<TagVo> tags = tagService.findTagsByArticleId(article.getId());
            articleVo.setTags(tags);
        }
        if (isAuthor) {
            SysUser user = sysUserService.findUserById(article.getAuthorId());
            UserVo userVo = new UserVo();
            userVo.setId(user.getId().toString());
            userVo.setAvatar(user.getAvatar());
            userVo.setNickname(user.getNickname());
            articleVo.setAuthor(userVo);

        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }

        return articleVo;

    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;

    }

    /**
     * 2、最热文章
     *
     * @param limit
     * @return
     */
    @Override
    public Result listHotArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return Result.success(hotArticleVos);

    }

    /**
     * 3、最新文章
     *
     * @param limit
     * @return
     */
    @Override
    public Result listNewArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<NewArticleVo> newArticleVos = BeanCopyUtils.copyBeanList(articles, NewArticleVo.class);
        return Result.success(newArticleVos);

    }

    /**
     * 4.文章归档
     *
     * @return
     */
    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    /**
     * 5.文章详情
     *
     * @param articleId
     * @return
     */
    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        //更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
        //线程池可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        //threadService.updateArticleViewCount(articleMapper, article);
        //使用redis incr自增

        return Result.success(articleVo);
    }

    /**
     * 写文章
     *
     * @param articleParam
     * @return
     */
    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        //获取当前用户
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();

        boolean isEdit = false;
        //判断是写文章还是编辑文章
        if (articleParam.getId()!=null){
            article = new Article();
            article.setId(articleParam.getId());
            article.setSummary(articleParam.getSummary());
            article.setTitle(articleParam.getTitle());
            article.setCategoryId(articleParam.getCategory().getId());
            isEdit = true;
        }else {
            article.setAuthorId(sysUser.getId());
            article.setCategoryId(articleParam.getCategory().getId());
            article.setCreateDate(System.currentTimeMillis());
            article.setTitle(articleParam.getTitle());
            article.setCommentCounts(0);
            article.setViewCounts(0);
            article.setWeight(Article.Article_Common);
            article.setSummary(articleParam.getSummary());
            article.setBodyId(-1L);
            //插入之后 会生成一个文章id（因为新建的文章没有文章id所以要insert一下)
            //使用baseMapper.insert返回自增主键id
            this.articleMapper.insert(article);
        }
            //tags
            List<TagVo> tags = articleParam.getTags();
            if (tags != null) {
                for (TagVo tag : tags) {
                    ArticleTag articleTag = new ArticleTag();
                    //官网解释："insert后主键会自动'set到实体的ID字段。所以只需要"getId()就好
                    //利用主键自增，mp的insert操作后id值会回到参数对象中
                    articleTag.setArticleId(article.getId());
                    articleTag.setTagId(tag.getId());
                    this.articleTagMapper.insert(articleTag);
                }
            }

            //body
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBodyMapper.insert(articleBody);
            //插入完之后再给一个id
            article.setBodyId(articleBody.getId());
            articleMapper.updateById(article);


            ArticleVo articleVo = new ArticleVo();
            articleVo.setId(article.getId());
            return Result.success(articleVo);

    }

    /**
     * 搜索功能
     * @param search
     * @return
     */
    @Override
    public Result searchArticle(String search) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.like(Article::getTitle,search);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<NewArticleVo> newArticleVos = BeanCopyUtils.copyBeanList(articles, NewArticleVo.class);
        return Result.success(newArticleVos);
    }



//以下为优化添加查询
    /**
     * 更新浏览量、评论数
     * @param article
     * @return
     */
    @Override
    public Boolean updateNumById(Article article) {
        return articleMapper.updateNumById(article)>0;
    }

    @Override
    public Article getArticleById(Long articleId) {
        return articleMapper.selectById(articleId);
    }

    @Override
    public List<Article> findArticleAll() {
        return articleMapper.selectList(null);
    }


}
