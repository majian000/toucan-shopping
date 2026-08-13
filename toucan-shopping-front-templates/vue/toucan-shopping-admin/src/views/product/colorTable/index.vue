<template>
  <div class="color-table">
    <h2 class="page-title">颜色表管理</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="颜色名称">
          <el-input v-model="searchForm.name" placeholder="请输入颜色名称" clearable style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">添加</el-button>
        <el-button type="danger" :icon="Delete" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="id" label="主键" width="120" />
        <el-table-column prop="name" label="颜色名称" min-width="140" />
        <el-table-column prop="rgbColor" label="颜色值(RGB)" width="140" />
        <el-table-column label="颜色预览" width="110" align="center">
          <template #default="{ row }">
            <div :style="{ backgroundColor: row.rgbColor, width: '35px', height: '35px', border: '1px solid #ddd' }" />
          </template>
        </el-table-column>
        <el-table-column prop="createAdminName" label="创建人" width="110" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminName" label="修改人" width="110" show-overflow-tooltip />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="130" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSizeChange" @current-change="loadTableData"
        />
      </div>
    </el-card>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogMode === 'add' ? '添加颜色表' : '编辑颜色表'" width="480px" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="颜色名称" required>
          <el-input v-model="form.name" placeholder="请输入颜色名称" maxlength="30" />
        </el-form-item>
        <el-form-item label="选择颜色" required>
          <el-color-picker v-model="form.rgbColor" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="form.remark" type="textarea" :rows="3" maxlength="255" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Plus, Delete } from '@element-plus/icons-vue'
import { listColorTable, saveColorTable, updateColorTable, deleteColorTable, deleteColorTableByIds } from '@/api/product/colorTable'

const searchForm = reactive({ name: '' })

const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listColorTable(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.name = ''; handleSearch() }

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该颜色表?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteColorTable({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要操作的记录'); return }
  ElMessageBox.confirm('确定删除选中的颜色表?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await deleteColorTableByIds(selectedRows.value.map(r => ({ id: r.id })))
      ElMessage.success('删除成功')
      loadTableData()
    } catch { }
  }).catch(() => {})
}

const dialogVisible = ref(false)
const dialogMode = ref('add')
const saving = ref(false)
const form = reactive({ id: null, name: '', rgbColor: '#000000', remark: '' })

function handleAdd() {
  dialogMode.value = 'add'
  Object.assign(form, { id: null, name: '', rgbColor: '#000000', remark: '' })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  Object.assign(form, { id: row.id, name: row.name, rgbColor: row.rgbColor || '#000000', remark: row.remark || '' })
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.name) { ElMessage.warning('请输入颜色名称'); return }
  if (!form.rgbColor) { ElMessage.warning('请选择颜色'); return }
  const payload = { name: form.name, rgbColor: form.rgbColor, remark: form.remark }
  if (dialogMode.value === 'edit') payload.id = form.id
  saving.value = true
  try {
    if (dialogMode.value === 'add') await saveColorTable(payload)
    else await updateColorTable(payload)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadTableData()
  } catch { } finally { saving.value = false }
}

loadTableData()
</script>

<style lang="scss" scoped>
.color-table {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
