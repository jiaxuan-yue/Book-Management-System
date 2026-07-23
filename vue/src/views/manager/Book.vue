<!--
  manager/Book.vue —— 图书信息管理页面

  功能说明：
  - 以分页表格展示所有图书列表（名称、封面、出版社、作者）
  - 支持按书名模糊搜索
  - 支持新增图书（弹窗表单，含封面图片上传）
  - 支持编辑图书信息
  - 支持删除图书（二次确认弹窗）
  - 支持分页浏览

  接口调用：
  - GET    /book/selectPage  → 分页查询图书列表
  - POST   /book/add         → 新增图书
  - PUT    /book/update      → 修改图书信息
  - DELETE /book/delete/{id} → 删除图书
  - POST   /files/upload     → 上传图书封面图片
-->
<template>
  <div>

    <!-- 搜索栏：按图书名称模糊搜索 + 查询/重置按钮 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="封面">
          <template #default="scope">
            <el-image :src="scope.row.img" style="width: 40px; height: 50px; border-radius: 5px"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="出版社" prop="publishing"></el-table-column>
        <el-table-column label="作者" prop="author"></el-table-column>
        <el-table-column label="价格" prop="price"></el-table-column>
        <el-table-column label="库存" prop="num"></el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="封面" prop="img">
          <el-upload :action="uploadUrl" :headers="uploadHeaders" list-type="picture" :on-success="handleImgSuccess">
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="data.form.author" autocomplete="off" />
        </el-form-item>
        <el-form-item label="出版社" prop="publishing">
          <el-input v-model="data.form.publishing" autocomplete="off" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="data.form.price" autocomplete="off" />
        </el-form-item>
        <el-form-item label="库存" prop="num">
          <el-input v-model="data.form.num" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import request from "@/utils/request"
import { authHeaders } from "@/utils/crypto";
import {reactive} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

// 文件上传的接口地址
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'
const uploadHeaders = authHeaders()

const data = reactive({
  pageNum: 1,
  pageSize: 5,
  total: 0,
  formVisible: false,
  form: {},
  tableData: [],
  name: null
})

// 分页查询
const load = () => {
  request.get('/book/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

// 新增
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 新增保存
const add = () => {
  request.post('/book/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 编辑保存
const update = () => {
  request.put('/book/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 弹窗保存
const save = () => {
  // data.form有id就是更新，没有就是新增
  data.form.id ? update() : add()
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/book/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 重置
const reset = () => {
  data.name = null
  load()
}

// 处理文件上传的钩子
const handleImgSuccess = (res) => {
  data.form.img = res.data
}

load()
</script>
