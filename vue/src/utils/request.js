/**
 * Axios 封装：自动携带 JWT accessToken；401 跳转登录
 */
import { ElMessage } from 'element-plus'
import router from '../router'
import axios from "axios";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000
})

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    const user = JSON.parse(localStorage.getItem('system-user') || localStorage.getItem('xm-user') || '{}')
    if (user.token) {
        config.headers['Authorization'] = 'Bearer ' + user.token
    }
    return config
}, error => {
    return Promise.reject(error)
});

request.interceptors.response.use(
    response => {
        let res = response.data;

        if (response.config.responseType === 'blob') {
            return res
        }

        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }

        if (res.code === '401') {
            ElMessage.error(res.msg || '请先登录');
            localStorage.removeItem('system-user')
            localStorage.removeItem('xm-user')
            router.push("/login")
        }

        return res;
    },
    error => {
        console.log('err' + error)
        return Promise.reject(error)
    }
)


export default request
