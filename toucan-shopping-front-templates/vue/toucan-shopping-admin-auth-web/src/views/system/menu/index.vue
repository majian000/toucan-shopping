<template>
  <div class="menu-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="功能ID">
          <el-input v-model="searchForm.functionId" placeholder="请输入功能ID" clearable style="width:240px" />
        </el-form-item>
        <el-form-item label="功能名称">
          <el-input v-model="searchForm.name" placeholder="请输入功能名称" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="链接地址">
          <el-input v-model="searchForm.url" placeholder="请输入链接地址" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="searchForm.permission" placeholder="请输入权限标识" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select v-model="searchForm.enableStatus" placeholder="请选择" clearable style="width:120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 树形表格 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <span class="app-select-label">已选应用：</span>
          <el-select
            v-model="selectedAppCode"
            placeholder="请选择应用"
            style="width:240px"
            @change="handleAppChange"
            :loading="appLoading"
          >
            <el-option
              v-for="a in appOptions"
              :key="a.code"
              :label="a.code + ' ' + a.name"
              :value="a.code"
            />
          </el-select>
          <el-button type="primary" :icon="Plus" v-permission="'pms:system:menu:add'" :disabled="!selectedAppCode" @click="handleAdd">添加</el-button>
          <el-button type="success" :icon="Plus" v-permission="'pms:system:menu:batch-add'" :disabled="!selectedAppCode" @click="handleBatchAdd">批量添加</el-button>
        </div>
      </div>

      <el-table
        :key="tableKey"
        :data="menuTree"
        border stripe row-key="id" lazy
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :load="loadChildren"
        v-loading="loading"
        style="width:100%"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="功能名称" min-width="200" />
        <el-table-column prop="url" label="链接" width="200" show-overflow-tooltip />
        <el-table-column prop="permission" label="权限" width="200" show-overflow-tooltip />
        <el-table-column prop="icon" label="图标" width="270" />
        <el-table-column prop="functionSort" label="排序" width="70" align="center" sortable />
        <el-table-column label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="typeTagMap[row.type]" size="small">{{ typeLabelMap[row.type] || '应用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createAdminUsername" label="创建人" width="100" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminUsername" label="修改人" width="100" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'pms:system:menu:edit-row'" @click="handleEdit(row)">修改</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'pms:system:menu:delete-row'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px"
      :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" v-loading="dialogLoading" element-loading-text="加载中...">
        <el-form-item label="上级功能" prop="pid">
          <el-tree-select v-model="formData.pid"
            :key="treeSelectKey"
            :data="fullMenuTree"
            :loading="treeLoading"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择上级功能（留空为顶级）" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item label="功能名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入功能名称" maxlength="100" />
        </el-form-item>
        <el-form-item label="功能链接" prop="url">
          <el-input v-model="formData.url" placeholder="请输入功能链接" maxlength="500" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="formData.permission" placeholder="请输入功能权限标识" maxlength="255" />
        </el-form-item>
        <el-form-item label="功能类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio :value="0">目录</el-radio>
            <el-radio :value="1">菜单</el-radio>
            <el-radio :value="2">操作按钮</el-radio>
            <el-radio :value="3">工具条按钮</el-radio>
            <el-radio :value="4">API</el-radio>
            <el-radio :value="5">页面控件</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="功能内容" prop="functionText" v-if="formData.type !== 0 && formData.type !== 1">
          <el-input v-model="formData.functionText" type="textarea" :rows="4" placeholder="功能内容(按钮HTML片段等)" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="formData.icon" placeholder="请输入功能图标" maxlength="60" />
        </el-form-item>
        <el-form-item label="排序" prop="functionSort">
          <el-input-number v-model="formData.functionSort" :min="0" :max="9999" style="width:160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.enableStatus">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading" :disabled="dialogLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量添加弹窗 -->
    <el-dialog v-model="batchDialogVisible" title="批量添加菜单" width="90%"
      :close-on-click-modal="false" destroy-on-close>
      <el-form-item label="上级功能" style="margin-bottom:16px">
        <el-tree-select v-model="batchForm.pid"
          :key="batchTreeKey"
          :data="fullMenuTree"
          :loading="treeLoading"
          :props="{ label: 'name', value: 'id', children: 'children' }"
          placeholder="请选择上级功能" check-strictly clearable style="width:320px" />
      </el-form-item>
      <div class="batch-table-wrapper">
        <div style="margin-bottom:8px">
          <el-button type="primary" size="small" @click="batchAddRow">添加行</el-button>
        </div>
        <el-table :data="batchRows" border size="small" max-height="400">
          <el-table-column label="功能名称" width="150">
            <template #default="{ row }">
              <el-input v-model="row.name" placeholder="请输入" size="small" maxlength="100" />
            </template>
          </el-table-column>
          <el-table-column label="功能链接" width="150">
            <template #default="{ row }">
              <el-input v-model="row.url" placeholder="请输入" size="small" maxlength="500" />
            </template>
          </el-table-column>
          <el-table-column label="权限标识" width="160">
            <template #default="{ row }">
              <el-input v-model="row.permission" placeholder="请输入" size="small" maxlength="255" />
            </template>
          </el-table-column>
          <el-table-column label="功能类型" width="120">
            <template #default="{ row }">
              <el-select v-model="row.type" size="small" style="width:100%">
                <el-option label="目录" :value="0" />
                <el-option label="菜单" :value="1" />
                <el-option label="操作按钮" :value="2" />
                <el-option label="工具条按钮" :value="3" />
                <el-option label="API" :value="4" />
                <el-option label="页面控件" :value="5" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="功能内容" width="200">
            <template #default="{ row }">
              <el-input v-model="row.functionText" type="textarea" :rows="1" placeholder="请输入" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="图标" width="120">
            <template #default="{ row }">
              <el-input v-model="row.icon" placeholder="请输入" size="small" maxlength="60" />
            </template>
          </el-table-column>
          <el-table-column label="排序" width="80">
            <template #default="{ row }">
              <el-input-number v-model="row.functionSort" :min="0" size="small" controls-position="right" style="width:100%" />
            </template>
          </el-table-column>
          <el-table-column label="状态" width="90">
            <template #default="{ row }">
              <el-select v-model="row.enableStatus" size="small" style="width:100%">
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="备注信息" width="200">
            <template #default="{ row }">
              <el-input v-model="row.remark" type="textarea" :rows="1" placeholder="请输入" size="small" maxlength="255" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" fixed="right">
            <template #default="{ $index }">
              <el-button type="danger" size="small" @click="batchRemoveRow($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchSubmit" :loading="batchLoading">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Refresh, Search } from '@element-plus/icons-vue'
import { listMenuByPid, listMenuSimpleTree, addMenu, updateMenu, delMenu, batchAddMenus } from '@/api/system/menu'
import { listApp } from '@/api/system/app'

const route = useRoute()

// 类型映射
const typeLabelMap = { 0: '目录', 1: '菜单', 2: '操作按钮', 3: '工具条按钮', 4: 'API', 5: '页面控件' }
const typeTagMap = { 0: '', 1: 'success', 2: 'info', 3: 'info', 4: 'warning', 5: '' }

function transformTypes(tree) {
  if (!tree) return []
  return tree.map(node => ({
    ...node,
    hasChildren: node.haveChild === true || node.haveChild === 'true' || (node.children && node.children.length > 0),
    children: node.children && node.children.length > 0 ? transformTypes(node.children) : undefined
  }))
}

// ========== 应用选择 ==========
const appOptions = ref([])
const appLoading = ref(false)
const selectedAppCode = ref('')

async function loadApps() {
  appLoading.value = true
  try {
    const res = await listApp({ page: 1, size: 1000 })
    appOptions.value = res.data || []
    if (appOptions.value.length > 0) {
      selectedAppCode.value = appOptions.value[0].code
    }
  } catch { /* ignore */ } finally { appLoading.value = false }
}

function handleAppChange() {
  fetchData()
  tableKey.value++
}

// ========== 树形菜单数据 ==========
const menuTree = ref([])
const tableKey = ref(0)
const loading = ref(false)
const fullMenuTree = ref([])
const treeLoading = ref(false)

async function fetchData(params = {}) {
  if (!selectedAppCode.value) { menuTree.value = []; return }
  loading.value = true
  try {
    const base = { pid: -1, appCode: selectedAppCode.value }
    const res = await listMenuByPid({ ...base, ...params })
    menuTree.value = transformTypes(res.data || [])
  } finally { loading.value = false }
}

async function loadFullTree() {
  if (!selectedAppCode.value) { fullMenuTree.value = []; return }
  treeLoading.value = true
  try {
    const res = await listMenuSimpleTree({ appCode: selectedAppCode.value })
    fullMenuTree.value = res.data || []
  } catch { /* ignore */ }
  finally { treeLoading.value = false }
}

onMounted(() => { loadApps().then(() => fetchData()) })

function handleRefresh() { fetchData(); tableKey.value++ }

async function loadChildren(row, _treeNode, resolve) {
  try {
    const res = await listMenuByPid({ pid: row.id, appCode: selectedAppCode.value })
    resolve(transformTypes(res.data || []))
  } catch { resolve([]) }
}

// ========== 搜索 ==========
const searchForm = reactive({ functionId: '', name: '', url: '', permission: '', enableStatus: '' })

function handleSearch() {
  const params = {}
  for (const k of Object.keys(searchForm)) {
    if (searchForm[k] !== '' && searchForm[k] != null) params[k] = searchForm[k]
  }
  fetchData(params)
  tableKey.value++
}

function handleReset() {
  searchForm.functionId = ''; searchForm.name = ''; searchForm.url = ''
  searchForm.permission = ''; searchForm.enableStatus = ''
  fetchData()
  tableKey.value++
}

// ========== 新增/编辑 ==========
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const treeSelectKey = ref(0)
const isEdit = ref(false)
const editingId = ref(null)
const editingFunctionId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => isEdit.value ? '编辑菜单' : '添加菜单')

