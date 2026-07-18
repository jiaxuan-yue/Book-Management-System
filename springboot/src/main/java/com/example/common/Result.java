/**
 * 公共工具包（common）
 *
 * 存放项目中通用的、与具体业务无关的工具类，包括：
 * - Result：统一接口返回结果封装，保证所有 API 返回格式一致
 * - CorsConfig：全局跨域配置，允许前端 Vue 项目跨域访问后端接口
 */
package com.example.common;

/**
 * 统一接口返回结果封装类
 * <p>
 * 所有 Controller 接口的返回值都通过此类进行包装，确保前后端交互的数据格式统一。
 * 前端通过判断 {@code code} 字段来确定请求是否成功：
 * <ul>
 *   <li>code = "200"：请求成功</li>
 *   <li>code = "500"：请求失败（服务器内部错误）</li>
 * </ul>
 * <p>
 * 返回数据结构示例（JSON）：
 * <pre>
 * {
 *   "code": "200",
 *   "msg": "请求成功",
 *   "data": { ... }   // 可选，携带具体的业务数据
 * }
 * </pre>
 * <p>
 * 使用方式：
 * <pre>
 *   return Result.success();              // 无数据的成功响应
 *   return Result.success(userList);      // 带数据的成功响应
 *   return Result.error("操作失败");       // 自定义错误消息
 * </pre>
 */
public class Result {
    /** 状态码："200" 表示成功，"500" 表示失败 */
    private String code;
    /** 提示信息，如 "请求成功"、"用户不存在" 等 */
    private String msg;
    /** 返回的业务数据，可以是对象、列表等任意类型 */
    private Object data;

    private Result(Object data) {
        this.data = data;
    }

    public Result() {
    }

    /**
     * 返回无数据的成功结果
     *
     * @return code="200", msg="请求成功"
     */
    public static Result success() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("请求成功");
        return result;
    }

    /**
     * 返回带数据的成功结果
     *
     * @param data 需要返回给前端的业务数据（如查询结果列表、单个实体对象等）
     * @return code="200", msg="请求成功", data=传入的数据
     */
    public static Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    /**
     * 返回默认的错误结果
     *
     * @return code="500", msg="请求失败"
     */
    public static Result error() {
        Result result = new Result();
        result.setCode("500");
        result.setMsg("请求失败");
        return result;
    }

    /**
     * 返回自定义错误消息的结果
     *
     * @param msg 自定义的错误提示信息，如 "用户不存在"、"原密码错误"
     * @return code="500", msg=传入的错误消息
     */
    public static Result error(String msg) {
        Result result = new Result();
        result.setCode("500");
        result.setMsg(msg);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
