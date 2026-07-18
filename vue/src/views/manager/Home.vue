<!--
  manager/Home.vue —— 后台管理系统首页

  功能说明：
  - 显示欢迎信息，展示当前登录用户的姓名
  - 作为登录后台后的默认着陆页面

  用户信息来源：从 localStorage 的 'system-user' 中读取登录时保存的用户信息
-->
<template>
  <div>

    <!-- 欢迎卡片：展示当前登录用户的姓名和问候语 -->
    <div class="card" style="line-height: 30px">
      <div>欢迎您，{{ data.user.name }} 祝您今天过得开心！</div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request";

// 从 localStorage 读取当前登录用户的信息
const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
})
</script>