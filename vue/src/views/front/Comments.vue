<template>
  <div>
    <div style="margin: 20px auto; width: 60%">
      <div class="card" style="margin-bottom: 5px;">
        <el-input v-model="data.content" style="width: 300px; margin-right: 10px" placeholder="请输入评论内容查询"></el-input>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
      </div>
      <div class="card" style="margin-bottom: 5px">
        <el-table :data="data.tableData" stripe tooltip-effect="dark myEffect">
          <el-table-column label="帖子标题" prop="relName" show-overflow-tooltip>
            <template v-slot="scope">
              <a :href="'/front/articleDetail?id=' + scope.row.relId">{{ scope.row.relName }}</a>
            </template>
          </el-table-column>
          <el-table-column label="评论内容" prop="content" show-overflow-tooltip></el-table-column>
          <el-table-column label="评论用户" prop="userName" show-overflow-tooltip></el-table-column>
          <el-table-column label="评论时间" prop="time"></el-table-column>
          <el-table-column label="操作" align="center" width="160">
            <template #default="scope">
              <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="card">
        <el-pagination @current-change="load" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
      </div>
    </div>
  </div>
</template>
<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  content: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
})

const load = () => {
  request.get('/comments/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.user.id,
      content: data.content
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/comments/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const reset = () => {
  data.content = null
  load()
}
</script>
<style>
.myEffect {
  max-width: 30%;
}
</style>
