package com.example.dao;

import com.example.entity.Borrow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 借阅记录数据访问接口（DAO） —— 操作 borrow 表
 * <p>
 * selectAll 方法在 XML 中使用了 LEFT JOIN user 表和 book 表，
 * 关联查询出借阅用户的姓名（userName）和借阅图书的名称（bookName），
 * 方便前端直接展示，无需额外的接口请求。
 */
public interface BorrowMapper {

    /**
      * 新增
    */
    void insert(Borrow borrow);

    /**
      * 删除
    */
    @Delete("delete from `borrow` where id = #{id}")
    void deleteById(Integer id);

    /**
      * 修改
    */
    void updateById(Borrow borrow);

    /**
      * 根据ID查询
    */
    @Select("select * from `borrow` where id = #{id}")
    Borrow selectById(Integer id);

    /**
      * 查询所有
    */
    List<Borrow> selectAll(Borrow borrow);

}
