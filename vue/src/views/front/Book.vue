<template>
  <div>
    <div style="margin: 20px auto; width: 60%">
      <el-row :gutter="30">
        <el-col :span="5" v-for="item in data.bookData" :key="item.id">
          <div style="margin-bottom: 30px">
            <img :src="item.img" alt="" style="width: 100%; height: 220px; border-radius: 5px">
            <div style="font-size: 16px; font-weight: bold">{{ item.name }}</div>
            <div style="display: flex; padding-top: 10px">
              <div style="flex: 1">{{ item.author }}</div>
              <div style="width: 120px">{{ item.publishing }}</div>
            </div>
            <div style="margin-top: 10px; text-align: center">
              <el-button type="primary" @click="borrow(item)">借阅</el-button>
            </div>
          </div>
        </el-col>
      </el-row>

      <div style="margin-top: 50px">
        <el-pagination @current-change="loadBook" size="small" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
      </div>
    </div>
  </div>
</template>
<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  pageNum: 1,
  pageSize: 12,
  total: 0,
  bookData: [],
})

const loadBook = () => {
  request.get('/book/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
    }
  }).then(res => {
    if (res.code === '200') {
      data.bookData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadBook()

const borrow = (book) => {
  request.post('/borrow/add', {
    userId: data.user.id,
    bookId: book.id,
    status: '借阅中'
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('借阅成功，看完要及时归还哦！！')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>
<style>
.el-col-5 {
  width: 20%;
  max-width: 20%;
}
</style>
