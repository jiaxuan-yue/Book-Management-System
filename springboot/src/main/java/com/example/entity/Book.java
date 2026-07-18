package com.example.entity;

/**
 * 图书实体类 —— 对应数据库 book 表
 * <p>
 * 用于存储图书馆中的图书基本信息。
 * 管理员可以增删改查图书，普通用户可以在前台浏览图书列表。
 * <p>
 * 数据库字段：id, img（封面图片URL）, name（书名）, publishing（出版社）, author（作者）
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
    /** 价格 */
    private Double price;
    /** 库存 */
    private Integer num;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
