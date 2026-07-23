<!--
  Register.vue —— 注册页面组件

  功能说明：
  - 提供用户注册表单，包含账号、密码、确认密码三个字段
  - 角色固定为"USER"（普通用户），注册页面不允许选择角色
  - 表单验证：
    - 账号和密码为必填项
    - 确认密码通过自定义验证器 validatePass 校验，确保两次输入一致
  - 注册成功后跳转到登录页面
  - 页面底部提供"登录"链接，引导已注册的用户前往登录

  注册流程：
  1. 用户填写账号、密码，并再次确认密码
  2. 点击"注册"按钮，触发表单验证
  3. 验证通过后调用 POST /register 接口
  4. 后端将用户信息写入数据库（默认密码 123456、角色 USER）
  5. 注册成功后跳转到登录页

  接口调用：POST /register
-->
<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 30px; text-align: center; margin-bottom: 30px; color: #1967e3">欢 迎 注 册</div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="请确认密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="register">注 册</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: right;">
        还没有账号？请 <a href="/login">登录</a>
      </div>
    </div>

  </div>
</template>

<script setup>
  import { reactive, ref } from "vue";
  import { User, Lock } from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import { encryptPassword } from "@/utils/crypto";
  import {ElMessage} from "element-plus";
  import router from "@/router";

  const validatePass = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请确认密码'))
    } else if (value !== data.form.password) {
      callback(new Error('两次输入密码不一致'))
    } else {
      callback()
    }
  }

  const data = reactive({
    form: { role: 'USER' },
    rules: {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
      confirmPassword: [
        { validator: validatePass, trigger: 'blur' },
      ],
    }
  })


  const formRef = ref()

  // 点击注册按钮的时候会触发这个方法
  const register = () => {
    formRef.value.validate((valid => {
      if (valid) {
        encryptPassword(data.form.password).then(cipher => {
          const payload = {
            username: data.form.username,
            name: data.form.name,
            password: cipher
          }
          return request.post('/register', payload)
        }).then(res => {
          if (res.code === '200') {
            ElMessage.success("注册成功")
            router.push('/login')
          } else {
            ElMessage.error(res.msg)
          }
        }).catch(err => {
          ElMessage.error(err.message || '注册失败')
        })
      }
    })).catch(error => {
      console.error(error)
    })
  }

</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow:hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #2e3143;
  background-size: cover;
}
.login-box {
  width: 350px;
  padding: 50px 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
  background-color: #fff;
}
</style>