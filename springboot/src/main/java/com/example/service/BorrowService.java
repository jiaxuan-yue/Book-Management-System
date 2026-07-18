package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Borrow;
import com.example.dao.BorrowMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 借阅记录业务逻辑处理类
 * <p>
 * 负责图书借阅记录的增删改查业务逻辑，是图书管理系统的核心业务之一。
 * <p>
 * 新增借阅记录时会自动设置借阅时间为当前时间。
 * 查询借阅记录时会通过 Mapper XML 的 LEFT JOIN 自动关联 user 表和 book 表，
 * 获取借阅用户姓名和图书名称。
 */
@Service
public class BorrowService {

    @Resource
    private BorrowMapper borrowMapper;

    /**
     * 新增借阅记录
     * <p>
     * 自动设置借阅时间为当前时间，然后插入数据库。
     *
     * @param borrow 前端传入的借阅信息（userId、bookId、status）
     */
    public void add(Borrow borrow) {
        borrow.setTime(DateUtil.now());
        borrowMapper.insert(borrow);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        borrowMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Borrow borrow) {
        borrowMapper.updateById(borrow);
    }

    /**
     * 根据ID查询
     */
    public Borrow selectById(Integer id) {
        return borrowMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Borrow> selectAll(Borrow borrow) {
        return borrowMapper.selectAll(borrow);
    }

    /**
     * 分页查询
     */
    public PageInfo<Borrow> selectPage(Borrow borrow, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Borrow> list = borrowMapper.selectAll(borrow);
        return PageInfo.of(list);
    }

}
