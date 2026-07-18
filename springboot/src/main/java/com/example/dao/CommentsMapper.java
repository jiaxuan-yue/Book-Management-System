package com.example.dao;

import com.example.entity.Comments;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论数据访问接口（DAO） —— 操作 comments 表
 */
public interface CommentsMapper {

    void insert(Comments comments);

    List<Comments> selectAll(Comments comments);

    void updateById(Comments comments);

    @Delete("delete from `comments` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `comments` where id = #{id}")
    Comments selectById(Integer id);
}
