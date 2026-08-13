<template>
  <div class="login-history-management">
    <h2 class="page-title">登录历史</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userMainId" placeholder="请输入用户ID" clearable style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading" row-key="id">
        <el-table-column prop="id" label="ID" width="200" show-overflow-tooltip />
        <el-table-column prop="userMainId" label="用户ID" width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="登录IP" width="180" />
        <el-table-column prop="loginSrcType" label="登录源头" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.loginSrcType === 1 || row.loginSrcType === '1' ? 'primary' : 'info'" size="small">
              {{ loginSrcTypeText(row.loginSrcType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="deleteStatus" label="删除状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.deleteStatus === 1 || row.deleteStatus === '1' ? 'danger' : 'success'" size="small">
              {{ row.deleteStatus === 1 || row.deleteStatus === '1' ? '已删除' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSearch" @current-change="handleSearch"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Search, RefreshRight } from '@element-plus/icons-vue'
import { listLoginHistory } from '@/api/user/loginHistory'

const searchForm = reactive({ userMainId: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.userMainId = '' }

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
    const res = await listLoginHistory(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function loginSrcTypeText(type) {
  if (type === 1 || type === '1') return 'PC'
  return type || '-'
}

loadTableData()
</script>

<style lang="scss" scoped>
.login-history-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
