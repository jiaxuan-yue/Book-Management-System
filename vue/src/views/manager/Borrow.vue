<!--
  manager/Borrow.vue —— 借阅信息管理页面

  功能说明：
  - 以分页表格展示所有借阅记录（图书名称、借阅用户、借阅时间、借阅状态）
  - 支持按图书名称模糊搜索
  - 支持"归还"操作：将借阅状态从"借阅中"更新为"已归还"
  - 支持删除借阅记录（二次确认弹窗）
  - 支持分页浏览
  - 状态标签：借阅中（蓝色标签）、已归还（绿色标签）

  与其他管理页面的区别：
  - 没有新增和编辑功能（借阅记录由用户在前台发起）
  - 额外提供"归还"按钮，管理员可以将图书标记为已归还

  接口调用：
  - GET    /borrow/selectPage  → 分页查询借阅记录
  - PUT    /borrow/update      → 更新借阅状态（归还操作）
  - DELETE /borrow/delete/{id} → 删除借阅记录
-->
<template>
  <div>

    <!-- 搜索栏：按图书名称模糊搜索 + 查询/重置按钮 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入图书名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe>
        <el-table-column label="图书名称" prop="bookName"></el-table-column>
        <el-table-column label="借阅用户" prop="userName"></el-table-column>
        <el-table-column label="借阅时间" prop="time"></el-table-column>
        <el-table-column label="借阅状态" prop="status">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '借阅中'" type="primary">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '已归还'" type="success">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template #default="scope">
            <el-button type="primary" @click="returns(scope.row)">归还</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>


  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";


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
  request.get('/borrow/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      bookName: data.name
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

const returns = (row) => {
  let data = JSON.parse(JSON.stringify(row))
  data.status = '已归还'
  request.put('/borrow/update', data).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/borrow/delete/' + id).then(res => {
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


load()
</script>
