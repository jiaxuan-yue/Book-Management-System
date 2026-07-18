package com.example.entity;

import java.util.List;

/**
 * 评论信息
 */
public class Comments {

    /** ID */
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 关联ID（帖子ID） */
    private Integer relId;
    /** 内容 */
    private String content;
    /** 时间 */
    private String time;
    /** 父级ID（0 表示顶级评论） */
    private Integer fid;
    /** 子评论列表 */
    private List<Comments> children;

    /** 用户名称（关联查询） */
    private String userName;
    /** 用户头像（关联查询） */
    private String userAvatar;
    /** 帖子标题（关联查询） */
    private String relName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRelId() {
        return relId;
    }

    public void setRelId(Integer relId) {
        this.relId = relId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public List<Comments> getChildren() {
        return children;
    }

    public void setChildren(List<Comments> children) {
        this.children = children;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getRelName() {
        return relName;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }
}
