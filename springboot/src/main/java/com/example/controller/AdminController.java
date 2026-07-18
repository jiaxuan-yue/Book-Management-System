package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器 —— 提供管理员 CRUD 相关的 RESTful 接口
 * <p>
 * 所有接口路径以 {@code /admin} 为前缀，对应数据库 admin 表。
 * <p>
 * 接口列表：
 * <ul>
 *   <li>POST   /admin/add            → 新增管理员</li>
 *   <li>DELETE /admin/delete/{id}    → 根据 ID 删除管理员</li>
 *   <li>PUT    /admin/update         → 修改管理员信息</li>
 *   <li>GET    /admin/selectById/{id}→ 根据 ID 查询管理员</li>
 *   <li>GET    /admin/selectAll      → 查询所有管理员（支持按名称模糊搜索）</li>
 *   <li>GET    /admin/selectPage     → 分页查询管理员列表</li>
 * </ul>
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> page = adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(page);
    }

}