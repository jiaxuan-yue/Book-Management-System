<!--
  front/Home.vue —— 前台首页（浏览站入口）
-->
<template>
  <div>
    <div style="margin: 20px auto; width: 60%">
      <div class="card" style="padding: 24px; margin-bottom: 16px">
        <div style="font-size: 22px; font-weight: bold">欢迎来到图书管理系统</div>
        <div style="margin-top: 8px; color: #666">浏览图书、交流论坛、收藏评论、在线阅读</div>
      </div>
      <el-row :gutter="16">
        <el-col :span="8" v-for="item in menus" :key="item.path" style="margin-bottom: 16px">
          <div class="entry-card" @click="router.push(item.path)">
            <div style="font-size: 16px; font-weight: bold">{{ item.title }}</div>
            <div style="margin-top: 8px; color: #666; font-size: 13px">{{ item.desc }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import router from "@/router";

const menus = [
  { title: '图书中心', path: '/front/book', desc: '借阅、购买、在线阅读' },
  { title: '交流论坛', path: '/front/article', desc: '看帖、收藏、评论' },
  { title: '我的订单', path: '/front/orders', desc: '订单与收货' },
  { title: '我的评论', path: '/front/comments', desc: '评论管理' },
  { title: '个人中心', path: '/front/person', desc: '资料与充值' },
  { title: '返回系统首页', path: '/manager/home', desc: '回到登录后的主入口' },
]
</script>

<style scoped>
.entry-card {
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  padding: 18px 16px;
  cursor: pointer;
  background: #fff;
  min-height: 88px;
}
.entry-card:hover {
  border-color: #1967e3;
}
</style>
