package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局跨域（CORS）配置类
 * <p>
 * 由于本项目采用前后端分离架构：
 * - 前端 Vue 运行在 localhost:5173（Vite 默认端口）
 * - 后端 Spring Boot 运行在 localhost:9090
 * 两者端口不同，浏览器会执行同源策略阻止跨域请求。
 * <p>
 * 本配置类通过注册 CorsFilter 过滤器，允许所有来源、所有请求头、所有 HTTP 方法
 * 跨域访问后端接口，从而解决前后端联调时的跨域问题。
 * <p>
 * {@code @Configuration}：声明这是一个 Spring 配置类，其中的 {@code @Bean} 方法
 * 会被 Spring 容器管理，在应用启动时自动生效。
 */
@Configuration
public class CorsConfig {

    /**
     * 注册跨域过滤器
     * <p>
     * 配置步骤：
     * 1. 创建 CORS 配置对象，设置允许的来源、请求头、请求方法
     * 2. 将配置注册到 URL 路径匹配源中（"/**" 匹配所有路径）
     * 3. 用配置源创建 CorsFilter 并交由 Spring 容器管理
     *
     * @return 配置好的跨域过滤器实例
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}