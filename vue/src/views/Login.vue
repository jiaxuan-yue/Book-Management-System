<!--
  Login.vue —— 登录页面组件

  功能说明：
  - 提供用户登录表单，包含账号、密码、角色选择三个字段
  - 角色下拉框支持选择"管理员"或"用户"，默认为管理员
  - 表单验证：账号和密码为必填项
  - 登录成功后将用户信息存入 localStorage（key: 'system-user'），并跳转到后台管理首页
  - 页面底部提供"注册"链接，引导未注册的用户前往注册页

  登录流程：
  1. 用户填写账号、密码，选择角色
  2. 点击"登录"按钮，触发表单验证
  3. 验证通过后调用 POST /login 接口
  4. 后端验证成功后返回用户信息
  5. 前端将用户信息序列化后存入 localStorage
  6. 使用 router.push 跳转到 /manager/home 后台首页

  接口调用：POST /login （通过 request.js 封装的 axios 发送请求）
-->
<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 30px; text-align: center; margin-bottom: 30px; color: #1967e3">欢 迎 登 录</div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role">
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="USER" label="用户"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: right;">
        还没有账号？请 <a href="/register">注册</a>
      </div>
    </div>
    <el-dialog title="郑重声明" v-model="data.dialogVisible" :show-close="false" width="40%" :close-on-click-modal="false" destroy-on-close>
      <div style="font-size: 16px; line-height: 26px; margin-bottom: 20px; text-align: justify">
        本平台已经对本项目申请了<b style="color: #000">软件著作权</b>，完善了所有该项目资源相关的法律文件材料。
        <b style="color: #ff2424">本平台付费用户可以学习该项目自己使用，禁止在其他平台做转手或者倒卖，禁止在私域未经授权分享源码，禁止上传代码到github、gitee、gitlab等代码托管平台。</b>
        我们公司的法务会在各大平台（例如：闲鱼、小红书、B站、CSDN等社交媒体平台）不定期检索， 一旦发现倒卖或转手的现象，我们会第一时间收集好证据，向你发送律师函。
        <b style="color: #000">现在互联网所有账号都是实名制，我们可以明确追溯到你。请大家不要做违法的事情，不要因为一时糊涂给自己的人生轨迹抹上一层黑。</b>
      </div>
      <div style="margin-top: 10px; font-size: 16px; color: #000">
        本项目唯一官方平台：<b>项目实战网（<a href="https://www.javaxmsz.cn" target="_blank">https://www.javaxmsz.cn</a>）</b>
      </div>
      <div style="margin-top: 5px; font-size: 16px"></div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="data.dialogVisible=false">我已明确法律规则，承诺本项目仅限个人使用</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
  import { reactive, ref } from "vue";
  import { User, Lock } from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import router from "@/router";

  const data = reactive({
    dialogVisible: true,
    form: { role: 'ADMIN' },
    rules: {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
    }
  })

  const formRef = ref()

  // 点击登录按钮的时候会触发这个方法
  const login = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // 调用后台的接口
        request.post('/login', data.form).then(res => {
          if (res.code === '200') {
            ElMessage.success("登录成功")
            localStorage.setItem('system-user', JSON.stringify(res.data))
            localStorage.setItem('xm-user', JSON.stringify(res.data))
            router.push('/manager/home')
          } else {
            ElMessage.error(res.msg)
          }
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
  background: #242d8f;
  background-size: cover;
}
.login-box {
  width: 400px;
  padding: 50px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
  background-color: #fff;
}
</style>