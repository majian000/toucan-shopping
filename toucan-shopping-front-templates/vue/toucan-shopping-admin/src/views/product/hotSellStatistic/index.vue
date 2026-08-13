<template>
  <div class="hot-sell-statistic">
    <h2 class="page-title">热销统计</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="开始下单时间">
          <el-date-picker v-model="searchForm.startPayDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择开始时间" style="width:200px" />
        </el-form-item>
        <el-form-item label="结束下单时间">
          <el-date-picker v-model="searchForm.endPayDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择结束时间" style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading" row-key="skuId">
        <el-table-column prop="skuId" label="商品ID" width="200" />
        <el-table-column prop="productName" label="商品名称" min-width="270" show-overflow-tooltip />
        <el-table-column prop="shopId" label="所属店铺ID" width="200" />
        <el-table-column prop="sellCount" label="销量" width="160" align="center" />
        <el-table-column prop="sellTotal" label="总金额" width="180" align="center" />
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSizeChange" @current-change="loadTableData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Search, RefreshRight } from '@element-plus/icons-vue'
import { queryHotSellListPage } from '@/api/product/hotSellStatistic'

const searchForm = reactive({ startPayDate: '', endPayDate: '' })

const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await queryHotSellListPage(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.startPayDate = ''; searchForm.endPayDate = ''; handleSearch() }

loadTableData()
</script>

<style lang="scss" scoped>
.hot-sell-statistic {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
