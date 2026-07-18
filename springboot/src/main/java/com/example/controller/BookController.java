package com.example.controller;

import com.example.common.Result;
import com.example.entity.Book;
import com.example.service.BookService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书控制器 —— 提供图书 CRUD 相关的 RESTful 接口
 * <p>
 * 所有接口路径以 {@code /book} 为前缀，对应数据库 book 表。
 * <p>
 * 接口列表：
 * <ul>
 *   <li>POST   /book/add            → 新增图书</li>
 *   <li>DELETE /book/delete/{id}    → 根据 ID 删除图书</li>
 *   <li>PUT    /book/update         → 修改图书信息</li>
 *   <li>GET    /book/selectById/{id}→ 根据 ID 查询图书</li>
 *   <li>GET    /book/selectAll      → 查询所有图书（支持按书名模糊搜索）</li>
 *   <li>GET    /book/selectPage     → 分页查询图书列表</li>
 * </ul>
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Book book) {
        bookService.add(book);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        bookService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Book book) {
        bookService.updateById(book);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Book book = bookService.selectById(id);
        return Result.success(book);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Book book) {
        List<Book> list = bookService.selectAll(book);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Book book,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Book> page = bookService.selectPage(book, pageNum, pageSize);
        return Result.success(page);
    }

}
