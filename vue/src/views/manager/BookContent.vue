<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.bookName" style="width: 300px; margin-right: 10px" placeholder="请输入图书名称查询"></el-input>
      <el-input v-model="data.chapterName" style="width: 300px; margin-right: 10px" placeholder="请输入章节名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" stripe tooltip-effect="dark myEffect">
        <el-table-column label="图书名称" prop="bookName" show-overflow-tooltip></el-table-column>
        <el-table-column label="章节名称" prop="chapterName" show-overflow-tooltip></el-table-column>
        <el-table-column label="图书内容" prop="content" show-overflow-tooltip>
          <template v-slot="scope">
            <el-button type="info" size="small" @click="viewInit(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="图书信息" width="50%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="图书名称" prop="bookId">
          <el-select v-model="data.form.bookId" placeholder="请选择图书" @change="loadChapters">
            <el-option
                v-for="item in data.bookData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="章节名称" prop="chapterId">
          <el-select v-model="data.form.chapterId" placeholder="请选择章节">
            <el-option
                v-for="item in data.chapterData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="图书内容" prop="content">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="图书内容" width="50%" v-model="data.viewVisible" :close-on-click-modal="false" destroy-on-close>
      <div v-html="data.viewContent"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { authHeaders } from "@/utils/crypto";
import {onBeforeUnmount, reactive, shallowRef} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const wangUploadUrl = import.meta.env.VITE_BASE_URL + '/files/wang/upload'

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  bookName: null,
  chapterName: null,
  viewContent: null,
  viewVisible: false,
  form: {},
  formVisible: false,
  bookData: [],
  chapterData: [],
})

const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
editorConfig.MENU_CONF['uploadImage'] = {
  server: wangUploadUrl,
  fieldName: 'file',
  headers: authHeaders()
}
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
const handleCreated = (editor) => {
  editorRef.value = editor
}

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

const loadChapters = (bookId) => {
  data.form.chapterId = null
  request.get('/chapter/selectAll', {
    params: {
      bookId: bookId
    }
  }).then(res => {
    if (res.code === '200') {
      data.chapterData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const viewInit = (content) => {
  data.viewContent = content
  data.viewVisible = true
}

const load = () => {
  request.get('/bookContent/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      bookName: data.bookName,
      chapterName: data.chapterName,
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

const handleAdd = () => {
  data.form = {}
  data.chapterData = []
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  request.get('/chapter/selectAll', {
    params: {
      bookId: row.bookId
    }
  }).then(res => {
    if (res.code === '200') {
      data.chapterData = res.data
    }
  })
  data.formVisible = true
}

const add = () => {
  request.post('/bookContent/add', data.form).then(res => {
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
  request.put('/bookContent/update', data.form).then(res => {
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

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/bookContent/delete/' + id).then(res => {
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
  data.bookName = null
  data.chapterName = null
  load()
}

load()
</script>
<style>
.myEffect {
  max-width: 30%;
}
</style>
