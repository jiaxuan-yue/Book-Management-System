package com.example.controller;

import com.example.common.JwtUtils;
import com.example.common.Result;
import com.example.common.RsaUtils;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 * 登录 / 注册 / 改密 / 公钥（无需 accessToken）
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RsaUtils rsaUtils;

    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /** 前端获取 RSA 公钥，用于加密密码后再提交 */
    @GetMapping("/publicKey")
    public Result publicKey() {
        return Result.success(rsaUtils.getPublicKeyPem());
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        account.setPassword(rsaUtils.requireDecrypt(account.getPassword()));
        Account ac = null;
        if ("ADMIN".equals(account.getRole())) {
            ac = adminService.login(account);
        } else if ("USER".equals(account.getRole())) {
            ac = userService.login(account);
        }
        if (ac != null) {
            ac.setPassword(null);
            ac.setToken(jwtUtils.createToken(ac.getId(), ac.getUsername(), ac.getRole()));
        }
        return Result.success(ac);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        User user = new User();
        user.setUsername(account.getUsername());
        user.setPassword(rsaUtils.requireDecrypt(account.getPassword()));
        user.setName(account.getName());
        userService.add(user);
        return Result.success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        account.setPassword(rsaUtils.requireDecrypt(account.getPassword()));
        account.setNewPassword(rsaUtils.requireDecrypt(account.getNewPassword()));
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if ("USER".equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

}
