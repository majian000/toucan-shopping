<template>
  <div class="order-refund-management">
    <h2 class="page-title">退款流水</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="退款流水号">
          <el-input v-model="searchForm.refundNo" placeholder="请输入退款流水号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="支付流水号">
          <el-input v-model="searchForm.payNo" placeholder="请输入支付流水号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="主订单编号">
          <el-input v-model="searchForm.mainOrderNo" placeholder="请输入主订单编号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="子订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入子订单编号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="退款状态">
          <el-select v-model="searchForm.refundStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="退款中" :value="0" />
            <el-option label="退款成功" :value="1" />
            <el-option label="退款失败" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.startCreateDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="开始时间" style="width:200px"
          />
        </el-form-item>
        <el-form-item label="至">
          <el-date-picker
            v-model="searchForm.endCreateDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="结束时间" style="width:200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading" row-key="id">
        <el-table-column prop="id" label="主键" width="180" show-overflow-tooltip />
        <el-table-column prop="refundNo" label="退款流水号" width="200" show-overflow-tooltip />
        <el-table-column prop="payNo" label="支付流水号" width="200" show-overflow-tooltip />
        <el-table-column prop="mainOrderNo" label="主订单编号" width="200" show-overflow-tooltip />
        <el-table-column prop="orderNo" label="子订单编号" width="200" show-overflow-tooltip />
        <el-table-column prop="userId" label="用户ID" width="160" show-overflow-tooltip />
        <el-table-column prop="refundType" label="退款类型" width="100" align="center">
          <template #default="{ row }">{{ refundTypeText(row.refundType) }}</template>
        </el-table-column>
        <el-table-column prop="refundAmount" label="退款金额" width="110" align="right" />
        <el-table-column prop="refundStatus" label="退款状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="refundStatusTag(row.refundStatus)" size="small">{{ refundStatusText(row.refundStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="outerRefundNo" label="第三方退款单号" width="180" show-overflow-tooltip />
        <el-table-column prop="refundDate" label="退款时间" width="170" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSearch" @current-change="loadTableData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Search, RefreshRight } from '@element-plus/icons-vue'
import { listOrderRefund } from '@/api/order/refund'

// ===================== 常量映射 =====================
function refundTypeText(v) {
  const map = { 1: '全额', 2: '部分' }
  return map[v] != null ? map[v] : v
}
function refundStatusText(v) {
  const map = { 0: '退款中', 1: '退款成功', 2: '退款失败' }
  return map[v] != null ? map[v] : v
}
function refundStatusTag(v) {
  const map = { 0: 'info', 1: 'success', 2: 'danger' }
  return map[v] || 'info'
}

// ===================== 列表 =====================
const searchForm = reactive({ refundNo: '', payNo: '', mainOrderNo: '', orderNo: '', userId: '', refundStatus: '', startCreateDate: '', endCreateDate: '' })
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.refundNo = ''; searchForm.payNo = ''; searchForm.mainOrderNo = ''
  searchForm.orderNo = ''; searchForm.userId = ''; searchForm.refundStatus = ''
  searchForm.startCreateDate = ''; searchForm.endCreateDate = ''
  handleSearch()
}

function buildSearchParams() {
  const p = {
    ...searchForm,
    page: pagination.page,
    limit: pagination.limit
  }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listOrderRefund(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

loadTableData()
</script>

<style lang="scss" scoped>
.order-refund-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card {
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; }
    :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  }
}
</style>
