<template>
  <div class="message-type-management">
    <h2 class="page-title">消息类型管理</h2>

    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="searchForm.code" placeholder="请输入编码" clearable style="width:180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'toucan:content:messageType:toolbar:save'" @click="handleAdd">新增</el-button>
        <el-button :icon="RefreshRight" v-permission="'toucan:content:messageType:btn:flushCache'" @click="handleFlushCache">刷新缓存</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="id" label="主键" width="200" show-overflow-tooltip />
        <el-table-column prop="code" label="类型编码" width="160" />
        <el-table-column prop="name" label="类型名称" width="160" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:content:messageType:btn:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:content:messageType:delete'" @click="handleDelete(row)">删除</el-button>
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
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" v-loading="dialogLoading">
        <el-form-item label="类型编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码（字母、数字、下划线，1-50位）" maxlength="50" />
        </el-form-item>
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" maxlength="25" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Search, RefreshRight } from '@element-plus/icons-vue'
import { listMessageType, addMessageType, updateMessageType, delMessageType, flushMessageTypeCache } from '@/api/message/messageType'

// ========== 搜索 ==========
const searchForm = reactive({ name: '', code: '' })

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() { searchForm.name = ''; searchForm.code = ''; pagination.page = 1; fetchData() }

// ========== 分页 ==========
const pagination = reactive({ page: 1, size: 15 })
const tableTotal = ref(0)

function handlePageChange(page) { pagination.page = page; fetchData() }
function handleSizeChange(size) { pagination.size = size; pagination.page = 1; fetchData() }

// ========== 表格 ==========
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])

function onSelectionChange(rows) { selectedRows.value = rows }

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      limit: pagination.size,
      name: searchForm.name || undefined,
      code: searchForm.code || undefined
    }
    const res = await listMessageType(params)
    tableData.value = res.data || []
    tableTotal.value = res.count || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => { fetchData() })

// ========== 新增/编辑 ==========
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => isEdit.value ? '编辑消息类型' : '新增消息类型')

const formData = reactive({ code: '', name: '' })

const formRules = {
  code: [{ required: true, message: '请输入类型编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入类型名称', trigger: 'blur' }]
}

function resetForm() {
  formData.code = ''; formData.name = ''
}

function handleAdd() {
  isEdit.value = false; editingId.value = null
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true; editingId.value = row.id
  formData.code = row.code; formData.name = row.name
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const data = { code: formData.code, name: formData.name }
    if (isEdit.value) {
      await updateMessageType({ id: editingId.value, ...data })
      ElMessage.success('编辑成功')
    } else {
      await addMessageType(data)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false; resetForm(); fetchData()
  } catch { } finally { submitLoading.value = false }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm(`确定删除消息类型「${row.name}」吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delMessageType(row.id); ElMessage.success('删除成功'); fetchData() } catch { }
  }).catch(() => {})
}

// ========== 刷新缓存 ==========
function handleFlushCache() {
  ElMessageBox.confirm('确定刷新消息类型缓存吗？', '刷新缓存确认', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
  }).then(async () => {
    try { await flushMessageTypeCache(); ElMessage.success('缓存刷新成功') } catch { }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.message-type-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card {
    .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrapper { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
