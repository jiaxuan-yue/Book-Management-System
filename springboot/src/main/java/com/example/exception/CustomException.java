/**
 * 异常处理包（exception）
 *
 * 存放项目中的异常相关类：
 * - CustomException：自定义业务异常，用于表示可预期的业务错误（如"用户不存在"、"原密码错误"）
 * - GlobalExceptionHandler：全局异常处理器，统一捕获并处理所有 Controller 层抛出的异常
 *
 * 异常处理流程：
 * 1. Service 层检测到业务异常时，抛出 CustomException（携带错误消息）
 * 2. GlobalExceptionHandler 捕获 CustomException，将错误消息封装为 Result 返回前端
 * 3. 对于未预期的 Exception，GlobalExceptionHandler 捕获后返回通用的 "请求失败"
 */
package com.example.exception;

/**
 * 自定义业务异常类
 * <p>
 * 继承自 {@link RuntimeException}（运行时异常），不需要在方法签名中声明 throws。
 * 在 Service 层的业务逻辑中，当检测到不符合业务规则的情况时抛出此异常。
 * <p>
 * 抛出示例：
 * <pre>
 *   throw new CustomException("用户不存在");
 *   throw new CustomException("原密码错误");
 *   throw new CustomException("用户已存在");
 * </pre>
 * <p>
 * 该异常会被 {@link GlobalExceptionHandler} 全局捕获，
 * 自动将 msg 字段封装为 {@code Result.error(msg)} 返回给前端。
 */
public class CustomException extends RuntimeException {
    /** 异常错误消息，会直接展示给前端用户 */
    private String msg;

    public CustomException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
