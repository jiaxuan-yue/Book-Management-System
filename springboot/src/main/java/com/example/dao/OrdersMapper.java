package com.example.dao;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单数据访问接口（DAO） —— 操作 orders 表
 */
public interface OrdersMapper {

    void insert(Orders orders);

    List<Orders> selectAll(Orders orders);

    void updateById(Orders orders);

    @Delete("delete from `orders` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `orders` where id = #{id}")
    Orders selectById(Integer id);
}
