package com.example.controller;

import com.example.common.Result;
import com.example.entity.BookContent;
import com.example.service.BookContentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书内容前端操作接口
 */
@RestController
@RequestMapping("/bookContent")
public class BookContentController {

    @Resource
    private BookContentService bookContentService;

    @PostMapping("/add")
    public Result add(@RequestBody BookContent bookContent) {
        bookContentService.add(bookContent);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        bookContentService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody BookContent bookContent) {
        bookContentService.updateById(bookContent);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        BookContent bookContent = bookContentService.selectById(id);
        return Result.success(bookContent);
    }

    @GetMapping("/selectAll")
    public Result selectAll(BookContent bookContent) {
        List<BookContent> list = bookContentService.selectAll(bookContent);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(BookContent bookContent,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<BookContent> page = bookContentService.selectPage(bookContent, pageNum, pageSize);
        return Result.success(page);
    }

}
