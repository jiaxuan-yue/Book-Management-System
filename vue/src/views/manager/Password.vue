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
import { encryptPassword } from "@/utils/crypto";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
})

const save = () => {
  if (data.user.newPassword !== data.user.confirmPasword) {
    ElMessage.error('确认新密码错误')
    return
  }
  Promise.all([
    encryptPassword(data.user.password),
    encryptPassword(data.user.newPassword)
  ]).then(([password, newPassword]) => {
    return request.put('/updatePassword', {
      username: data.user.username,
      role: data.user.role,
      password,
      newPassword
    })
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('修改密码成功')
      localStorage.removeItem('system-user')
      localStorage.removeItem('xm-user')
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(err => {
    ElMessage.error(err.message || '修改失败')
  })
}
</script>
