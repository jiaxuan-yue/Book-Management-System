package com.example.dao;

import com.example.entity.Chapter;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 章节数据访问接口（DAO） —— 操作 chapter 表
 */
public interface ChapterMapper {

    void insert(Chapter chapter);

    List<Chapter> selectAll(Chapter chapter);

    void updateById(Chapter chapter);

    @Delete("delete from `chapter` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `chapter` where id = #{id}")
    Chapter selectById(Integer id);
}
