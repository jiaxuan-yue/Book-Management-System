package com.example.entity;

/**
 * 图书内容（阅读分页片段）
 */
public class BookContent {

    /** ID */
    private Integer id;
    /** 内容 */
    private String content;
    /** 图书ID */
    private Integer bookId;
    /** 章节ID */
    private Integer chapterId;
    /** 图书名称（关联查询） */
    private String bookName;
    /** 章节名称（关联查询） */
    private String chapterName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}
