package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.HtmlSanitizer;
import com.example.dao.ArticleMapper;
import com.example.entity.Article;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    private void sanitize(Article article) {
        if (article == null) {
            return;
        }
        article.setTitle(HtmlSanitizer.cleanText(article.getTitle()));
        article.setDescription(HtmlSanitizer.cleanText(article.getDescription()));
        article.setContent(HtmlSanitizer.cleanRich(article.getContent()));
    }

    public void add(Article article) {
        sanitize(article);
        article.setTime(DateUtil.now());
        article.setViews(0);
        articleMapper.insert(article);
    }

    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    public void updateById(Article article) {
        sanitize(article);
        articleMapper.updateById(article);
    }

    public Article selectById(Integer id) {
        Article article = articleMapper.selectById(id);
        sanitize(article);
        return article;
    }

    public List<Article> selectAll(Article article) {
        List<Article> list = articleMapper.selectAll(article);
        list.forEach(this::sanitize);
        return list;
    }

    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        list.forEach(this::sanitize);
        return PageInfo.of(list);
    }
}
