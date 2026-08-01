<template>
  <div class="locked-account-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button :icon="Refresh" @click="fetchData">刷新</el-button>
      </div>

      <el-table :data="list" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="管理员账号" width="140" />
        <el-table-column prop="adminId" label="管理员ID" min-width="260" show-overflow-tooltip />
        <el-table-column prop="appCode" label="应用编码" min-width="120" />
        <el-table-column label="剩余锁定时间" min-width="160" align="center">
          <template #default="{ row }">
            {{ formatRemain(row.remainSeconds) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Unlock"
              v-permission="'system:adminLock:unlock'" @click="handleUnlock(row)">
              解锁
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && list.length === 0" description="暂无被锁定的账号" style="margin-top:40px" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Unlock } from '@element-plus/icons-vue'
import { listLockedAccounts, unlockAccount } from '@/api/system/admin'

const route = useRoute()

const list = ref([])
const loading = ref(false)

function formatRemain(seconds) {
  if (!seconds || seconds <= 0) return '即将过期'
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  if (m > 0) return `${m}分${s}秒`
  return `${s}秒`
}

async function fetchData() {
  loading.value = true
  try {
    const res = await listLockedAccounts()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => { fetchData() })

function handleUnlock(row) {
  ElMessageBox.confirm(`确认解锁管理员「${row.username || row.adminId}」吗？`, '解锁确认', {
    confirmButtonText: '确定解锁',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await unlockAccount(row.adminId)
      ElMessage.success('解锁成功')
      fetchData()
    } catch { }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.locked-account-management {
  .table-card {
    .toolbar { margin-bottom: $gap-md; }
  }
  :deep(.el-table) {
    th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
  }
}
</style>
