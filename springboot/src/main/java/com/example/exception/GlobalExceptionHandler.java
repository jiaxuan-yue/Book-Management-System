package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理器
 * <p>
 * {@code @ControllerAdvice(basePackages = "com.example.controller")}：
 * 声明这是一个 Controller 增强类，仅对 com.example.controller 包下的控制器生效。
 * 当 Controller 中的方法抛出异常时，会被本类中对应的 {@code @ExceptionHandler} 方法捕获处理。
 * <p>
 * 异常处理优先级（从具体到通用）：
 * 1. {@link CustomException} → 返回自定义的业务错误消息（如"用户不存在"）
 * 2. {@link Exception}       → 返回通用的"请求失败"，并在控制台打印异常堆栈
 * <p>
 * 这样做的好处：
 * - 前端不会收到 500 错误页面，而是统一的 JSON 格式错误响应
 * - 业务异常消息可以直接展示给用户，提升用户体验
 * - 未预期异常不会暴露系统内部信息，保证安全性
 */
@ControllerAdvice(basePackages = "com.example.controller")
public class GlobalExceptionHandler {

    /** 使用 Hutool 的日志工具，记录异常信息 */
    private static final Log log = LogFactory.get();


    /**
     * 处理所有未捕获的通用异常
     * <p>
     * 当 Controller 方法抛出 {@link Exception}（且不是 CustomException）时触发。
     * 会在控制台打印完整的异常堆栈信息，方便开发者排查问题，
     * 同时向前端返回通用的 "请求失败" 响应，避免暴露系统内部细节。
     *
     * @param request 当前 HTTP 请求对象（可用于获取请求 URL 等调试信息）
     * @param e       捕获到的异常对象
     * @return 统一的错误响应 Result（code="500", msg="请求失败"）
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody // 将返回值直接序列化为 JSON 写入响应体
    public Result error(HttpServletRequest request, Exception e) {
        log.error("异常信息：", e);
        return Result.error();
    }

    /**
     * 处理自定义业务异常
     * <p>
     * 当 Controller 方法（实际是 Service 层抛出、Controller 层传递上来的）
     * 抛出 {@link CustomException} 时触发。
     * 直接将业务错误消息返回给前端，前端可通过 ElMessage.error(msg) 展示给用户。
     *
     * @param request 当前 HTTP 请求对象
     * @param e       捕获到的自定义业务异常，包含错误消息 msg
     * @return 带自定义错误消息的响应 Result（code="500", msg=业务错误消息）
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody // 将返回值直接序列化为 JSON 写入响应体
    public Result customError(HttpServletRequest request, CustomException e) {
        return Result.error(e.getMsg());
    }

}
