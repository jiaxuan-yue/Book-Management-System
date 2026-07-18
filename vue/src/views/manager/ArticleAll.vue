<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.title" style="width: 300px; margin-right: 10px" placeholder="请输入帖子标题查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe tooltip-effect="dark" class="myEffect">
        <el-table-column label="标题" prop="title" show-overflow-tooltip></el-table-column>
        <el-table-column label="封面">
          <template #default="scope">
            <el-image :src="scope.row.img" style="width: 50px; height: 40px; border-radius: 5px"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="简介" prop="description" show-overflow-tooltip></el-table-column>
        <el-table-column label="作者" prop="userName"></el-table-column>
        <el-table-column label="内容" prop="content" show-overflow-tooltip>
          <template v-slot="scope">
            <el-button type="info" @click="viewInit(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column label="浏览量" prop="views"></el-table-column>
        <el-table-column label="发布时间" prop="time"></el-table-column>
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

    <el-dialog title="帖子内容" width="50%" v-model="data.viewVisible" :close-on-click-modal="false" destroy-on-close>
      <div v-html="data.viewContent"></div>
    </el-dialog>
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
  tableData: [],
  title: null,
  viewContent: null,
  viewVisible: false
})

const viewInit = (content) => {
  data.viewContent = content
  data.viewVisible = true
}

const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/article/delete/' + id).then(res => {
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
  data.title = null
  load()
}

load()
</script>
<style>
.myEffect {
  max-width: 30%;
}
</style>