const formData = reactive({ pid: null, type: 1, name: '', url: '', permission: '', icon: '', functionText: '', functionSort: 0, enableStatus: 1 })
const formRules = {
  name: [{ required: true, message: '请输入功能名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择功能类型', trigger: 'change' }],
  functionSort: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
}

function resetForm() {
  formData.pid = null; formData.type = 1; formData.name = ''; formData.url = ''
  formData.permission = ''; formData.icon = ''; formData.functionText = ''
  formData.functionSort = 0; formData.enableStatus = 1
}

async function handleAdd() {
  isEdit.value = false; editingId.value = null; resetForm()
  dialogVisible.value = true
  treeSelectKey.value++
  await loadFullTree()
}

async function handleEdit(row) {
  isEdit.value = true; editingId.value = row.id; editingFunctionId.value = row.functionId
  formData.type = row.type; formData.name = row.name; formData.url = row.url || ''
  formData.permission = row.permission || ''; formData.icon = row.icon || ''
  formData.functionText = row.functionText || ''; formData.functionSort = row.functionSort
  formData.enableStatus = row.enableStatus
  dialogLoading.value = true; dialogVisible.value = true
  formData.pid = (row.pid != null && row.pid !== -1) ? row.pid : null
  await loadFullTree()
  treeSelectKey.value++
  dialogLoading.value = false
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const apiData = {
      pid: formData.pid != null ? formData.pid : -1, type: formData.type, name: formData.name,
      url: formData.url, permission: formData.permission, icon: formData.icon,
      functionText: formData.functionText, functionSort: formData.functionSort,
      enableStatus: formData.enableStatus, appCode: selectedAppCode.value
    }
    if (isEdit.value) {
      apiData.id = editingId.value
      apiData.functionId = editingFunctionId.value
      await updateMenu(apiData)
      ElMessage.success('修改成功')
    } else {
      await addMenu(apiData)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    resetForm()
    fetchData(); tableKey.value++
  } finally { submitLoading.value = false }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm(`确认删除功能项「${row.name}」吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await delMenu(row.id, row.functionId)
      ElMessage.success('删除成功')
      fetchData(); tableKey.value++
    } catch { /* handled */ }
  }).catch(() => {})
}

// ========== 批量添加 ==========
const batchDialogVisible = ref(false)
const batchTreeKey = ref(0)
const batchLoading = ref(false)
const batchRows = ref([])
const batchForm = reactive({ pid: null })

function batchAddRow() {
  batchRows.value.push({ name: '', url: '', permission: '', type: 1, functionText: '', icon: '', functionSort: 0, enableStatus: 1, remark: '' })
}

function batchRemoveRow(index) {
  batchRows.value.splice(index, 1)
}

async function handleBatchAdd() {
  batchForm.pid = null
  batchRows.value = []
  batchAddRow()
  batchDialogVisible.value = true; batchTreeKey.value++
  await loadFullTree()
}

async function handleBatchSubmit() {
  if (batchRows.value.length === 0) { ElMessage.warning('请至少添加一行数据'); return }
  for (let i = 0; i < batchRows.value.length; i++) {
    const r = batchRows.value[i]
    if (!r.name) { ElMessage.warning(`第${i + 1}行的功能名称不能为空`); return }
    if (!r.functionSort && r.functionSort !== 0) { ElMessage.warning(`第${i + 1}行的排序不能为空`); return }
  }
  batchLoading.value = true
  try {
    const items = batchRows.value.map(r => ({
      pid: batchForm.pid || -1,
      name: r.name, type: r.type, url: r.url, permission: r.permission,
      functionText: r.functionText, icon: r.icon, functionSort: r.functionSort,
      enableStatus: r.enableStatus, remark: r.remark, appCode: selectedAppCode.value
    }))
    await batchAddMenus(items)
    ElMessage.success(`批量添加成功，共 ${items.length} 条`)
    batchDialogVisible.value = false
    fetchData(); tableKey.value++
  } finally { batchLoading.value = false }
}
</script>

<style lang="scss" scoped>
.menu-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
  .table-card {
    .toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: $gap-md; }
    .toolbar-left { display: flex; gap: $gap-sm; align-items: center; }
    .app-select-label { font-size: 14px; color: $text-secondary; white-space: nowrap; }
  }
  :deep(.el-table) th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
}
</style>
