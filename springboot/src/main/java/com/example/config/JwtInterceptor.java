package com.example.config;

import cn.hutool.core.util.StrUtil;
import com.example.common.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT 拦截：除白名单外所有接口必须携带有效 accessToken
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        String token = null;
        if (StrUtil.isNotBlank(auth) && auth.startsWith("Bearer ")) {
            token = auth.substring(7).trim();
        }
        if (StrUtil.isBlank(token)) {
            token = request.getHeader("token");
        }
        if (StrUtil.isBlank(token)) {
            write401(response, "请先登录");
            return false;
        }
        try {
            Claims claims = jwtUtils.parseToken(token);
            request.setAttribute("userId", claims.get("id"));
            request.setAttribute("username", claims.get("username"));
            request.setAttribute("role", claims.get("role"));
            return true;
        } catch (Exception e) {
            write401(response, "登录已过期，请重新登录");
            return false;
        }
    }

    private void write401(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new HashMap<>();
        body.put("code", "401");
        body.put("msg", msg);
        body.put("data", null);
        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}
