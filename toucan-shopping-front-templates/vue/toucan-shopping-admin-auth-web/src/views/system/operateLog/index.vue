<template>
  <div class="operate-log-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="功能名称/请求地址" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="请求方式">
          <el-select v-model="searchForm.method" placeholder="请选择" clearable style="width:120px">
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="IP">
          <el-input v-model="searchForm.ip" placeholder="请输入IP" clearable style="width:150px" />
        </el-form-item>
        <el-form-item label="操作人">
          <el-input v-model="searchForm.createAdminId" placeholder="请输入管理员ID" clearable style="width:260px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" v-permission="'pms:log:operate:list'" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" v-permission="'pms:log:operate:list'" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="list" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="method" label="请求方式" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="methodTag(row.method)" size="small">{{ row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uri" label="请求地址" min-width="260" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP" width="140" show-overflow-tooltip />
        <el-table-column prop="createAdminId" label="操作人" width="260" show-overflow-tooltip />
        <el-table-column prop="params" label="请求参数" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.params" style="font-size:12px;color:#909399">{{ row.params }}</span>
            <span v-else style="color:#c0c4cc">--</span>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="请求时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" v-permission="'pms:log:operate:detail'" @click="handleView(row)">详情</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'pms:log:operate:delete'" @click="handleDelete(row)">删除</el-button>
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="请求详情" width="700px" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border>
        <el-descriptions-item label="请求方式">{{ detail.method }}</el-descriptions-item>
        <el-descriptions-item label="请求地址">{{ detail.uri }}</el-descriptions-item>
        <el-descriptions-item label="IP">{{ detail.ip }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ detail.createAdminId }}</el-descriptions-item>
        <el-descriptions-item label="时间">{{ detail.createDate }}</el-descriptions-item>
        <el-descriptions-item label="应用编码">{{ detail.appCode }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <div style="max-height:300px;overflow-y:auto;white-space:pre-wrap;word-break:break-all;font-size:12px;color:#666">{{ detail.params || '--' }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark || '--' }}</el-descriptions-item>
      </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, View } from '@element-plus/icons-vue'
import { listOperateLog, queryOperateLogDetail, delOperateLog } from '@/api/system/operateLog'

const route = useRoute()

const list = ref([])
const tableTotal = ref(0)
const loading = ref(false)

function methodTag(method) {
  const map = { GET: 'success', POST: 'primary', PUT: 'warning', DELETE: 'danger' }
  return map[method] || 'info'
}

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      method: searchForm.method || undefined,
      ip: searchForm.ip || undefined,
      createAdminId: searchForm.createAdminId || undefined,
      keyword: searchForm.keyword || undefined
    }
    const res = await listOperateLog(params)
    list.value = res.data || []
    tableTotal.value = res.count || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => { fetchData() })

const searchForm = reactive({ method: '', ip: '', createAdminId: '', keyword: '' })
function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() { searchForm.method = ''; searchForm.ip = ''; searchForm.createAdminId = ''; searchForm.keyword = '' }

const pagination = reactive({ page: 1, size: 10 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

// ========== 详情 ==========
const detailVisible = ref(false)
const detailLoading = ref(false)
const detail = ref({})

async function handleView(row) {
  detail.value = {}
  detailVisible.value = true
  detailLoading.value = true
  try {
    const res = await queryOperateLogDetail(row.id)
    detail.value = res.data || {}
  } catch { } finally {
    detailLoading.value = false
  }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm(`确认删除该条日志吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delOperateLog(row.id); ElMessage.success('删除成功'); fetchData() } catch { }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.operate-log-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
  .table-card {
    .pagination-wrapper { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
