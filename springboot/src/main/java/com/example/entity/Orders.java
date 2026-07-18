package com.example.entity;

/**
 * 订单信息
 */
public class Orders {

    /** ID */
    private Integer id;
    /** 订单编号 */
    private String orderNo;
    /** 关联ID（图书ID） */
    private Integer relId;
    /** 数量 */
    private Integer num;
    /** 总价 */
    private Double price;
    /** 下单时间 */
    private String time;
    /** 状态 */
    private String status;
    /** 电话 */
    private String phone;
    /** 收货地址 */
    private String address;
    /** 用户ID */
    private Integer userId;

    /** 关联图书 */
    private Book book;
    /** 下单人名称（关联查询） */
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getRelId() {
        return relId;
    }

    public void setRelId(Integer relId) {
        this.relId = relId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
