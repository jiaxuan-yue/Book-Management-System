package com.example.dao;

import com.example.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 帖子数据访问接口（DAO） —— 操作 article 表
 * <p>
 * 注意 selectAll 方法在 XML 中使用了 LEFT JOIN user 表，
 * 关联查询出帖子作者的姓名（userName）和头像（userAvatar），
 * 因此返回的 Article 对象中会包含这两个关联字段的值。
 * <p>
 * 由于 article 是 MySQL 保留字，SQL 中使用反引号 {@code `article`} 包裹表名。
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
