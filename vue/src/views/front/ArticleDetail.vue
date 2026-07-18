<template>
  <div>
    <div style="margin: 20px auto; width: 50%">
      <div class="card" style="padding: 30px">
        <div style="text-align: center; font-weight: bold; font-size: 18px">{{ data.articleData.title }}</div>
        <div style="margin-top: 10px; text-align: center; color: #666666">
          <span>浏览量：{{ data.articleData.views }}</span>
          <span style="margin-left: 50px">发布时间：{{ data.articleData.time }}</span>
        </div>
        <div style="margin-top: 50px" v-html="data.articleData.content"></div>
      </div>
    </div>
  </div>
</template>
<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  articleId: router.currentRoute.value.query.id,
  articleData: {},
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
</script>
