<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入章节名称查询"></el-input>
      <el-input v-model="data.bookName" style="width: 300px; margin-right: 10px" placeholder="请输入图书名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="章节名称" prop="name"></el-table-column>
        <el-table-column label="所属图书" prop="bookName"></el-table-column>
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

    <el-dialog title="章节信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="图书" prop="bookId">
          <el-select v-model="data.form.bookId" placeholder="请选择图书">
            <el-option
                v-for="item in data.bookData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
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
  name: null,
  bookName: null,
  bookData: [],
})

const loadBooks = () => {
  request.get('/book/selectAll').then(res => {
    if (res.code === '200') {
      data.bookData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadBooks()

const load = () => {
  request.get('/chapter/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      bookName: data.bookName,
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  request.post('/chapter/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/chapter/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/chapter/delete/' + id).then(res => {
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
  data.name = null
  data.bookName = null
  load()
}

load()
</script>
