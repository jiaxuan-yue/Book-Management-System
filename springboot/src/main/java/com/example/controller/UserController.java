package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器 —— 提供普通用户 CRUD 相关的 RESTful 接口
 * <p>
 * 所有接口路径以 {@code /user} 为前缀，对应数据库 user 表。
 * <p>
 * 接口列表：
 * <ul>
 *   <li>POST   /user/add            → 新增用户</li>
 *   <li>DELETE /user/delete/{id}    → 根据 ID 删除用户</li>
 *   <li>PUT    /user/update         → 修改用户信息</li>
 *   <li>GET    /user/selectById/{id}→ 根据 ID 查询用户</li>
 *   <li>GET    /user/selectAll      → 查询所有用户（支持按名称模糊搜索）</li>
 *   <li>GET    /user/selectPage     → 分页查询用户列表</li>
 * </ul>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> page = userService.selectPage(user, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 充值
     */
    @PostMapping("/recharge")
    public Result recharge(@RequestBody User user) {
        User dbUser = userService.recharge(user);
        return Result.success(dbUser);
    }

}
