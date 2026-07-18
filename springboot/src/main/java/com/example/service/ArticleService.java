package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Article;
import com.example.dao.ArticleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 帖子业务逻辑处理类
 * <p>
 * 负责帖子的增删改查业务逻辑。
 * <p>
 * 发布帖子时的自动处理：
 * - 自动设置发布时间为当前时间（通过 Hutool 的 DateUtil.now()）
 * - 自动设置初始浏览量为 0
 * <p>
 * 帖子查询时会通过 Mapper XML 的 LEFT JOIN 自动关联用户表，
 * 获取发帖人的姓名和头像信息。
 */
@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 发布新帖子
     * <p>
     * 自动填充发布时间和初始浏览量，然后插入数据库。
     *
     * @param article 前端传入的帖子信息（title、img、description、content、userId）
     */
    public void add(Article article) {
        article.setTime(DateUtil.now());
        article.setViews(0);
        articleMapper.insert(article);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Article article) {
        articleMapper.updateById(article);
    }

    /**
     * 根据ID查询
     */
    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }

    /**
     * 分页查询
     */
    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

}
