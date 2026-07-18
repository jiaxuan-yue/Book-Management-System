<template>
  <div>
    <div style="margin: 20px auto; width: 65%">
      <div class="card" style="margin-bottom: 5px;">
        <el-input v-model="data.orderNo" style="width: 300px; margin-right: 10px" placeholder="请输入订单编号查询"></el-input>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
      </div>
      <div class="card" style="margin-bottom: 5px">
        <el-table :data="data.tableData" stripe tooltip-effect="dark myEffect">
          <el-table-column label="订单编号" prop="orderNo" show-overflow-tooltip></el-table-column>
          <el-table-column label="图书名称" prop="book.name" show-overflow-tooltip></el-table-column>
          <el-table-column label="图书单价" prop="book.price" show-overflow-tooltip></el-table-column>
          <el-table-column label="订单数量" prop="num" show-overflow-tooltip></el-table-column>
          <el-table-column label="订单总价" prop="price" show-overflow-tooltip></el-table-column>
          <el-table-column label="下单时间" prop="time"></el-table-column>
          <el-table-column label="订单状态" prop="status">
            <template v-slot="scope">
              <el-tag type="warning" v-if="scope.row.status === '待发货'">{{ scope.row.status }}</el-tag>
              <el-tag type="info" v-if="scope.row.status === '已发货'">{{ scope.row.status }}</el-tag>
              <el-tag type="success" v-if="scope.row.status === '已完成'">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="下单人" prop="userName"></el-table-column>
          <el-table-column label="收货地址" prop="address"></el-table-column>
          <el-table-column label="联系电话" prop="phone"></el-table-column>
          <el-table-column label="操作" align="center" width="200">
            <template #default="scope">
              <el-button type="info" v-if="scope.row.status === '已发货'" @click="updateStatus(scope.row)">确认收货</el-button>
              <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="card">
        <el-pagination @current-change="load" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
      </div>
    </div>
  </div>
</template>
<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  orderNo: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
})

const updateStatus = (row) => {
  row.status = '已完成'
  request.put('/orders/update', row).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const load = () => {
  request.get('/orders/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.user.id,
      orderNo: data.orderNo
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

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/orders/delete/' + id).then(res => {
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
  data.orderNo = null
  load()
}
</script>
<style>
.myEffect {
  max-width: 30%;
}
</style>
