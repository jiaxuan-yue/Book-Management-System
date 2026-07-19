<!--
  Manager.vue —— 后台管理布局组件（管理员和用户共用）

  页面布局结构：
  ┌─────────────────────────────────────────────────┐
  │  顶部导航栏（Logo + 系统名称 | 用户头像 + 用户名）  │  ← 高度 60px
  ├──────────┬──────────────────────────────────────┤
  │          │                                      │
  │  侧边栏   │          内容区域                     │
  │  导航菜单  │     （通过 <router-view> 渲染子页面）  │
  │          │                                      │
  │          │                                      │
  └──────────┴──────────────────────────────────────┘

  侧边栏菜单项（根据角色动态显示）：
  - 系统首页（所有角色可见）
  - 用户管理 → 管理员信息、用户信息（仅 ADMIN 可见）
  - 图书信息（仅 ADMIN 可见）
  - 借阅信息（仅 ADMIN 可见）
  - 我的帖子（所有角色可见，但只显示自己发布的帖子）
  - 帖子信息（仅 ADMIN 可见，可管理所有帖子）
  - 个人资料（所有角色可见）
  - 修改密码（所有角色可见）
  - 退出系统（所有角色可见）

  用户信息来源：从 localStorage 的 'system-user' 中读取
  权限控制：通过 v-if="data.user.role === 'ADMIN'" 控制菜单的显示/隐藏
-->
<template>
  <div>
    <!-- 顶部导航栏：左侧 Logo + 系统名称，右侧用户头像 + 用户名 -->
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">武哥带小白2026</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
        <span style="margin-left: 5px">{{ data.user.name }}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['user']"
        >
          <el-menu-item index="/manager/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="user" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">
              <el-icon><User /></el-icon>
              <span>管理员信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/user">
              <el-icon><User /></el-icon>
              <span>用户信息</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/manager/book" v-if="data.user.role === 'ADMIN'">
            <el-icon><Reading /></el-icon>
            <span>图书信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/chapter" v-if="data.user.role === 'ADMIN'">
            <el-icon><List /></el-icon>
            <span>章节信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/bookContent" v-if="data.user.role === 'ADMIN'">
            <el-icon><DocumentCopy /></el-icon>
            <span>图书内容</span>
          </el-menu-item>
          <el-menu-item index="/manager/borrow" v-if="data.user.role === 'ADMIN'">
            <el-icon><Notebook /></el-icon>
            <span>借阅信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/article">
            <el-icon><Document /></el-icon>
            <span>我的帖子</span>
          </el-menu-item>
          <el-menu-item index="/front/article">
            <el-icon><ChatDotRound /></el-icon>
            <span>交流论坛</span>
          </el-menu-item>
          <el-menu-item index="/manager/articleAll" v-if="data.user.role === 'ADMIN'">
            <el-icon><Document /></el-icon>
            <span>帖子信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/collect" v-if="data.user.role === 'ADMIN'">
            <el-icon><Star /></el-icon>
            <span>收藏信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/comments" v-if="data.user.role === 'ADMIN'">
            <el-icon><ChatDotRound /></el-icon>
            <span>评论信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/orders" v-if="data.user.role === 'ADMIN'">
            <el-icon><ShoppingCart /></el-icon>
            <span>订单信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/person">
            <el-icon><User /></el-icon>
            <span>个人资料</span>
          </el-menu-item>
          <el-menu-item index="/manager/password">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
          <el-menu-item index="/login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view @updateUser="updateUser" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
})

if (!data.user?.id) {
  ElMessage.error('请登录！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('system-user') || '{}')
}

const logout = () => {
  ElMessage.success('退出成功')
  localStorage.removeItem('system-user')
  router.push('/login')
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #e0edfd !important;
}
.el-menu-item:hover {
  color: #1967e3;
}
:deep(th)  {
  color: #333;
}
</style>