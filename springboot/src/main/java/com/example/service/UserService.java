package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.dao.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务逻辑处理类
 * <p>
 * {@code @Service}：声明为 Spring 服务组件，可被 Controller 通过 @Resource 注入。
 * <p>
 * 主要职责：
 * - 用户的增删改查（带业务校验）
 * - 用户登录验证（账号密码校验）
 * - 用户修改密码（原密码校验）
 * <p>
 * 新增用户时的业务规则：
 * - 用户名不能与已存在的用户重复 → 重复则抛出 "用户已存在" 异常
 * - 如果未设置密码，默认密码为 "123456"
 * - 如果未设置姓名，默认使用用户名
 * - 角色标识自动设置为 "USER"
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 新增用户（也用于注册功能）
     * <p>
     * 业务逻辑：
     * 1. 根据用户名查询是否已存在同名用户 → 已存在则抛出异常
     * 2. 如果未传入密码，设置默认密码 "123456"
     * 3. 如果未传入姓名，使用用户名作为姓名
     * 4. 设置角色标识为 "USER"
     * 5. 调用 Mapper 插入数据库
     *
     * @param user 前端传入的用户信息（至少需要 username）
     * @throws CustomException 当用户名已存在时抛出 "用户已存在" 异常
     */
    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException("用户已存在");
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword("123456");
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole("USER");
        if (user.getAccount() == null) {
            user.setAccount(0.0);
        }
        userMapper.insert(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    /**
     * 根据ID查询
     */
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    /**
     * 分页查询
     */
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    /**
     * 用户登录验证
     * <p>
     * 业务逻辑：
     * 1. 根据用户名查询用户 → 不存在则抛出 "用户不存在" 异常
     * 2. 比对传入密码与数据库密码 → 不一致则抛出 "账号或密码错误" 异常
     * 3. 验证通过则返回用户完整信息
     *
     * @param account 前端传入的登录信息（username + password）
     * @return 验证通过的用户信息（包含 id、name、avatar 等）
     * @throws CustomException 当用户不存在或密码错误时抛出对应异常
     */
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        return dbUser;
    }

    /**
     * 用户修改密码
     * <p>
     * 业务逻辑：
     * 1. 根据用户名查询用户 → 不存在则抛出异常
     * 2. 验证原密码 → 错误则抛出 "原密码错误" 异常
     * 3. 将密码更新为新密码，调用 Mapper 写入数据库
     *
     * @param account 包含 username、password（原密码）、newPassword（新密码）
     * @throws CustomException 当用户不存在或原密码错误时抛出对应异常
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException("原密码错误");
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

    /**
     * 钱包充值（模拟支付）
     */
    public User recharge(User user) {
        Double account = user.getAccount();
        User dbUser = userMapper.selectById(user.getId());
        if (dbUser.getAccount() == null) {
            dbUser.setAccount(0.0);
        }
        dbUser.setAccount(dbUser.getAccount() + account);
        userMapper.updateById(dbUser);
        return dbUser;
    }

}
