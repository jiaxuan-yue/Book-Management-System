package com.example.entity;

/**
 * 借阅信息
*/
public class Borrow {

    /** ID */
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 图书ID */
    private Integer bookId;
    /** 时间 */
    private String time;
    /** 状态 */
    private String status;

    /** 用户名称（关联查询） */
    private String userName;
    /** 图书名称（关联查询） */
    private String bookName;

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

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
