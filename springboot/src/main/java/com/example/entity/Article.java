package com.example.entity;

/**
 * 帖子实体类 —— 对应数据库 article 表
 * <p>
 * 用于存储用户发布的帖子/文章信息，支持交流论坛功能。
 * <p>
 * 数据库字段：id, title, img, description, time, user_id, content, views
 * <p>
 * 关联查询字段（非数据库字段，通过 LEFT JOIN user 表获得）：
 * - userName：发帖用户的姓名
 * - userAvatar：发帖用户的头像
 * <p>
 * 在 Mapper XML 中通过 LEFT JOIN 实现关联查询，
 * 一次性获取帖子信息和发帖人的名称、头像，减少前端多次请求。
 */
public class Article {

    /** ID */
    private Integer id;
    /** 标题 */
    private String title;
    /** 封面 */
    private String img;
    /** 简介 */
    private String description;
    /** 时间 */
    private String time;
    /** 用户ID */
    private Integer userId;
    /** 内容 */
    private String content;
    /** 浏览量 */
    private Integer views;

    /** 用户头像（关联查询） */
    private String userAvatar;
    /** 用户名称（关联查询） */
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
