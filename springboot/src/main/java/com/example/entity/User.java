package com.example.entity;

/**
 * 普通用户实体类 —— 对应数据库 user 表
 * <p>
 * 继承自 {@link Account} 基类，拥有普通用户的全部属性。
 * 与 Admin 类似，字段与父类重复定义并通过 {@code @Override} 重写 getter/setter，
 * 确保 MyBatis 能正确映射查询结果。
 * <p>
 * 用户角色标识固定为 "USER"，在 {@code UserService.add()} 中自动设置。
 * 普通用户可以浏览图书、申请借阅、发布帖子、管理个人资料等。
 */
public class User extends Account {

    /** ID */
    private Integer id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 头像 */
    private String avatar;
    /** 角色标识 */
    private String role;
    /** 钱包余额 */
    private Double account;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }
}
