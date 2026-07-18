package com.example.entity;

/**
 * 章节信息
 */
public class Chapter {

    /** ID */
    private Integer id;
    /** 名称 */
    private String name;
    /** 图书ID */
    private Integer bookId;
    /** 图书名称（关联查询） */
    private String bookName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
