<template>
  <div class="login-history">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="账号ID">
          <el-input v-model="searchForm.adminId" placeholder="请输入管理员ID" clearable style="width:260px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="adminId" label="管理员ID" width="280" show-overflow-tooltip />
        <el-table-column prop="ip" label="登录IP" width="150" />
        <el-table-column prop="loginSrcType" label="登录来源" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small">{{ row.loginSrcType === 1 ? 'Web' : row.loginSrcType === 2 ? 'App' : '其他' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="登录时间" width="170" sortable />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" v-permission="'pms:log:login:show'" @click="handleShow(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="tableTotal"
          layout="total, sizes, prev, pager, next, jumper"
          background
        />
      </div>
    </el-card>

    <el-dialog v-model="detailVisible" title="登录详情" width="500px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="管理员ID">{{ detail.adminId }}</el-descriptions-item>
        <el-descriptions-item label="登录IP">{{ detail.loginIp }}</el-descriptions-item>
        <el-descriptions-item label="登录来源">{{ detail.loginSrcType === 1 ? 'Web' : detail.loginSrcType === 2 ? 'App' : '其他' }}</el-descriptions-item>
        <el-descriptions-item label="登录时间">{{ detail.createDate }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Search, Refresh, View } from '@element-plus/icons-vue'
import { listLoginHistory } from '@/api/system/loginHistory'

const route = useRoute()

const tableData = ref([])
const tableTotal = ref(0)
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      adminId: searchForm.adminId || undefined
    }
    const res = await listLoginHistory(params)
    tableData.value = res.data || []
    tableTotal.value = res.count || 0
  } catch { /* ignore */ }
  finally { loading.value = false }
}

onMounted(fetchData)

const searchForm = reactive({ adminId: '' })
function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() { searchForm.adminId = '' }

const pagination = reactive({ page: 1, size: 10 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

const detailVisible = ref(false)
const detail = ref({})

function handleShow(row) {
  detail.value = row
  detailVisible.value = true
}
</script>

<style lang="scss" scoped>
.login-history {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
  .table-card {
    .pagination-wrapper { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
}
</style>
