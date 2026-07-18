package com.example.controller;

import com.example.common.Result;
import com.example.entity.Chapter;
import com.example.service.ChapterService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 章节前端操作接口
 */
@RestController
@RequestMapping("/chapter")
public class ChapterController {

    @Resource
    private ChapterService chapterService;

    @PostMapping("/add")
    public Result add(@RequestBody Chapter chapter) {
        chapterService.add(chapter);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        chapterService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Chapter chapter) {
        chapterService.updateById(chapter);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Chapter chapter = chapterService.selectById(id);
        return Result.success(chapter);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Chapter chapter) {
        List<Chapter> list = chapterService.selectAll(chapter);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Chapter chapter,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Chapter> page = chapterService.selectPage(chapter, pageNum, pageSize);
        return Result.success(page);
    }

}
