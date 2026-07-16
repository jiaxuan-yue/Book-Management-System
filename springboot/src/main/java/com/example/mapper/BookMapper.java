package com.example.mapper;

import com.example.entity.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作book相关数据接口
*/
public interface BookMapper {

    /**
      * 新增
    */
    void insert(Book book);

    /**
      * 删除
    */
    @Delete("delete from `book` where id = #{id}")
    void deleteById(Integer id);

    /**
      * 修改
    */
    void updateById(Book book);

    /**
      * 根据ID查询
    */
    @Select("select * from `book` where id = #{id}")
    Book selectById(Integer id);

    /**
      * 查询所有
    */
    List<Book> selectAll(Book book);

}
