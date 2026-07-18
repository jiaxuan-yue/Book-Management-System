/**
 * Vue Router 路由配置文件
 *
 * 定义了应用的所有页面路由，采用 HTML5 History 模式（URL 中没有 # 号）。
 *
 * 路由结构分为三大模块：
 *
 * 1. 公共页面（无需登录）
 *    - /login     → 登录页面
 *    - /register  → 注册页面
 *
 * 2. 后台管理页面（/manager/*，供管理员和用户使用）
 *    - /manager/home        → 系统首页（数据统计面板）
 *    - /manager/admin       → 管理员信息管理（仅管理员可见）
 *    - /manager/user        → 用户信息管理（仅管理员可见）
 *    - /manager/book        → 图书信息管理（仅管理员可见）
 *    - /manager/borrow      → 借阅信息管理（仅管理员可见）
 *    - /manager/article     → 我的帖子（当前用户发布的帖子）
 *    - /manager/articleAll  → 全部帖子信息（仅管理员可见）
 *    - /manager/person      → 个人资料编辑
 *    - /manager/password    → 修改密码
 *
 * 3. 前台展示页面（/front/*，供普通用户浏览）
 *    - /front/home          → 前台首页
 *    - /front/book          → 图书中心（浏览图书列表）
 *    - /front/article       → 交流论坛（帖子列表）
 *    - /front/articleDetail → 帖子详情页
 *    - /front/person        → 个人中心
 *
 * 嵌套路由说明：
 * - /manager 和 /front 都使用了 children 嵌套路由
 * - 父组件（Manager.vue / Front.vue）中通过 <router-view> 渲染子页面
 * - 父组件提供统一的布局框架（侧边栏/顶部导航 + 内容区域）
 *
 * 所有路由组件都使用 () => import(...) 实现懒加载，
 * 只有访问到对应页面时才会加载组件代码，减少首屏加载时间。
 */
import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  // 使用 HTML5 History 模式（基于浏览器的 History API），URL 更美观
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // ---------- 根路径重定向到登录页 ----------
    { path: '/', redirect: '/login' },

    // ---------- 后台管理模块 ----------
    // Manager.vue 提供侧边栏导航 + 内容区域的布局框架
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/manager/home',  // 进入后台默认跳转到首页
      children: [
        { path: 'person', component: () => import('@/views/manager/Person.vue')},      // 个人资料
        { path: 'password', component: () => import('@/views/manager/Password.vue')},   // 修改密码
        { path: 'home', component: () => import('@/views/manager/Home.vue')},           // 系统首页
        { path: 'admin', component: () => import('@/views/manager/Admin.vue')},         // 管理员管理
        { path: 'user', component: () => import('@/views/manager/User.vue')},           // 用户管理
        { path: 'book', component: () => import('@/views/manager/Book.vue')},           // 图书管理
        { path: 'borrow', component: () => import('@/views/manager/Borrow.vue')},       // 借阅管理
        { path: 'article', component: () => import('@/views/manager/Article.vue')},     // 我的帖子
        { path: 'articleAll', component: () => import('@/views/manager/ArticleAll.vue')}, // 全部帖子
        { path: 'collect', component: () => import('@/views/manager/Collect.vue')},         // 收藏信息
        { path: 'comments', component: () => import('@/views/manager/Comments.vue')},       // 评论信息
        { path: 'orders', component: () => import('@/views/manager/Orders.vue')},           // 订单信息
        { path: 'chapter', component: () => import('@/views/manager/Chapter.vue')},         // 章节信息
        { path: 'bookContent', component: () => import('@/views/manager/BookContent.vue')}, // 图书内容
      ]
    },

    // ---------- 前台展示模块 ----------
    // Front.vue 提供顶部导航栏 + 内容区域的布局框架
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },           // 前台首页
        { path: 'person', component: () => import('@/views/front/Person.vue'),  },       // 个人中心
        { path: 'book', component: () => import('@/views/front/Book.vue'),  },           // 图书中心
        { path: 'article', component: () => import('@/views/front/Article.vue'),  },     // 交流论坛
        { path: 'articleDetail', component: () => import('@/views/front/ArticleDetail.vue'),  }, // 帖子详情
        { path: 'comments', component: () => import('@/views/front/Comments.vue'),  },   // 我的评论
        { path: 'orders', component: () => import('@/views/front/Orders.vue'),  },       // 我的订单
        { path: 'contentDetail', component: () => import('@/views/front/ContentDetail.vue'),  }, // 在线阅读
      ]
    },

    // ---------- 公共页面 ----------
    { path: '/login', component: () => import('@/views/Login.vue')},      // 登录页
    { path: '/register', component: () => import('@/views/Register.vue')}, // 注册页
  ]
})

export default router
