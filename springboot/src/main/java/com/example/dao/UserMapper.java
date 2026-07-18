package com.example.dao;

import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问接口（DAO） —— 操作 user 表
 * <p>
 * 与 AdminMapper 结构类似，额外提供了 selectByUsername 方法，
 * 用于登录验证和注册时的用户名重复检查。
 * 由于 user 是 MySQL 保留字，SQL 中使用反引号 {@code `user`} 包裹表名。
 */
public interface UserMapper {

    /**
      * 新增
    */
    int insert(User user);

    /**
      * 删除
    */
    @Delete("delete from `user` where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(User user);

    /**
      * 根据ID查询
    */
    @Select("select * from `user` where id = #{id}")
    User selectById(Integer id);

    /**
      * 查询所有
    */
    List<User> selectAll(User user);

    @Select("select * from `user` where username = #{username}")
    User selectByUsername(String username);

}
