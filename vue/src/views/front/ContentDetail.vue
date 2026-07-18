<template>
  <div>
    <div style="margin: 30px auto; width: 40%">
      <div style="border-radius: 5px; padding: 20px 40px; background-color: #faf4e1" v-html="item.content" v-for="item in data.bookContent" :key="item.id"></div>
      <div style="margin-top: 30px">
        <el-pagination @current-change="loadBookContent" background layout="prev, next" prev-text="上一页" next-text="下一页" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
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
  chapterId: router.currentRoute.value.query.id,
  bookContent: [],
  total: 0,
  pageNum: 1,
  pageSize: 1,
})

const loadBookContent = () => {
  request.get('/bookContent/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      chapterId: data.chapterId
    }
  }).then(res => {
    if (res.code === '200') {
      data.bookContent = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadBookContent()
</script>
