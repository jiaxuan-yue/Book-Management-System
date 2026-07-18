<!--
  manager/Password.vue —— 修改密码页面（后台管理端）

  功能说明：
  - 提供修改密码表单：原密码、新密码、确认新密码
  - 前端校验：新密码与确认密码必须一致，不一致则提示错误
  - 修改成功后自动跳转到登录页面，要求用户使用新密码重新登录
  - 同时将更新后的用户信息写入 localStorage

  修改流程：
  1. 用户输入原密码和新密码（两次确认）
  2. 前端校验两次新密码是否一致
  3. 调用 PUT /updatePassword 接口，后端验证原密码并更新
  4. 成功后跳转到登录页

  接口调用：PUT /updatePassword（统一接口，后端根据 role 分发）
-->
<template>
  <div style="width: 50%">
    <div class="card" style="padding: 30px">
      <el-form :model="data.user" label-width="100px" style="padding-right: 50px">
        <el-form-item label="原密码">
          <el-input v-model="data.user.password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="data.user.newPassword" show-password />
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input v-model="data.user.confirmPasword" show-password />
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
})

// 把当前修改的用户信息存储到后台数据库
const save = () => {
  if (data.user.newPassword !== data.user.confirmPasword) {
    ElMessage.error('确认新密码错误')
    return
  }
  request.put('/updatePassword', data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('修改密码成功')
      //把更新后的用户信息存储到缓存
      localStorage.setItem('system-user', JSON.stringify(data.user))
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>