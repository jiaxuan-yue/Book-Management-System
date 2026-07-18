/**
 * Axios HTTP 请求封装工具
 *
 * 对 axios 进行二次封装，提供以下功能：
 * 1. 统一配置 baseURL（从环境变量 VITE_BASE_URL 读取，区分开发和生产环境）
 * 2. 请求拦截器：统一设置 Content-Type 为 application/json
 * 3. 响应拦截器：统一处理响应数据、错误提示、权限验证
 *
 * 环境变量配置：
 * - 开发环境（.env.development）：VITE_BASE_URL = http://localhost:9090
 * - 生产环境（.env.production）：VITE_BASE_URL = 服务器实际地址
 *
 * 使用方式：
 *   import request from '@/utils/request'
 *   request.get('/user/selectAll').then(res => { ... })
 *   request.post('/login', data).then(res => { ... })
 */
import { ElMessage } from 'element-plus'
import router from '../router'
import axios from "axios";

// 创建 axios 实例，配置基础 URL 和超时时间
const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,  // 后端接口的基础 URL（从环境变量读取）
    timeout: 30000  // 请求超时时间：30秒，超时后自动报错
})

// ========== 请求拦截器 ==========
// 在每个请求发送之前执行，用于统一设置请求头等
request.interceptors.request.use(config => {
    // 统一设置请求头 Content-Type 为 JSON 格式
    // 后端 @RequestBody 注解要求请求体为 JSON 格式
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    return config
}, error => {
    return Promise.reject(error)
});

// ========== 响应拦截器 ==========
// 在每个响应返回之后执行，用于统一处理响应数据和错误
request.interceptors.response.use(
    response => {
        let res = response.data;

        // 如果响应类型是 blob（文件下载），直接返回原始数据
        if (response.config.responseType === 'blob') {
            return res
        }

        // 兼容处理：如果后端返回的是字符串格式的 JSON，自动解析为对象
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }

        // 权限验证：当返回 401 状态码时，提示用户并跳转到登录页
        if (res.code === '401') {
            ElMessage.error(res.msg);
            router.push("/login")
        }

        // 返回处理后的响应数据（此时 res 就是后端 Result 对象的 JSON）
        return res;
    },
    error => {
        // 网络错误、超时等请求级别的错误处理
        console.log('err' + error)
        return Promise.reject(error)
    }
)


export default request
