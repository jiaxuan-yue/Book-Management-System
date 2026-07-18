/**
 * 图书管理系统 - 后端启动模块
 *
 * 本包是整个 Spring Boot 应用的根包，包含以下子包：
 * - common:      公共工具类（统一返回结果封装、跨域配置等）
 * - controller:  控制器层（接收并处理前端 HTTP 请求，调用 Service 层）
 * - entity:      实体类（与数据库表一一对应的 Java 对象）
 * - exception:   自定义异常及全局异常处理器
 * - dao:         数据访问层 / DAO 层（MyBatis Mapper 接口，操作数据库）
 * - service:     业务逻辑层（封装核心业务逻辑，被 Controller 调用）
 *
 * 技术栈：Spring Boot 3.3.1 + MyBatis + PageHelper + Hutool + MySQL
 */
package com.example;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用启动类
 * <p>
 * {@code @SpringBootApplication} 组合注解，等价于同时标注：
 * - {@code @SpringBootConfiguration}：声明这是一个配置类
 * - {@code @EnableAutoConfiguration}：开启 Spring Boot 自动配置
 * - {@code @ComponentScan}：自动扫描当前包及子包下的组件（Controller、Service 等）
 * <p>
 * {@code @MapperScan("com.example.dao")}：
 * 扫描指定包下的 MyBatis Mapper 接口，无需在每个 Mapper 上标注 {@code @Mapper}
 */
@SpringBootApplication
@MapperScan("com.example.dao")
public class SpringbootApplication {

    /**
     * 应用入口方法
     * <p>
     * 调用 {@code SpringApplication.run()} 启动 Spring Boot 应用，
     * 完成 IoC 容器初始化、自动配置、内嵌 Tomcat 启动等工作。
     * 启动成功后默认监听 9090 端口（在 application.yml 中配置）。
     *
     * @param args 命令行启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
