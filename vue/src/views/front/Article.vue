<!--
  front/Article.vue —— 交流论坛页面（帖子列表）

  功能说明：
  - 以列表形式展示所有用户发布的帖子（每页 5 条）
  - 每条帖子展示：封面图、标题、简介、作者头像和姓名、发布时间
  - 点击封面图可跳转到帖子详情页（/front/articleDetail?id=xxx）
  - 支持分页浏览

  接口调用：
  - GET /article/selectPage → 分页查询帖子列表（后端 LEFT JOIN 获取作者信息）
-->
<template>
  <div>
    <div style="margin: 20px auto; width: 40%">
      <div style="display: flex; grid-gap: 20px; margin-bottom: 10px" class="card" v-for="item in data.articleData" :key="item.id">
        <img :src="item.img" alt="" style="width: 200px; border-radius: 5px; cursor: pointer" @click="router.push('/front/articleDetail?id=' + item.id)">
        <div style="flex: 1">
          <div style="font-weight: bold">{{ item.title }}</div>
          <div style="margin-top: 5px; color: #666666" class="line2">{{ item.description }}</div>
          <div style="display: flex; padding-top: 20px; grid-gap: 30px">
            <div style="display: flex; align-items: center; grid-gap: 5px">
              <img :src="item.userAvatar" alt="" style="width: 25px; height: 25px; border-radius: 50%">
              <div style="flex: 1">{{ item.userName }}</div>
            </div>
            <div>{{ item.time }}</div>
          </div>
        </div>
      </div>

      <div style="margin-top: 20px">
        <el-pagination @current-change="load" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
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
  pageNum: 1,
  pageSize: 5,
  total: 0,
  articleData: [],
})

const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
    }
  }).then(res => {
    if (res.code === '200') {
      data.articleData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>
