package com.example.dao;

import com.example.entity.Collect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 收藏数据访问接口（DAO） —— 操作 collect 表
 */
public interface CollectMapper {

    void insert(Collect collect);

    @Delete("delete from `collect` where id = #{id}")
    void deleteById(Integer id);

    void updateById(Collect collect);

    @Select("select * from `collect` where id = #{id}")
    Collect selectById(Integer id);

    List<Collect> selectAll(Collect collect);

}
