package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.dao.BookMapper;
import com.example.dao.OrdersMapper;
import com.example.dao.UserMapper;
import com.example.entity.Book;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单业务处理（模拟支付：从用户钱包扣款）
 */
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 新增订单（校验库存与余额，扣款并扣库存）
     */
    public void add(Orders orders) {
        Book book = bookMapper.selectById(orders.getRelId());
        if (book == null) {
            throw new CustomException("图书不存在");
        }
        if (book.getPrice() == null) {
            throw new CustomException("图书价格未设置");
        }
        if (book.getNum() == null) {
            book.setNum(0);
        }

        orders.setOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        orders.setTime(DateUtil.now());
        orders.setPrice(orders.getNum() * book.getPrice());

        if (book.getNum() < orders.getNum()) {
            throw new CustomException("库存不够，请减少您购买的数量");
        }

        User user = userMapper.selectById(orders.getUserId());
        if (user == null) {
            throw new CustomException("用户不存在");
        }
        if (user.getAccount() == null) {
            user.setAccount(0.0);
        }
        if (user.getAccount() < orders.getPrice()) {
            throw new CustomException("用户余额不足，请到个人中心充值");
        }

        ordersMapper.insert(orders);

        user.setAccount(user.getAccount() - orders.getPrice());
        userMapper.updateById(user);

        book.setNum(book.getNum() - orders.getNum());
        bookMapper.updateById(book);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    public List<Orders> selectAll(Orders orders) {
        List<Orders> list = ordersMapper.selectAll(orders);
        for (Orders dbOrders : list) {
            Book book = bookMapper.selectById(dbOrders.getRelId());
            dbOrders.setBook(book);
        }
        return list;
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = selectAll(orders);
        return PageInfo.of(list);
    }

}
