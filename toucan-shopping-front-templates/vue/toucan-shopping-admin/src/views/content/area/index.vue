<template>
  <div class="area-management">
    <h2 class="page-title">地区列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="searchForm.code" placeholder="请输入编码" clearable style="width:160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">添加地区</el-button>
        <el-button type="danger" :icon="Delete" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="warning" :icon="Refresh" @click="handleFlushCache">刷新全部缓存</el-button>
      </div>
      <el-table
        ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id"
        lazy :load="loadChildren" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="name" label="名称" width="200" />
        <el-table-column prop="code" label="编码" width="120" />
        <el-table-column prop="areaSort" label="排序" width="80" align="center" />
        <el-table-column prop="isMunicipality" label="直辖市" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isMunicipality === 1 ? 'warning' : 'info'" size="small">
              {{ row.isMunicipality === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="countryName" label="国家" width="120" />
        <el-table-column prop="countryCode" label="国家编码" width="100" />
        <el-table-column prop="bigAreaName" label="大区名称" width="120" />
        <el-table-column prop="bigAreaCode" label="大区编码" width="100" />
        <el-table-column prop="createAdminName" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminName" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px" v-loading="dialogLoading">
        <el-form-item label="上级地区">
          <el-tree-select
            v-model="formData.pid" :data="parentAreaTree"
            :props="{ children: 'children', label: 'name', value: 'id', isLeaf: 'leaf' }"
            check-strictly clearable placeholder="请选择上级地区（不选则为根节点）" style="width:100%"
            :load="loadParentAreaTree" lazy node-key="id"
          />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" maxlength="100" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码" maxlength="50" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-input v-model="formData.type" placeholder="请输入类型" maxlength="20" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.areaSort" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="是否直辖市">
          <el-switch v-model="formData.isMunicipality" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="国家编码">
          <el-input v-model="formData.countryCode" placeholder="请输入国家编码" maxlength="50" />
        </el-form-item>
        <el-form-item label="大区编码">
          <el-input v-model="formData.bigAreaCode" placeholder="请输入大区编码" maxlength="50" />
        </el-form-item>
        <el-form-item label="父编码">
          <el-input v-model="formData.parentCode" placeholder="请输入父编码" maxlength="50" />
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
import { Plus, Delete, Edit, Search, RefreshRight, Refresh } from '@element-plus/icons-vue'
import { queryAreaTreeTable, queryAreaTree, saveArea, updateArea, deleteArea, deleteAreas, flushAllAreaCache } from '@/api/content/area'

const searchForm = reactive({ name: '', code: '' })
function handleSearch() { loadRootData() }
function handleReset() { searchForm.name = ''; searchForm.code = '' }

const tableData = ref([]); const loading = ref(false); const selectedRows = ref([])
function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, pid: -1 }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadRootData() {
  loading.value = true
  try { const res = await queryAreaTreeTable(buildSearchParams()); tableData.value = (res.data || []).map(item => ({ ...item, hasChildren: true })) } catch { } finally { loading.value = false }
}

async function loadChildren(row, treeNode, resolve) {
  try { const params = { ...searchForm, pid: row.id }; const res = await queryAreaTreeTable(params); resolve((res.data || []).map(item => ({ ...item, hasChildren: true }))) } catch { resolve([]) }
}

const dialogVisible = ref(false); const dialogLoading = ref(false); const isEdit = ref(false)
const submitLoading = ref(false); const formRef = ref(null)
const parentAreaTree = ref([])
const dialogTitle = computed(() => isEdit.value ? '编辑地区' : '添加地区')

const formData = reactive({ id: null, pid: null, name: '', code: '', type: '', areaSort: 0, isMunicipality: 0, countryCode: '', bigAreaCode: '', parentCode: '' })
const formRules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入编码', trigger: 'blur' }],
  type: [{ required: true, message: '请输入类型', trigger: 'blur' }]
}

function resetForm() {
  formData.id = null; formData.pid = null; formData.name = ''; formData.code = ''; formData.type = ''
  formData.areaSort = 0; formData.isMunicipality = 0; formData.countryCode = ''; formData.bigAreaCode = ''; formData.parentCode = ''
}

async function loadParentAreaRoots() {
  try {
    const res = await queryAreaTreeTable({ pid: -1 })
    parentAreaTree.value = (res.data || []).map(item => ({ ...item, leaf: false }))
  } catch { }
}

async function loadParentAreaTree(node, resolve) {
  try { const res = await queryAreaTreeTable({ pid: node?.id || -1 }); resolve((res.data || []).map(item => ({ ...item, leaf: false }))) } catch { resolve([]) }
}

async function openDialog(title, row) {
  dialogVisible.value = true
  dialogLoading.value = true
  parentAreaTree.value = []
  if (row) {
    isEdit.value = true
    formData.id = row.id; formData.pid = row.pid; formData.name = row.name || ''; formData.code = row.code || ''
    formData.type = row.type || ''; formData.areaSort = row.areaSort || 0
    formData.isMunicipality = row.isMunicipality || 0; formData.countryCode = row.countryCode || ''
    formData.bigAreaCode = row.bigAreaCode || ''; formData.parentCode = row.parentCode || ''
  } else {
    isEdit.value = false
    resetForm()
  }
  await loadParentAreaRoots()
  dialogLoading.value = false
}

function handleAdd() { openDialog() }
function handleEdit(row) { openDialog(null, row) }

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false); if (!valid) return
  submitLoading.value = true
  try { if (isEdit.value) { await updateArea({ ...formData }) } else { await saveArea({ ...formData }) }; ElMessage.success(isEdit.value ? '修改成功' : '添加成功'); dialogVisible.value = false; loadRootData() } catch { } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该地区吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteArea({ id: row.id }); ElMessage.success('删除成功'); loadRootData() } catch { } }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个地区吗？`, '批量删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteAreas(selectedRows.value); ElMessage.success('批量删除成功'); loadRootData() } catch { } }).catch(() => {})
}

function handleFlushCache() {
  ElMessageBox.confirm('确定刷新全部数据缓存?', '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { const res = await flushAllAreaCache(); ElMessage.success(res.msg || '刷新成功') } catch { } }).catch(() => {})
}

loadRootData()
</script>

<style lang="scss" scoped>
.area-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
