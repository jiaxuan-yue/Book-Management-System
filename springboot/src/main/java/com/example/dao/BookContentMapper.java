package com.example.dao;

import com.example.entity.BookContent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 图书内容数据访问接口（DAO） —— 操作 book_content 表
 */
public interface BookContentMapper {

    void insert(BookContent bookContent);

    List<BookContent> selectAll(BookContent bookContent);

    void updateById(BookContent bookContent);

    @Delete("delete from `book_content` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `book_content` where id = #{id}")
    BookContent selectById(Integer id);
}
