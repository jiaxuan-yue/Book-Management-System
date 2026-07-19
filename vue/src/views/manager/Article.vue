<!--
  manager/Article.vue —— 我的帖子管理页面

  功能说明：
  - 以分页表格展示当前登录用户发布的所有帖子（仅显示自己的帖子）
  - 支持按帖子标题模糊搜索
  - 支持发布新帖子（弹窗表单，含封面上传、富文本编辑器编写正文）
  - 支持编辑帖子（复用发布弹窗）
  - 支持删除帖子（二次确认弹窗）
  - 支持点击"查看"按钮弹窗预览帖子正文内容（HTML 渲染）

  富文本编辑器：
  - 使用 wangEditor 编辑器，支持图文混排
  - 编辑器中的图片上传使用专用接口 /files/wang/upload
  - 编辑器配置在 editorConfig 中设置图片上传地址

  与 ArticleAll.vue 的区别：
  - 本页面通过 userId 参数过滤，只显示当前用户的帖子
  - ArticleAll.vue 显示所有帖子，且只有删除功能（无编辑）

  接口调用：
  - GET    /article/selectPage  → 分页查询帖子（带 userId 过滤）
  - POST   /article/add         → 发布新帖子
  - PUT    /article/update      → 修改帖子
  - DELETE /article/delete/{id} → 删除帖子
-->
<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.title" style="width: 300px; margin-right: 10px" placeholder="请输入帖子标题查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
      <el-button type="success" @click="goFront">去论坛看全部帖子 / 收藏</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">发布帖子</el-button>
      </div>
      <el-table :data="data.tableData" stripe tooltip-effect="dark myEffect" style="width: 100%">
        <el-table-column label="标题" prop="title" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column label="封面" width="100">
          <template #default="scope">
            <el-image :src="scope.row.img" style="width: 50px; height: 40px; border-radius: 5px"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="简介" prop="description" min-width="220" show-overflow-tooltip></el-table-column>
        <el-table-column label="作者" prop="userName" width="120"></el-table-column>
        <el-table-column label="内容" prop="content" width="120" show-overflow-tooltip>
          <template v-slot="scope">
            <el-button type="info" size="small" @click="viewInit(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column label="浏览量" prop="views" width="90"></el-table-column>
        <el-table-column label="发布时间" prop="time" width="180"></el-table-column>
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
    <el-dialog title="帖子信息" width="50%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="封面" prop="img">
          <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess">
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="data.form.title" autocomplete="off" />
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" v-model="data.form.description" autocomplete="off" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
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
    <el-dialog title="帖子内容" width="50%" v-model="data.viewVisible" :close-on-click-modal="false" destroy-on-close>
      <div v-html="data.viewContent"></div>
    </el-dialog>
  </div>
</template>
<script setup>
import request from "@/utils/request";
import {onBeforeUnmount, reactive, shallowRef} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'
const wangUploadUrl = import.meta.env.VITE_BASE_URL + '/files/wang/upload'

const goFront = () => {
  router.push('/front/article')
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  title: null,
  form: {},
  formVisible: false,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  viewContent: null,
  viewVisible: false,
})

const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
editorConfig.MENU_CONF['uploadImage'] = {
  server: wangUploadUrl,
  fieldName: 'file'
}
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
const handleCreated = (editor) => {
  editorRef.value = editor
}

const viewInit = (content) => {
  data.viewContent = content
  data.viewVisible = true
}
const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.user.id,
      title: data.title
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

const handleAdd = () => {
  data.form = {}
  data.form.userId = data.user.id
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  request.post('/article/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/article/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
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

const handleImgSuccess = (res) => {
  data.form.img = res.data
}

const reset = () => {
  data.title = null
  load()
}
</script>
<style>
/* 仅限制悬浮提示气泡宽度，不要加到表格本身 */
.myEffect {
  max-width: 30%;
}
</style>
