<!--
  manager/Home.vue —— 后台管理系统首页
-->
<template>
  <div>
    <div class="card" style="line-height: 30px; margin-bottom: 10px">
      <div>欢迎您，{{ data.user.name }} 祝您今天过得开心！</div>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="font-weight: bold; margin-bottom: 15px">进入前台</div>
      <div style="display: flex; flex-wrap: wrap; gap: 12px">
        <el-button type="primary" @click="goFront('/front/home')">前台首页</el-button>
        <el-button type="success" @click="goFront('/front/book')">图书中心</el-button>
        <el-button type="warning" @click="goFront('/front/article')">交流论坛</el-button>
        <el-button type="info" @click="goFront('/front/orders')">我的订单</el-button>
        <el-button @click="goFront('/front/comments')">我的评论</el-button>
        <el-button @click="goFront('/front/person')">个人中心</el-button>
      </div>
      <div style="margin-top: 12px; color: #666; font-size: 13px">
        收藏、评论、购书、在线阅读等功能都在前台使用
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
})

const goFront = (path) => {
  window.open(path, '_blank')
}
</script>
