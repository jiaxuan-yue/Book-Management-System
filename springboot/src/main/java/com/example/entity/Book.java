package com.example.entity;

/**
 * 图书信息
*/
public class Book {

    /** ID */
    private Integer id;
    /** 封面 */
    private String img;
    /** 名称 */
    private String name;
    /** 出版社 */
    private String publishing;
    /** 作者 */
    private String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishing() {
        return publishing;
    }

    public void setPublishing(String publishing) {
        this.publishing = publishing;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
