<template>
  <div style="width: 40%; margin: 5px auto" class="card">
    <el-form ref="user" :model="data.user" label-width="60px" style="padding: 20px">
      <div style="text-align: center; margin-bottom: 20px">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item prop="username" label="用户名">
        <el-input disabled v-model="data.user.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="data.user.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="data.user.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="data.user.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="price" label="钱包">
        <span style="color: red">￥{{ data.user.account }}</span>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保 存</el-button>
        <el-button type="warning" @click="rechargeInit" v-if="data.user.role === 'USER'">充 值</el-button>
      </div>
    </el-form>

    <el-dialog title="充值信息" width="30%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px">
        <el-form-item label="支付方式" prop="type">
          <el-radio-group v-model="data.type">
            <el-radio value="weiPay">
              <img src="@/assets/imgs/weiPay.jpg" alt="" style="width: 100px">
            </el-radio>
            <el-radio value="aliPay">
              <img src="@/assets/imgs/aliPay.jpg" alt="" style="width: 100px">
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="充值金额" prop="recharge">
          <el-input v-model="data.recharge" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="recharge">充 值</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || localStorage.getItem('system-user') || '{}'),
  formVisible: false,
  type: null,
  recharge: 100,
})

const loadUser = () => {
  if (!data.user.id || data.user.role !== 'USER') {
    return
  }
  request.get('/user/selectById/' + data.user.id).then(res => {
    if (res.code === '200') {
      data.user = res.data
      localStorage.setItem('xm-user', JSON.stringify(res.data))
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadUser()

const handleFileUpload = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])
const update = () => {
  let url = ''
  if (data.user.role === 'ADMIN') {
    url = '/admin/update'
  } else if (data.user.role === 'USER') {
    url = '/user/update'
  }
  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('保存成功')
      localStorage.setItem('xm-user', JSON.stringify(data.user))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const rechargeInit = () => {
  data.formVisible = true
}

const recharge = () => {
  if (!data.type) {
    ElMessage.warning('请选择支付方式')
    return
  }
  if (data.recharge <= 0) {
    ElMessage.warning('请输入正确的充值金额')
    return
  }
  let form = JSON.parse(JSON.stringify(data.user))
  form.account = Number(data.recharge)
  request.post('/user/recharge', form).then(res => {
    if (res.code === '200') {
      ElMessage.success('充值成功')
      data.user = res.data
      localStorage.setItem('xm-user', JSON.stringify(res.data))
      data.formVisible = false
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
