package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.dao.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员业务逻辑处理类
 * <p>
 * {@code @Service}：声明为 Spring 服务组件，可被 Controller 通过 @Resource 注入。
 * <p>
 * 主要职责：
 * - 管理员的增删改查（带业务校验）
 * - 管理员登录验证（账号密码校验）
 * - 管理员修改密码（原密码校验）
 * <p>
 * 新增管理员时的业务规则：
 * - 用户名不能与已存在的管理员重复
 * - 如果未设置密码，默认密码为 "admin"
 * - 如果未设置姓名，默认使用用户名
 * - 角色标识自动设置为 "ADMIN"
 */
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 新增管理员
     * <p>
     * 业务逻辑：
     * 1. 根据用户名查询是否已存在同名管理员 → 已存在则抛出异常
     * 2. 如果未传入密码，设置默认密码 "admin"
     * 3. 如果未传入姓名，使用用户名作为姓名
     * 4. 设置角色标识为 "ADMIN"
     * 5. 调用 Mapper 插入数据库
     *
     * @param admin 前端传入的管理员信息（至少需要 username）
     * @throws CustomException 当用户名已存在时抛出 "用户不存在" 异常
     */
    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (ObjectUtil.isNotNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword("admin");
        }
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Admin admin) {
        adminMapper.updateById(admin);
    }

    /**
     * 根据ID查询
     */
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    /**
     * 分页查询管理员列表
     * <p>
     * 使用 PageHelper 插件实现分页，无需手动编写 LIMIT 语句。
     * 调用 {@code PageHelper.startPage()} 后，紧接着的查询会自动追加分页条件。
     * 查询结果会被包装为 {@code PageInfo} 对象，包含总记录数、总页数、当前页数据等信息。
     *
     * @param admin    查询条件（如按名称模糊搜索）
     * @param pageNum  当前页码（从 1 开始）
     * @param pageSize 每页显示的记录数
     * @return 包含分页信息和当前页数据的 PageInfo 对象
     */
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    /**
     * 管理员登录验证
     * <p>
     * 业务逻辑：
     * 1. 根据用户名查询管理员 → 不存在则抛出 "用户不存在" 异常
     * 2. 比对传入密码与数据库密码 → 不一致则抛出 "账号或密码错误" 异常
     * 3. 验证通过则返回管理员信息
     *
     * @param account 前端传入的登录信息（username + password）
     * @return 验证通过的管理员信息（包含 id、name、avatar 等）
     * @throws CustomException 当用户不存在或密码错误时抛出对应异常
     */
    public Account login(Account account) {
        Account dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        return dbAdmin;
    }

    /**
     * 管理员修改密码
     * <p>
     * 业务逻辑：
     * 1. 根据用户名查询管理员 → 不存在则抛出异常
     * 2. 验证原密码 → 错误则抛出 "原密码错误" 异常
     * 3. 将密码更新为新密码，调用 Mapper 写入数据库
     *
     * @param account 包含 username、password（原密码）、newPassword（新密码）
     * @throws CustomException 当用户不存在或原密码错误时抛出对应异常
     */
    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException("原密码错误");
        }
        dbAdmin.setPassword(account.getNewPassword());
        adminMapper.updateById(dbAdmin);
    }

}