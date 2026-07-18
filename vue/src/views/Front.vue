<!--
  Front.vue —— 前台展示布局组件（供普通用户浏览使用）

  页面布局结构：
  ┌────────────────────────────────────────────────────┐
  │  公告滚动栏（公告内容自动轮播切换）                    │
  ├────────────────────────────────────────────────────┤
  │  顶部导航栏（Logo + 标题 | 导航菜单 | 登录/用户头像）  │
  ├────────────────────────────────────────────────────┤
  │                                                    │
  │              内容区域                                │
  │       （通过 <router-view> 渲染子页面）               │
  │                                                    │
  └────────────────────────────────────────────────────┘

  顶部导航菜单项：
  - 首页（/front/home）
  - 图书中心（/front/book）
  - 交流论坛（/front/article）
  - 个人中心（/front/person）

  用户状态：
  - 未登录：显示"登录"和"注册"按钮
  - 已登录：显示用户头像和姓名，下拉菜单提供"退出登录"选项

  用户信息来源：从 localStorage 的 'xm-user' 中读取（与后台管理的 'system-user' 不同）
  公告功能：调用 /notice/selectAll 接口获取公告列表，每 2.5 秒自动切换显示下一条公告
-->
<template>
  <div>
    <!-- 公告滚动栏：循环展示公告内容 -->
    <div class="front-notice"><el-icon><Bell /></el-icon>公告：{{ data.top }}</div>
    <!-- 顶部导航栏 -->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">项目前台</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/book">图书中心</el-menu-item>
          <el-menu-item index="/front/article">交流论坛</el-menu-item>
          <el-menu-item index="/front/comments">我的评论</el-menu-item>
          <el-menu-item index="/front/orders">我的订单</el-menu-item>
          <el-menu-item index="/front/person">个人中心</el-menu-item>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer; height: 60px">
            <div style="display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px;">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script setup>
  import router from "@/router/index.js";
  import { reactive } from "vue";
  import request from "@/utils/request.js";

  const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    top: '',
    noticeData: []
  })

  const logout = () => {
    localStorage.removeItem('xm-user')
    router.push('/login')
  }

  const updateUser = () => {
    data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
  }

  const loadNotice = () => {
    request.get('/notice/selectAll').then(res => {
      data.noticeData = res.data
      let i = 0
      if (data.noticeData && data.noticeData.length) {
        data.top = data.noticeData[0].content
        setInterval(() => {
          data.top = data.noticeData[i].content
          i++
          if (i === data.noticeData.length) {
            i = 0
          }
        }, 2500)
      }
    })
  }
  loadNotice()
</script>

<style scoped>
@import "@/assets/css/front.css";
</style>