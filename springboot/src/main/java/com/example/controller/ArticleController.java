package com.example.controller;

import com.example.common.Result;
import com.example.entity.Article;
import com.example.service.ArticleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 帖子控制器 —— 提供帖子 CRUD 相关的 RESTful 接口
 * <p>
 * 所有接口路径以 {@code /article} 为前缀，对应数据库 article 表。
 * <p>
 * 接口列表：
 * <ul>
 *   <li>POST   /article/add            → 发布新帖子（自动设置发布时间和初始浏览量）</li>
 *   <li>DELETE /article/delete/{id}    → 根据 ID 删除帖子</li>
 *   <li>PUT    /article/update         → 修改帖子内容</li>
 *   <li>GET    /article/selectById/{id}→ 根据 ID 查询帖子详情</li>
 *   <li>GET    /article/selectAll      → 查询所有帖子（支持按标题模糊搜索、按用户ID筛选）</li>
 *   <li>GET    /article/selectPage     → 分页查询帖子列表</li>
 * </ul>
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        articleService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Article article) {
        articleService.updateById(article);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Article article = articleService.selectById(id);
        return Result.success(article);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Article article) {
        List<Article> list = articleService.selectAll(article);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Article article,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Article> page = articleService.selectPage(article, pageNum, pageSize);
        return Result.success(page);
    }

}
