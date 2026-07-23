/**
 * 实体类包（entity）
 *
 * 存放与数据库表一一对应的 Java 实体类（POJO / JavaBean）。
 * 本项目的实体类继承关系如下：
 *
 * <pre>
 *   Account（账户基类 —— 抽取公共字段）
 *     ├── Admin（管理员，对应 admin 表）
 *     └── User （普通用户，对应 user 表）
 *
 *   Article（帖子，对应 article 表）
 *   Book   （图书，对应 book 表）
 *   Borrow （借阅记录，对应 borrow 表）
 * </pre>
 *
 * 所有实体类均使用传统的 getter/setter 方式封装字段，
 * MyBatis 会自动将数据库查询结果映射到对应的实体对象上。
 */
package com.example.entity;

/**
 * 账户基类 —— 抽取管理员和用户的公共字段
 * <p>
 * Admin 和 User 都继承此类，共享以下公共属性：
 * 用户名、密码、姓名、角色标识、头像、新密码（用于修改密码场景）。
 * <p>
 * 本类不直接映射数据库表，仅作为父类提供字段复用。
 */
public class Account {
    private Integer id;
    /** 用户名 */
    private String username;
    /** 名称 */
    private String name;
    /** 密码 */
    private String password;
    /** 角色标识 */
    private String role;
    /** 新密码 */
    private String newPassword;
    /** 头像 */
    private String avatar;
    /** JWT accessToken（非数据库字段，登录成功后返回） */
    private String token;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
