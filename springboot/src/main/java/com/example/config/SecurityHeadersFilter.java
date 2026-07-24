package com.example.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 基础 CSP，降低 XSS 执行面（仍保留必要的内联以兼容现有页面）
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse http) {
            http.setHeader("X-Content-Type-Options", "nosniff");
            http.setHeader("X-Frame-Options", "SAMEORIGIN");
            http.setHeader("Referrer-Policy", "no-referrer");
            // 禁止页面被嵌入到外域，并限制 object；富文本已服务端消毒
            http.setHeader("Content-Security-Policy",
                    "default-src 'self'; img-src 'self' data: https: http:; " +
                    "style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline'; " +
                    "connect-src 'self' https: http:; frame-ancestors 'self'");
        }
        chain.doFilter(request, response);
    }
}
