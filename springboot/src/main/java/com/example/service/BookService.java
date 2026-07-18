package com.example.service;

import com.example.entity.Book;
import com.example.dao.BookMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图书业务逻辑处理类
 * <p>
 * 负责图书信息的增删改查业务逻辑。
 * 逻辑相对简单，主要是直接调用 Mapper 执行数据库操作，
 * 没有复杂的业务校验。
 */
@Service
public class BookService {

    @Resource
    private BookMapper bookMapper;

    /**
     * 新增
     */
    public void add(Book book) {
        bookMapper.insert(book);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        bookMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Book book) {
        bookMapper.updateById(book);
    }

    /**
     * 根据ID查询
     */
    public Book selectById(Integer id) {
        return bookMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Book> selectAll(Book book) {
        return bookMapper.selectAll(book);
    }

    /**
     * 分页查询
     */
    public PageInfo<Book> selectPage(Book book, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Book> list = bookMapper.selectAll(book);
        return PageInfo.of(list);
    }

}
