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
            <div style="margin-top: 5px; color: #e6a23c">￥{{ item.price }} / 库存 {{ item.num }}</div>
            <div style="margin-top: 10px; text-align: center">
              <el-button type="primary" @click="borrow(item)">借阅</el-button>
              <el-button type="info" @click="buyInit(item.id)" :disabled="item.num === 0">购买</el-button>
              <el-button size="small" type="success" @click="readInit(item.id)">阅读</el-button>
            </div>
          </div>
        </el-col>
      </el-row>

      <div style="margin-top: 50px">
        <el-pagination @current-change="loadBook" size="small" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
      </div>
    </div>

    <el-dialog title="购买信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="购买数量" prop="num">
          <el-input-number v-model="data.form.num" :min="1" />
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="data.form.address" autocomplete="off" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="data.form.phone" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="buy">下 单</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="章节信息" width="30%" v-model="data.readVisible" :close-on-click-modal="false" destroy-on-close>
      <div v-for="item in data.chapterData" :key="item.id" style="line-height: 30px; height: 30px">
        <a :href="'/front/contentDetail?id=' + item.id" target="_blank">{{ item.name }}</a>
      </div>
    </el-dialog>
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
  form: {},
  formVisible: false,
  readVisible: false,
  chapterData: [],
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

const buyInit = (relId) => {
  data.form = {}
  data.form.relId = relId
  data.form.status = '待发货'
  data.form.userId = data.user.id
  data.form.num = 1
  data.formVisible = true
}

const buy = () => {
  request.post('/orders/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('下单成功，等待发货')
      data.formVisible = false
      loadBook()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const readInit = (bookId) => {
  request.get('/chapter/selectAll', {
    params: {
      bookId: bookId
    }
  }).then(res => {
    if (res.code === '200') {
      data.chapterData = res.data
      data.readVisible = true
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
