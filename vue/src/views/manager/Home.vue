<!--
  manager/Home.vue —— 系统首页（项目登录后的主入口）
-->
<template>
  <div>
    <div class="card" style="line-height: 30px; margin-bottom: 10px">
      <div style="font-size: 18px; font-weight: bold">欢迎您，{{ data.user.name }}</div>
      <div style="color: #666; margin-top: 6px">这里是系统首页，可从下方进入论坛、图书等功能</div>
    </div>

    <div class="card">
      <div style="font-weight: bold; margin-bottom: 15px">功能入口</div>
      <el-row :gutter="16">
        <el-col :span="8" v-for="item in data.menus" :key="item.path" style="margin-bottom: 16px">
          <div class="entry-card" @click="go(item.path)">
            <div style="font-size: 16px; font-weight: bold">{{ item.title }}</div>
            <div style="margin-top: 8px; color: #666; font-size: 13px">{{ item.desc }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  menus: [
    { title: '交流论坛', path: '/front/article', desc: '看全部帖子，可收藏、评论' },
    { title: '图书中心', path: '/front/book', desc: '借阅、购买、在线阅读' },
    { title: '我的帖子', path: '/manager/article', desc: '发布和管理自己的帖子' },
    { title: '我的订单', path: '/front/orders', desc: '查看购书订单、确认收货' },
    { title: '我的评论', path: '/front/comments', desc: '管理自己发表的评论' },
    { title: '个人资料', path: '/manager/person', desc: '资料修改、钱包充值' },
  ]
})

const go = (path) => {
  router.push(path)
}
</script>

<style scoped>
.entry-card {
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  padding: 18px 16px;
  cursor: pointer;
  background: #fff;
  min-height: 88px;
  transition: border-color .2s, box-shadow .2s;
}
.entry-card:hover {
  border-color: #1967e3;
  box-shadow: 0 2px 8px rgba(25, 103, 227, .12);
}
</style>
