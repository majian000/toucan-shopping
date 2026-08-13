<template>
  <div class="column-type-management">
    <h2 class="page-title">栏目分类</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="searchForm.code" placeholder="请输入编码" clearable style="width:160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search"  v-permission="'toucan:content:columnType:list'" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight"   v-permission="'toucan:content:columnType:list'"  @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus"  v-permission="'toucan:content:columnType:toolbar:save'"  @click="handleAdd">添加栏目分类</el-button>
      </div>
      <el-table ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id">
        <el-table-column prop="id" label="ID" width="210" align="center" />
        <el-table-column prop="code" label="编码" width="200" />
        <el-table-column prop="name" label="名称" width="240" />
        <el-table-column prop="createAdminName" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminName" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:content:columnType:btn:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:content:columnType:delete'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSearch" @current-change="handleSearch"
        />
      </div>
    </el-card>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="90px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="字母、数字、下划线，长度1-50位" maxlength="50" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
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
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Search, RefreshRight } from '@element-plus/icons-vue'
import { listColumnType, saveColumnType, updateColumnType, deleteColumnType } from '@/api/content/columnType'

const searchForm = reactive({ name: '', code: '' })

function handleSearch() { pagination.pageNum = 1; loadTableData() }
function handleReset() { searchForm.name = ''; searchForm.code = '' }

const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ pageNum: 1, pageSize: 15, total: 0 })

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.pageNum, limit: pagination.pageSize }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listColumnType(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑栏目分类' : '添加栏目分类')

const formData = reactive({ id: null, code: '', name: '' })
const formRules = {
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]{1,50}$/, message: '编码只允许字母、数字、下划线，长度1-50位', trigger: 'blur' }
  ],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }]
}

function resetForm() {
  formData.id = null; formData.code = ''; formData.name = ''
}

function handleAdd() {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  formData.id = row.id
  formData.code = row.code || ''
  formData.name = row.name || ''
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateColumnType({ ...formData })
      ElMessage.success('修改成功')
    } else {
      await saveColumnType({ ...formData })
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadTableData()
  } catch { } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该栏目分类吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try { await deleteColumnType({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { }
    }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.column-type-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
