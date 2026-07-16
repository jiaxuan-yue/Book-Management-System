package com.example.mapper;

import com.example.entity.Borrow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作borrow相关数据接口
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
