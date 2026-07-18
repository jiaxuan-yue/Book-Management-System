package com.example.controller;

import com.example.common.Result;
import com.example.entity.Borrow;
import com.example.service.BorrowService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 借阅记录控制器 —— 提供借阅 CRUD 相关的 RESTful 接口
 * <p>
 * 所有接口路径以 {@code /borrow} 为前缀，对应数据库 borrow 表。
 * <p>
 * 接口列表：
 * <ul>
 *   <li>POST   /borrow/add            → 新增借阅记录（自动设置借阅时间）</li>
 *   <li>DELETE /borrow/delete/{id}    → 根据 ID 删除借阅记录</li>
 *   <li>PUT    /borrow/update         → 修改借阅信息（如更新借阅状态）</li>
 *   <li>GET    /borrow/selectById/{id}→ 根据 ID 查询借阅详情</li>
 *   <li>GET    /borrow/selectAll      → 查询所有借阅记录（支持按图书名称模糊搜索）</li>
 *   <li>GET    /borrow/selectPage     → 分页查询借阅记录列表</li>
 * </ul>
 */
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
