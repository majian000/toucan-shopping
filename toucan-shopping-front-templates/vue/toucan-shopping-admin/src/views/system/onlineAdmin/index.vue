<template>
  <div class="online-admin-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="账号ID">
          <el-input v-model="searchForm.adminId" placeholder="请输入账号ID" clearable style="width:240px" />
        </el-form-item>
        <el-form-item label="账号名称">
          <el-input v-model="searchForm.username" placeholder="请输入账号名称" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="登录应用">
          <el-select v-model="searchForm.appCode" placeholder="请选择" clearable style="width:200px" :loading="appLoading">
            <el-option v-for="a in appOptions" :key="a.code" :label="a.code + ' ' + a.name" :value="a.code" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button :icon="Refresh" @click="fetchData" :loading="loading">刷新</el-button>
        <span class="online-count">当前在线：<strong>{{ tableTotal }}</strong> 人</span>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="adminId" label="账号ID" min-width="280" show-overflow-tooltip />
        <el-table-column prop="username" label="账号名称" min-width="150" />
        <el-table-column prop="appName" label="登录应用" min-width="150" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link size="small" :icon="SwitchButton" v-permission="'pms:system:online:logout'" @click="handleForceLogout(row)">强制退出</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[15, 30, 100, 200]"
          :total="tableTotal"
          layout="total, sizes, prev, pager, next, jumper"
          background
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, SwitchButton, Search } from '@element-plus/icons-vue'
import { listOnlineAdmins, forceLogout } from '@/api/system/admin'
import { listAllApps } from '@/api/system/app'

const route = useRoute()

const tableData = ref([])
const tableTotal = ref(0)
const loading = ref(false)
const pagination = reactive({ page: 1, size: 15 })
const searchForm = reactive({ adminId: '', username: '', appCode: '' })
const appOptions = ref([])
const appLoading = ref(false)

async function fetchApps() {
  appLoading.value = true
  try {
    const res = await listAllApps()
    appOptions.value = res.data || []
  } catch { /* ignore */ } finally { appLoading.value = false }
}

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      adminId: searchForm.adminId || undefined,
      username: searchForm.username || undefined,
      appCode: searchForm.appCode || undefined
    }
    const res = await listOnlineAdmins(params)
    tableData.value = res.data || []
    tableTotal.value = res.count || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => { fetchApps(); fetchData() })

watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() {
  searchForm.adminId = ''
  searchForm.username = ''
  searchForm.appCode = ''
  pagination.page = 1; fetchData()
}

function handleForceLogout(row) {
  ElMessageBox.confirm(`确认强制退出"${row.username}"的登录吗？`, '强制退出', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await forceLogout(row.id)
      ElMessage.success(`「${row.username}」已被强制退出登录`)
      fetchData()
    } catch { /* 拦截器处理 */ }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.online-admin-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
  .table-card {
    .toolbar {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: $gap-md;
      .online-count {
        color: $text-secondary;
        font-size: 14px;
        strong {
          color: $text-primary;
          font-weight: 700;
        }
      }
    }
    :deep(.el-table) {
      th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
    }
  }
  .pagination-wrapper {
    display: flex; justify-content: flex-end; margin-top: $gap-md;
  }
}
</style>
