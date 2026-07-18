package com.example.entity;

/**
 * 管理员实体类 —— 对应数据库 admin 表
 * <p>
 * 继承自 {@link Account} 基类，拥有管理员的全部属性。
 * 虽然字段与父类 Account 重复定义（通过 {@code @Override} 重写 getter/setter），
 * 这样做的目的是让 MyBatis 能够直接对 Admin 对象进行属性映射，而不依赖父类字段。
 * <p>
 * 管理员角色标识固定为 "ADMIN"，在 {@code AdminService.add()} 中自动设置。
 * 管理员可以管理用户信息、图书信息、借阅信息、帖子信息等所有后台功能。
 */
public class Admin extends Account {

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
}