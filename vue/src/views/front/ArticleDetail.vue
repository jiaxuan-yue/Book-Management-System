<template>
  <div>
    <div style="margin: 20px auto; width: 50%">
      <div class="card" style="padding: 30px">
        <div style="text-align: center; font-weight: bold; font-size: 18px">{{ data.articleData.title }}</div>
        <div style="margin-top: 10px; text-align: center; color: #666666">
          <span>浏览量：{{ data.articleData.views }}</span>
          <span style="margin-left: 50px">发布时间：{{ data.articleData.time }}</span>
        </div>
        <div style="margin-top: 20px; text-align: center">
          <el-button v-if="data.collectFlag" type="danger" size="small" @click="collect">取消收藏</el-button>
          <el-button v-else type="primary" size="small" @click="collect">收藏</el-button>
        </div>
        <div style="margin-top: 30px" v-html="data.articleData.content"></div>
      </div>

      <div class="card" style="margin-top: 10px; margin-bottom: 100px; padding: 20px">
        <div>请说说你的想法吧</div>
        <div style="margin-top: 10px">
          <el-input type="textarea" :rows="4" v-model="data.content" placeholder="请填写评论内容"></el-input>
        </div>
        <div style="margin-top: 10px; text-align: right">
          <el-button type="primary" @click="comment(data.content)">评论</el-button>
        </div>
        <div>看看大家都评论了什么（{{ data.commentsData.length }}）</div>
        <div style="margin-top: 20px; padding: 0 20px">
          <div v-for="item in data.commentsData" :key="item.id" style="display: flex; grid-gap: 20px; margin-bottom: 20px">
            <img :src="item.userAvatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
            <div>
              <div style="font-weight: bold">{{ item.userName }}</div>
              <div style="margin-top: 10px">{{ item.content }}</div>
              <div style="margin-top: 5px; color: #666666">
                <span style="margin-right: 20px">{{ item.time }}</span>
                <el-button type="info" size="small" @click="replyInit(item.id)">回复</el-button>
              </div>
              <div style="margin-top: 20px">
                <div v-for="it in item.children" :key="it.id" style="display: flex; grid-gap: 20px; margin-bottom: 20px">
                  <img :src="it.userAvatar" alt="" style="width: 30px; height: 30px; border-radius: 50%">
                  <div>
                    <div><span style="font-weight: bold">{{ it.userName }}</span> 回复：</div>
                    <div style="margin-top: 5px">{{ it.content }}</div>
                    <div style="margin-top: 5px; color: #666666">
                      <span style="margin-right: 20px">{{ it.time }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog title="评论回复" width="30%" v-model="data.dialogVisible" :close-on-click-modal="false" destroy-on-close>
      <div>
        <el-input type="textarea" :rows="4" v-model="data.replyContent" placeholder="请填写评论内容"></el-input>
      </div>
      <div style="margin-top: 10px; text-align: right">
        <el-button type="primary" @click="comment(data.replyContent)">回复</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  articleId: router.currentRoute.value.query.id,
  articleData: {},
  collectFlag: false,
  content: null,
  replyContent: null,
  commentsData: [],
  fid: 0,
  dialogVisible: false,
})

const loadArticle = () => {
  request.get('/article/selectById/' + data.articleId).then(res => {
    if (res.code === '200') {
      data.articleData = res.data
      data.articleData.views = data.articleData.views + 1
      request.put('/article/update', data.articleData)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadArticle()

const collect = () => {
  request.post('/collect/add', {
    userId: data.user.id,
    relId: data.articleId
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      loadCollect()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadCollect = () => {
  request.get('/collect/selectAll', {
    params: {
      userId: data.user.id,
      relId: data.articleId
    }
  }).then(res => {
    if (res.code === '200') {
      data.collectFlag = !!res.data.length
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCollect()

const replyInit = (fid) => {
  data.fid = fid
  data.dialogVisible = true
}

const comment = (content) => {
  if (!content) {
    ElMessage.error('请输入评论内容')
    return
  }
  request.post('/comments/add', {
    userId: data.user.id,
    relId: data.articleId,
    content: content,
    fid: data.fid
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('评论成功')
      data.content = null
      data.replyContent = null
      data.fid = 0
      data.dialogVisible = false
      loadComments()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadComments = () => {
  request.get('/comments/selectAll', {
    params: {
      relId: data.articleId,
      fid: 0
    }
  }).then(res => {
    if (res.code === '200') {
      data.commentsData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadComments()
</script>
