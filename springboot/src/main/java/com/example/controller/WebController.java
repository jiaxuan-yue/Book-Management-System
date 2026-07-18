package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 * 通用 Web 控制器 —— 提供登录、注册、修改密码等与具体模块无关的公共接口
 * <p>
 * 与其他的 Controller 不同，本类没有 {@code @RequestMapping} 前缀，
 * 因为它的接口路径是全局的（如 /login、/register）。
 * <p>
 * 接口列表：
 * <ul>
 *   <li>GET  /               → 默认请求接口（健康检查）</li>
 *   <li>POST /login          → 用户登录（根据角色分发到 Admin 或 User 的登录逻辑）</li>
 *   <li>POST /register       → 用户注册（固定注册为普通用户角色）</li>
 *   <li>PUT  /updatePassword → 修改密码（根据角色分发到对应的修改密码逻辑）</li>
 * </ul>
 * <p>
 * 登录流程：
 * 1. 前端传入 username + password + role
 * 2. 后端根据 role 判断是管理员还是普通用户
 * 3. 调用对应 Service 的 login 方法验证账号密码
 * 4. 验证通过则返回用户信息，前端存入 localStorage 并跳转页面
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 用户登录接口
     * <p>
     * 根据传入的角色标识（role）分发到不同的登录逻辑：
     * - role = "ADMIN"：调用 AdminService.login() 验证管理员账号
     * - role = "USER" ：调用 UserService.login() 验证普通用户账号
     * <p>
     * 验证通过后返回完整的用户信息（包含 id、name、avatar 等），
     * 前端将用户信息存入 localStorage，后续页面据此展示用户名、头像、控制菜单权限等。
     *
     * @param account 前端传入的登录信息（包含 username、password、role）
     * @return 登录成功返回用户信息，失败抛出 CustomException（由全局异常处理器返回错误消息）
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = null;
        if ("ADMIN".equals(account.getRole())) {
            ac = adminService.login(account);
        } else if ("USER".equals(account.getRole())) {
            ac = userService.login(account);
        }
        return Result.success(ac);
    }

    /**
     * 用户注册接口
     * <p>
     * 前端注册页面只允许注册普通用户（role 固定为 "USER"）。
     * 接收用户名、密码、姓名等信息，封装为 User 对象后调用 UserService.add() 完成注册。
     * <p>
     * 如果用户名已存在，UserService 会抛出 "用户已存在" 的业务异常。
     *
     * @param account 前端传入的注册信息（包含 username、password、name）
     * @return 注册成功返回无数据的成功响应
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        User user = new User();
        user.setUsername(account.getUsername());
        user.setPassword(account.getPassword());
        user.setName(account.getName());
        userService.add(user);
        return Result.success();
    }

    /**
     * 修改密码接口
     * <p>
     * 根据角色标识分发到对应的修改密码逻辑：
     * - role = "ADMIN"：调用 AdminService.updatePassword()
     * - role = "USER" ：调用 UserService.updatePassword()
     * <p>
     * 修改流程：验证原密码 → 原密码正确则更新为新密码 → 原密码错误则抛出异常。
     *
     * @param account 前端传入的修改信息（包含 username、password（原密码）、newPassword（新密码）、role）
     * @return 修改成功返回无数据的成功响应，原密码错误则返回错误提示
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if ("USER".equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

}
