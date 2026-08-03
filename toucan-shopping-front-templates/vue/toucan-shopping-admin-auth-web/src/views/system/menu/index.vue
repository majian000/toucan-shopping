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
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
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
        <el-form-item label="功能链接" prop="url" v-if="formData.type !== 0">
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
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量添加弹窗 -->
    <el-dialog v-model="batchDialogVisible" title="批量添加菜单" width="750px"
      :close-on-click-modal="false" destroy-on-close>
      <el-form ref="batchFormRef" :model="batchForm" :rules="batchRules" label-width="100px">
        <el-form-item label="上级菜单" prop="pid">
          <el-tree-select v-model="batchForm.pid"
            :key="batchTreeKey"
            :data="fullMenuTree"
            :loading="treeLoading"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择上级功能" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item label="批量数据" prop="batchText">
          <el-input v-model="batchForm.batchText" type="textarea" :rows="10"
            placeholder="每行一条，格式：名称|类型|链接|权限标识|图标|功能内容|排序|状态&#10;类型：0目录 1菜单 2操作按钮 3工具条按钮 4API 5页面控件&#10;状态：1启用 0禁用&#10;&#10;例：&#10;用户列表|1|/user/listPage|pms:system:user|fa-user||1|1&#10;添加按钮|3||pms:system:user:add||&lt;button&gt;添加&lt;/button&gt;|2|1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchSubmit" :loading="batchLoading">批量保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
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
const treeSelectKey = ref(0)
const isEdit = ref(false)
const editingId = ref(null)
const editingFunctionId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => isEdit.value ? '编辑功能项' : '添加功能项')

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
  await loadFullTree(); treeSelectKey.value++; dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true; editingId.value = row.id; editingFunctionId.value = row.functionId
  formData.type = row.type; formData.name = row.name; formData.url = row.url || ''
  formData.permission = row.permission || ''; formData.icon = row.icon || ''
  formData.functionText = row.functionText || ''; formData.functionSort = row.functionSort
  formData.enableStatus = row.enableStatus
  await loadFullTree()
  // 找父节点
  const findParent = (tree, id, parent) => {
    for (const n of tree) {
      if (n.id === id) return parent
      if (n.children?.length) {
        const found = findParent(n.children, id, n)
        if (found !== undefined) return found
      }
    }
    return undefined
  }
  formData.pid = findParent(fullMenuTree.value, row.id)?.id || null
  treeSelectKey.value++
  await nextTick()
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const apiData = {
      pid: formData.pid, type: formData.type, name: formData.name,
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
const batchFormRef = ref(null)
const batchForm = reactive({ pid: null, batchText: '' })
const batchRules = { batchText: [{ required: true, message: '请输入批量数据', trigger: 'blur' }] }

async function handleBatchAdd() {
  batchForm.pid = null; batchForm.batchText = ''
  await loadFullTree(); batchTreeKey.value++; batchDialogVisible.value = true
}

async function handleBatchSubmit() {
  const valid = await batchFormRef.value.validate().catch(() => false)
  if (!valid) return
  batchLoading.value = true
  try {
    const lines = batchForm.batchText.trim().split('\n').filter(l => l.trim())
    const items = []
    for (const line of lines) {
      const parts = line.split('|')
      if (parts.length < 6) continue
      items.push({
        pid: batchForm.pid || -1,
        name: parts[0].trim(),
        type: parseInt(parts[1].trim()),
        url: parts[2].trim(),
        permission: parts[3].trim(),
        icon: parts[4].trim(),
        functionText: parts.length > 5 ? parts[5].trim() : '',
        functionSort: parseInt(parts.length > 6 ? parts[6].trim() : '0') || 0,
        enableStatus: parts.length > 7 ? parseInt(parts[7].trim()) : 1,
        appCode: selectedAppCode.value
      })
    }
    if (items.length === 0) { ElMessage.warning('没有有效数据'); return }
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
