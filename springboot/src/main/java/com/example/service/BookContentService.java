package com.example.service;

import com.example.dao.BookContentMapper;
import com.example.entity.BookContent;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图书内容业务处理
 */
@Service
public class BookContentService {

    @Resource
    private BookContentMapper bookContentMapper;

    public void add(BookContent bookContent) {
        bookContentMapper.insert(bookContent);
    }

    public void deleteById(Integer id) {
        bookContentMapper.deleteById(id);
    }

    public void updateById(BookContent bookContent) {
        bookContentMapper.updateById(bookContent);
    }

    public BookContent selectById(Integer id) {
        return bookContentMapper.selectById(id);
    }

    public List<BookContent> selectAll(BookContent bookContent) {
        return bookContentMapper.selectAll(bookContent);
    }

    public PageInfo<BookContent> selectPage(BookContent bookContent, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BookContent> list = bookContentMapper.selectAll(bookContent);
        return PageInfo.of(list);
    }

}
