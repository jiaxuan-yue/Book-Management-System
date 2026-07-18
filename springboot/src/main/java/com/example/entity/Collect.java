package com.example.entity;

/**
 * 收藏信息
*/
public class Collect {

    /** ID */
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 关联ID（帖子ID） */
    private Integer relId;
    /** 时间 */
    private String time;

    /** 用户名称（关联查询） */
    private String userName;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRelName() {
        return relName;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }
}
