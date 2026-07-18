package com.example.entity;

/**
 * 借阅记录实体类 —— 对应数据库 borrow 表
 * <p>
 * 用于记录用户的图书借阅信息，是图书管理系统的核心业务实体。
 * <p>
 * 数据库字段：id, user_id（借阅用户）, book_id（借阅图书）, time（借阅时间）, status（借阅状态）
 * <p>
 * 关联查询字段（非数据库字段，通过 LEFT JOIN 获得）：
 * - userName：借阅用户的姓名（JOIN user 表）
 * - bookName：借阅图书的名称（JOIN book 表）
 * <p>
 * 借阅流程：用户在前台选择图书 → 创建借阅记录 → 管理员在后台审核/管理借阅状态。
 * 新增借阅记录时，borrowTime 由 BorrowService 自动设置为当前时间。
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
