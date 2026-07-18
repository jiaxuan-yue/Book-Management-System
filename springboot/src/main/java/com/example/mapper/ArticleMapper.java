package com.example.mapper;

import com.example.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作article相关数据接口
*/
public interface ArticleMapper {

    /**
      * 新增
    */
    void insert(Article article);

    /**
      * 删除
    */
    @Delete("delete from `article` where id = #{id}")
    void deleteById(Integer id);

    /**
      * 修改
    */
    void updateById(Article article);

    /**
      * 根据ID查询
    */
    @Select("select * from `article` where id = #{id}")
    Article selectById(Integer id);

    /**
      * 查询所有
    */
    List<Article> selectAll(Article article);

}
