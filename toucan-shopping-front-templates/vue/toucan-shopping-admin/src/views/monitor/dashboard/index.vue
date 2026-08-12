<template>
  <div class="api-monitor-dashboard">
    <h2 class="page-title">接口监控大盘</h2>

    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="接口URL">
          <el-input v-model="searchForm.apiUrl" placeholder="请输入接口URL" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="应用">
          <el-input v-model="searchForm.appName" placeholder="请输入应用名" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="searchForm.startTime" type="datetime" placeholder="请选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:200px" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="searchForm.endTime" type="datetime" placeholder="请选择结束时间" value-format="YYYY-MM-DD HH:mm:ss" style="width:200px" />
        </el-form-item>
        <el-form-item label="耗时 ≥ (ms)">
          <el-input v-model="searchForm.minElapsed" placeholder="不填查全部" clearable style="width:140px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="apiUrl" label="接口URL" min-width="300" show-overflow-tooltip />
        <el-table-column prop="appName" label="应用" width="180" />
        <el-table-column prop="requestCount" label="请求数(QPS)" width="120" align="center" sortable />
        <el-table-column prop="avgMs" label="平均耗时(ms)" width="140" align="center" sortable />
        <el-table-column label="最大耗时(ms)" width="140" align="center" sortable prop="maxMs">
          <template #default="{ row }">
            <span v-if="row.maxMs > 1000" style="color:red;font-weight:bold">{{ row.maxMs }}</span>
            <span v-else>{{ row.maxMs }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="minMs" label="最小耗时(ms)" width="140" align="center" sortable />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Search" v-permission="'toucan:monitor:dashboard:trendBtn'" @click="viewRequestLog(row)">查看请求日志</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[15, 30, 100]"
          :total="tableTotal"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 请求日志弹窗 -->
    <el-dialog
      v-model="requestLogVisible"
      :title="`请求日志 - ${currentApiUrl}`"
      width="90%"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="requestLogSearch" :inline="true">
        <el-form-item label="TraceId">
          <el-input v-model="requestLogSearch.traceId" placeholder="请输入TraceId" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="耗时 ≥ (ms)">
          <el-input v-model="requestLogSearch.minElapsed" placeholder="不填查全部" clearable style="width:140px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchRequestLog">搜索</el-button>
          <el-button :icon="RefreshRight" @click="resetRequestLogSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="requestLogData" border stripe v-loading="requestLogLoading" max-height="500">
        <el-table-column prop="apiUrl" label="接口URL" min-width="260" show-overflow-tooltip />
        <el-table-column label="请求方法" width="90" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.httpMethod === 'GET'" type="success" size="small">GET</el-tag>
            <el-tag v-else-if="row.httpMethod === 'POST'" type="primary" size="small">POST</el-tag>
            <el-tag v-else-if="row.httpMethod === 'PUT'" type="warning" size="small">PUT</el-tag>
            <el-tag v-else-if="row.httpMethod === 'DELETE'" type="danger" size="small">DELETE</el-tag>
            <span v-else>{{ row.httpMethod }}</span>
          </template>
        </el-table-column>
        <el-table-column label="耗时(ms)" width="110" align="center" sortable prop="elapsedMs">
          <template #default="{ row }">
            <span style="color:red;font-weight:bold">{{ row.elapsedMs }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态码" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.statusCode >= 200 && row.statusCode < 300" type="success" size="small">{{ row.statusCode }}</el-tag>
            <el-tag v-else-if="row.statusCode >= 400" type="danger" size="small">{{ row.statusCode }}</el-tag>
            <el-tag v-else type="warning" size="small">{{ row.statusCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="TraceId" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <code v-if="row.traceId" style="background:#f0f0f0;padding:2px 6px;border-radius:2px;font-size:12px">{{ row.traceId }}</code>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="serverIp" label="服务器IP" width="140" />
        <el-table-column prop="requestTime" label="请求时间" width="180" />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="requestLogPagination.page"
          v-model:page-size="requestLogPagination.size"
          :page-sizes="[20, 50, 100]"
          :total="requestLogTotal"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="onRequestLogSizeChange"
          @current-change="onRequestLogPageChange"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, RefreshRight } from '@element-plus/icons-vue'
import { getSummary, getRequestLog } from '@/api/monitor/apiMonitor'

// ========== 搜索 ==========
const searchForm = reactive({ apiUrl: '', appName: '', startTime: '', endTime: '', minElapsed: '' })

function handleSearch() {
  if (!searchForm.startTime || !searchForm.endTime) {
    ElMessage.warning('请选择时间范围')
    return
  }
  pagination.page = 1
  fetchData()
}

function handleReset() {
  searchForm.apiUrl = ''
  searchForm.appName = ''
  searchForm.startTime = ''
  searchForm.endTime = ''
  searchForm.minElapsed = ''
}

// ========== 分页 ==========
const pagination = reactive({ page: 1, size: 30 })
const tableTotal = ref(0)

function handlePageChange(page) { pagination.page = page; fetchData() }
function handleSizeChange(size) { pagination.size = size; pagination.page = 1; fetchData() }

// ========== 表格 ==========
const tableData = ref([])
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      limit: pagination.size,
      apiUrl: searchForm.apiUrl || undefined,
      appName: searchForm.appName || undefined,
      startTime: searchForm.startTime || undefined,
      endTime: searchForm.endTime || undefined,
      minElapsed: searchForm.minElapsed ? Number(searchForm.minElapsed) : undefined
    }
    const res = await getSummary(params)
    if (res.code === 1 && res.data) {
      tableData.value = res.data.list || []
      tableTotal.value = res.data.total || 0
    } else {
      tableData.value = []
      tableTotal.value = 0
    }
  } finally {
    loading.value = false
  }
}

