/**
 * Vue 应用入口文件
 *
 * 本项目是一个图书管理系统的前端项目，技术栈：
 * - Vue 3：前端框架（使用 Composition API + <script setup> 语法）
 * - Vue Router 4：路由管理（实现页面切换）
 * - Element Plus：UI 组件库（提供表格、表单、对话框、消息提示等组件）
 * - Axios：HTTP 请求库（封装在 utils/request.js 中）
 *
 * 项目结构说明：
 * - views/Manager.vue：后台管理布局（侧边栏 + 内容区），供管理员使用
 * - views/Front.vue：前台展示布局（顶部导航 + 内容区），供普通用户浏览
 * - views/Login.vue：登录页面
 * - views/Register.vue：注册页面
 * - views/manager/：后台管理子页面（管理员管理、用户管理、图书管理等）
 * - views/front/：前台展示子页面（首页、图书中心、交流论坛等）
 * - router/index.js：路由配置
 * - utils/request.js：Axios 请求封装（统一配置 baseURL、请求/响应拦截器）
 */
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'  // Element Plus 中文语言包
import * as ElementPlusIconsVue from '@element-plus/icons-vue'  // Element Plus 图标库

import '@/assets/css/global.css'  // 全局样式表

// 创建 Vue 应用实例
const app = createApp(App)

// 注册路由（实现页面切换）
app.use(router)
// 注册 Element Plus 组件库，并设置语言为中文
app.use(ElementPlus, {
    locale: zhCn,
})
// 将应用挂载到 index.html 中的 #app 元素上
app.mount('#app')

// 全局注册 Element Plus 图标组件
// 注册后可以在模板中直接使用 <el-icon><HomeFilled /></el-icon> 等图标，无需单独 import
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}