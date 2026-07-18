package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Article;
import com.example.mapper.ArticleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 帖子业务处理
 **/
@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 新增
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
