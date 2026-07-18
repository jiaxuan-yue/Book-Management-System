package com.example.dao;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 管理员数据访问接口（DAO） —— 操作 admin 表
 * <p>
 * 简单 SQL（deleteById、selectById、selectByUsername）通过注解直接定义；
 * 复杂 SQL（insert、updateById、selectAll）在 AdminMapper.xml 中定义。
 * <p>
 * MyBatis 会在应用启动时自动扫描此接口（通过启动类的 @MapperScan 配置），
 * 为其生成代理实现类并注册到 Spring 容器中，Service 层通过 @Resource 注入使用。
 */
public interface AdminMapper {

    /**
      * 新增
    */
    int insert(Admin admin);

    /**
      * 删除
    */
    @Delete("delete from admin where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Admin admin);

    /**
      * 根据ID查询
    */
    @Select("select * from admin where id = #{id}")
    Admin selectById(Integer id);

    /**
      * 查询所有
    */
    List<Admin> selectAll(Admin admin);

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);

}
