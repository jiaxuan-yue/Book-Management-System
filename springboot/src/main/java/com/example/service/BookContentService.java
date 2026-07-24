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
        bookContent.setContent(com.example.common.HtmlSanitizer.cleanRich(bookContent.getContent()));
        bookContentMapper.insert(bookContent);
    }

    public void deleteById(Integer id) {
        bookContentMapper.deleteById(id);
    }

    public void updateById(BookContent bookContent) {
        bookContent.setContent(com.example.common.HtmlSanitizer.cleanRich(bookContent.getContent()));
        bookContentMapper.updateById(bookContent);
    }

    public BookContent selectById(Integer id) {
        BookContent content = bookContentMapper.selectById(id);
        if (content != null) {
            content.setContent(com.example.common.HtmlSanitizer.cleanRich(content.getContent()));
        }
        return content;
    }

    public List<BookContent> selectAll(BookContent bookContent) {
        List<BookContent> list = bookContentMapper.selectAll(bookContent);
        list.forEach(item -> item.setContent(com.example.common.HtmlSanitizer.cleanRich(item.getContent())));
        return list;
    }

    public PageInfo<BookContent> selectPage(BookContent bookContent, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BookContent> list = bookContentMapper.selectAll(bookContent);
        return PageInfo.of(list);
    }

}
