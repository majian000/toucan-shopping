<template>
  <div class="order-pay-management">
    <h2 class="page-title">交易流水</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="支付流水号">
          <el-input v-model="searchForm.payNo" placeholder="请输入支付流水号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="主订单编号">
          <el-input v-model="searchForm.mainOrderNo" placeholder="请输入主订单编号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="交易类型">
          <el-select v-model="searchForm.payType" placeholder="全部" clearable style="width:120px">
            <el-option label="未确定" :value="-1" />
            <el-option label="微信" :value="0" />
            <el-option label="支付宝" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="流水状态">
          <el-select v-model="searchForm.tradeStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="待支付" :value="0" />
            <el-option label="支付成功" :value="1" />
            <el-option label="支付失败" :value="2" />
            <el-option label="已关闭" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="第三方流水号">
          <el-input v-model="searchForm.outerTradeNo" placeholder="请输入第三方流水号" clearable style="width:180px" />
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
        <el-table-column prop="payNo" label="支付流水号" width="200" show-overflow-tooltip />
        <el-table-column prop="mainOrderNo" label="主订单编号" width="200" show-overflow-tooltip />
        <el-table-column prop="userId" label="用户ID" width="160" show-overflow-tooltip />
        <el-table-column prop="payType" label="交易类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="payTypeTag(row.payType)" size="small">{{ payTypeText(row.payType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tradeType" label="支付场景" width="110" align="center" />
        <el-table-column prop="payMethod" label="支付方式" width="100" align="center">
          <template #default="{ row }">{{ payMethodText(row.payMethod) }}</template>
        </el-table-column>
        <el-table-column prop="payAmount" label="交易金额" width="110" align="right" />
        <el-table-column prop="tradeStatus" label="流水状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="tradeStatusTag(row.tradeStatus)" size="small">{{ tradeStatusText(row.tradeStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payerId" label="付款人标识" width="180" show-overflow-tooltip />
        <el-table-column prop="prepayId" label="预支付单号" width="180" show-overflow-tooltip />
        <el-table-column prop="outerTradeNo" label="第三方流水号" width="180" show-overflow-tooltip />
        <el-table-column prop="notifyId" label="通知ID" width="180" show-overflow-tooltip />
        <el-table-column prop="verifyStatus" label="验签结果" width="100" align="center">
          <template #default="{ row }">{{ verifyStatusText(row.verifyStatus) }}</template>
        </el-table-column>
        <el-table-column prop="payDate" label="支付时间" width="170" />
        <el-table-column prop="expireTime" label="过期时间" width="170" />
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
import { listOrderPay } from '@/api/order/pay'

// ===================== 常量映射 =====================
function payTypeText(v) {
  if (v === -1 || v === '-1') return '未确定'
  const map = { 0: '微信', 1: '支付宝' }
  return map[v] != null ? map[v] : v
}
function payTypeTag(v) {
  if (v === -1 || v === '-1') return 'info'
  const map = { 0: 'success', 1: 'primary' }
  return map[v] || 'info'
}
function tradeStatusText(v) {
  const map = { 0: '待支付', 1: '支付成功', 2: '支付失败', 3: '已关闭' }
  return map[v] != null ? map[v] : v
}
function tradeStatusTag(v) {
  const map = { 0: 'info', 1: 'success', 2: 'danger', 3: 'warning' }
  return map[v] || 'info'
}
function payMethodText(v) {
  const map = { 1: '线上支付', 2: '线下支付' }
  return map[v] != null ? map[v] : v
}
function verifyStatusText(v) {
  const map = { 0: '未验', 1: '通过', 2: '失败' }
  return map[v] != null ? map[v] : v
}

// ===================== 列表 =====================
const searchForm = reactive({ payNo: '', mainOrderNo: '', userId: '', payType: '', tradeStatus: '', outerTradeNo: '', startCreateDate: '', endCreateDate: '' })
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.payNo = ''; searchForm.mainOrderNo = ''; searchForm.userId = ''
  searchForm.payType = ''; searchForm.tradeStatus = ''; searchForm.outerTradeNo = ''
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
    const res = await listOrderPay(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

loadTableData()
</script>

<style lang="scss" scoped>
.order-pay-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card {
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; }
    :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  }
}
</style>
