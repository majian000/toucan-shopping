<template>
  <div class="online-admin-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button :icon="Refresh" @click="fetchData" :loading="loading">刷新</el-button>
        <span class="online-count">当前在线：<strong>{{ tableData.length }}</strong> 人</span>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="管理员账号" min-width="200" />
        <el-table-column prop="adminId" label="管理员ID" min-width="300" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link size="small" :icon="SwitchButton" v-permission="'pms:system:online:logout'" @click="handleForceLogout(row)">强制退出</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, SwitchButton } from '@element-plus/icons-vue'
import { listOnlineAdmins, forceLogout } from '@/api/system/admin'

const route = useRoute()

const tableData = ref([])
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const res = await listOnlineAdmins()
    tableData.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchData())

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
}
</style>
