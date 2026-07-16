package com.example.controller;

import com.example.common.Result;
import com.example.entity.Borrow;
import com.example.service.BorrowService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 借阅前端操作接口
 **/
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Resource
    private BorrowService borrowService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Borrow borrow) {
        borrowService.add(borrow);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        borrowService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Borrow borrow) {
        borrowService.updateById(borrow);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Borrow borrow = borrowService.selectById(id);
        return Result.success(borrow);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Borrow borrow) {
        List<Borrow> list = borrowService.selectAll(borrow);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Borrow borrow,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Borrow> page = borrowService.selectPage(borrow, pageNum, pageSize);
        return Result.success(page);
    }

}
