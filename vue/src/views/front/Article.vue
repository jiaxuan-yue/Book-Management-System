<!--
  front/Article.vue —— 交流论坛页面（帖子列表）

  功能说明：
  - 展示所有用户发布的帖子（不只自己的）
  - 点击封面 / 标题 /「查看详情」均可进入详情页收藏、评论
-->
<template>
  <div>
    <div style="margin: 20px auto; width: 60%">
      <div style="margin-bottom: 15px; color: #666">交流论坛 · 点击帖子进入详情后可收藏 / 评论</div>
      <div
          style="display: flex; grid-gap: 20px; margin-bottom: 10px; cursor: pointer"
          class="card"
          v-for="item in data.articleData"
          :key="item.id"
          @click="goDetail(item.id)"
      >
        <img :src="item.img" alt="" style="width: 200px; height: 140px; object-fit: cover; border-radius: 5px">
        <div style="flex: 1; display: flex; flex-direction: column">
          <div style="font-weight: bold; font-size: 16px; color: #1967e3">{{ item.title }}</div>
          <div style="margin-top: 8px; color: #666666; flex: 1" class="line2">{{ item.description }}</div>
          <div style="display: flex; align-items: center; justify-content: space-between; padding-top: 12px">
            <div style="display: flex; align-items: center; grid-gap: 20px; color: #666">
              <div style="display: flex; align-items: center; grid-gap: 5px">
                <img :src="item.userAvatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 25px; height: 25px; border-radius: 50%">
                <div>{{ item.userName }}</div>
              </div>
              <div>{{ item.time }}</div>
              <div>浏览 {{ item.views || 0 }}</div>
            </div>
            <el-button type="primary" size="small" @click.stop="goDetail(item.id)">查看详情</el-button>
          </div>
        </div>
      </div>

      <div v-if="!data.articleData.length" style="text-align: center; color: #999; padding: 40px" class="card">
        暂无帖子
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

const goDetail = (id) => {
  router.push('/front/articleDetail?id=' + id)
}

const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
    }
  }).then(res => {
    if (res.code === '200') {
      data.articleData = res.data?.list || []
      data.total = res.data?.total || 0
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>