// ========== 请求日志弹窗 ==========
const requestLogVisible = ref(false)
const currentApiUrl = ref('')
const currentAppName = ref('')
const requestLogData = ref([])
const requestLogLoading = ref(false)
const requestLogTotal = ref(0)
const requestLogSearch = reactive({ traceId: '', minElapsed: '' })
const requestLogPagination = reactive({ page: 1, size: 50 })

function viewRequestLog(row) {
  currentApiUrl.value = row.apiUrl || ''
  currentAppName.value = row.appName || ''
  requestLogSearch.traceId = ''
  requestLogSearch.minElapsed = ''
  requestLogPagination.page = 1
  requestLogVisible.value = true
  fetchRequestLog()
}

function searchRequestLog() {
  requestLogPagination.page = 1
  fetchRequestLog()
}

function resetRequestLogSearch() {
  requestLogSearch.traceId = ''
  requestLogSearch.minElapsed = ''
}

function onRequestLogPageChange(page) { requestLogPagination.page = page; fetchRequestLog() }
function onRequestLogSizeChange(size) { requestLogPagination.size = size; requestLogPagination.page = 1; fetchRequestLog() }

async function fetchRequestLog() {
  requestLogLoading.value = true
  try {
    const params = {
      page: requestLogPagination.page,
      limit: requestLogPagination.size,
      apiUrl: currentApiUrl.value || undefined,
      appName: currentAppName.value || undefined,
      startTime: searchForm.startTime || undefined,
      endTime: searchForm.endTime || undefined,
      traceId: requestLogSearch.traceId || undefined,
      minElapsed: requestLogSearch.minElapsed ? Number(requestLogSearch.minElapsed) : undefined
    }
    const res = await getRequestLog(params)
    if (res.code === 1 && res.data) {
      requestLogData.value = res.data.list || []
      requestLogTotal.value = res.data.total || 0
    } else {
      requestLogData.value = []
      requestLogTotal.value = 0
    }
  } finally {
    requestLogLoading.value = false
  }
}
</script>

<style lang="scss" scoped>
.api-monitor-dashboard {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card {
    .pagination-wrapper { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
