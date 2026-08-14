<template>
  <div class="head-sculpture-approve-management">
    <h2 class="page-title">头像审核</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userMainId" placeholder="请输入用户ID" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="searchForm.approveStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="审核中" value="1" />
            <el-option label="审核通过" value="2" />
            <el-option label="审核驳回" value="3" />
          </el-select>
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
        <el-table-column prop="userMainId" label="用户ID" width="180" show-overflow-tooltip />
        <el-table-column label="头像" width="90" align="center">
          <template #default="{ row }">
            <el-image v-if="row.httpHeadSculpture" :src="row.httpHeadSculpture" :preview-src-list="[row.httpHeadSculpture]" preview-teleported fit="cover" style="width:40px;height:40px;border-radius:50%;cursor:zoom-in" />
          </template>
        </el-table-column>
        <el-table-column prop="approveStatus" label="审核状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="approveStatusType(row.approveStatus)" size="small">{{ approveStatusText(row.approveStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rejectText" label="驳回原因" width="180" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button v-if="row.approveStatus === 1 || row.approveStatus === '1'" type="success" link size="small" :icon="CircleCheck" v-permission="'toucan:user:headSculptureApprove:pass'" @click="handlePass(row)">通过</el-button>
            <el-button v-if="row.approveStatus === 1 || row.approveStatus === '1'" type="danger" link size="small" :icon="CircleClose" v-permission="'toucan:user:headSculptureApprove:reject'" @click="handleReject(row)">驳回</el-button>
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

    <!-- 驳回弹窗 -->
    <el-dialog v-model="rejectVisible" title="审核驳回" width="480px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="rejectFormRef" :model="rejectForm" :rules="rejectRules" label-width="100px">
        <el-form-item label="驳回原因" prop="rejectText">
          <el-input v-model="rejectForm.rejectText" type="textarea" :rows="4" maxlength="200" placeholder="请输入驳回原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRejectSubmit" :loading="rejectLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import { listHeadSculptureApprove, passHeadSculptureApprove, rejectHeadSculptureApprove } from '@/api/user/headSculptureApprove'

const searchForm = reactive({ userMainId: '', approveStatus: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.userMainId = ''; searchForm.approveStatus = '' }

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
    const res = await listHeadSculptureApprove(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function approveStatusText(status) {
  if (status === 1 || status === '1') return '审核中'
  if (status === 2 || status === '2') return '审核通过'
  if (status === 3 || status === '3') return '审核驳回'
  return '-'
}

function approveStatusType(status) {
  if (status === 2 || status === '2') return 'success'
  if (status === 3 || status === '3') return 'danger'
  return 'info'
}

// ========== 审核通过 ==========
function handlePass(row) {
  ElMessageBox.confirm('确定审核通过该头像吗？', '确认', { confirmButtonText: '确定通过', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try { await passHeadSculptureApprove(row.id); ElMessage.success('审核通过'); loadTableData() } catch { }
    }).catch(() => {})
}

// ========== 审核驳回 ==========
const rejectVisible = ref(false)
const rejectLoading = ref(false)
const rejectFormRef = ref(null)
const rejectForm = reactive({ id: null, userMainId: null, rejectText: '' })
const rejectRules = { rejectText: [{ required: true, message: '请输入驳回原因', trigger: 'blur' }] }

function handleReject(row) {
  rejectForm.id = row.id
  rejectForm.userMainId = row.userMainId
  rejectForm.rejectText = ''
  rejectVisible.value = true
}

async function handleRejectSubmit() {
  const valid = await rejectFormRef.value.validate().catch(() => false)
  if (!valid) return
  rejectLoading.value = true
  try {
    await rejectHeadSculptureApprove({ ...rejectForm })
    ElMessage.success('已驳回')
    rejectVisible.value = false
    loadTableData()
  } catch { } finally { rejectLoading.value = false }
}

loadTableData()
</script>

<style lang="scss" scoped>
.head-sculpture-approve-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
