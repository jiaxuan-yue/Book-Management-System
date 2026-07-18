<!--
  manager/Person.vue —— 个人资料编辑页面（后台管理端）

  功能说明：
  - 编辑当前登录用户的个人资料：头像、账号（不可修改）、名称
  - 头像支持上传更换（点击上传区域选择图片）
  - 保存后同时更新 localStorage 缓存和父组件的用户信息展示
  - 根据用户角色（ADMIN/USER）自动调用不同的更新接口

  数据同步机制：
  - 保存成功后通过 emit('updateUser') 通知父组件 Manager.vue 更新用户信息
  - 同时将更新后的用户信息写入 localStorage，确保刷新页面后数据一致

  接口调用：
  - PUT /admin/update  → 管理员保存资料
  - PUT /user/update   → 普通用户保存资料
  - POST /files/upload → 上传头像图片
-->
<template>
  <div style="width: 40%">
    <div class="card" style="padding: 30px">
      <el-form :model="data.user" label-width="100px" style="padding-right: 50px">
        <div style="margin: 20px 0; text-align: center">
          <el-upload :show-file-list="false" class="avatar-uploader" :action="uploadUrl" :on-success="handleFileUpload">
            <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <el-form-item label="账号">
          <el-input disabled v-model="data.user.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="data.user.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="钱包" v-if="data.user.role === 'USER'">
          <span style="color: red">￥{{ data.user.account }}</span>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="save">保存</el-button>
          <el-button type="warning" @click="rechargeInit" v-if="data.user.role === 'USER'">充 值</el-button>
        </div>
      </el-form>
    </div>

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
import {reactive} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";

// 文件上传的接口地址
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
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
      localStorage.setItem('system-user', JSON.stringify(res.data))
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadUser()

const handleFileUpload = (file) => {
  data.user.avatar = file.data
}

const emit = defineEmits(["updateUser"])
// 把当前修改的用户信息存储到后台数据库
const save = () => {
  let url = ''
  if (data.user.role === 'ADMIN') {
    url = '/admin/update'
  } else if (data.user.role === 'USER') {
    url = '/user/update'
  }
  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      //把更新后的用户信息存储到缓存
      localStorage.setItem('system-user', JSON.stringify(data.user))
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
      localStorage.setItem('system-user', JSON.stringify(res.data))
      data.formVisible = false
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
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