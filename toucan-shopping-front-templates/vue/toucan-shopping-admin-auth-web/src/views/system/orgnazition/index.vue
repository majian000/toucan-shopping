<template>
  <div class="org-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="所属应用">
          <el-input v-model="searchForm.appCode" placeholder="请输入应用编码" clearable style="width:220px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" v-permission="'pms:system:org:list'" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" v-permission="'pms:system:org:list'" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'pms:system:org:add'" @click="handleAdd">添加</el-button>
        <el-button type="danger" :icon="Delete" v-permission="'pms:system:org:delete'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">删除</el-button>
        <el-button :icon="Refresh" @click="handleRefresh">刷新</el-button>
      </div>

      <!-- 树形表格 -->
      <el-table
        :key="tableKey"
        :data="orgTree"
        border stripe row-key="id"
        :tree-props="{ children: 'children' }"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width:100%"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="名称" min-width="260" />
        <el-table-column prop="code" label="编码" width="180" show-overflow-tooltip />
        <el-table-column prop="appNames" label="应用名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="orgnazitionSort" label="排序" width="80" align="center" />
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createAdminUsername" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminUsername" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'pms:system:org:edit'" @click="handleEdit(row)">修改</el-button>
            <el-button type="success" link size="small" :icon="Plus"
              v-permission="'pms:system:org:add'" @click="handleAddChild(row)">添加子机构</el-button>
            <el-button type="danger" link size="small" :icon="Delete"
              v-permission="'pms:system:org:delete'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px"
      :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级机构" prop="pid">
          <el-input v-if="isAddChild" :model-value="parentNodeName" disabled />
          <el-tree-select v-else v-model="formData.pid"
            :key="treeSelectKey"
            :data="fullOrgTree"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择上级机构（留空为顶级）" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="排序" prop="orgnazitionSort">
          <el-input-number v-model="formData.orgnazitionSort" :min="0" :max="9999" style="width:160px" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Refresh, Search } from '@element-plus/icons-vue'
import { listOrgnazitionTree, addOrgnazition, updateOrgnazition, delOrgnazition, delBatchOrgnazition } from '@/api/system/orgnazition'

const route = useRoute()

// ========== 搜索 ==========
const searchForm = reactive({ name: '', appCode: '' })

function handleSearch() {
  fetchData()
}

function handleReset() {
  searchForm.name = ''
  searchForm.appCode = ''
  fetchData()
}

// ========== 树形数据转换 ==========
function transformTree(tree) {
  return tree.map(node => ({
    ...node,
    children: node.children ? transformTree(node.children) : undefined
  }))
}

// ========== 树形表格数据 ==========
const orgTree = ref([])
const tableKey = ref(0)
const loading = ref(false)
const fullOrgTree = ref([])

async function fetchData() {
  loading.value = true
  try {
    const params = {}
    if (searchForm.name) params.name = searchForm.name
    if (searchForm.appCode) params.appCode = searchForm.appCode
    const res = await listOrgnazitionTree(params)
    orgTree.value = transformTree(res.data || [])
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchData())

async function handleRefresh() { await fetchData(); tableKey.value++ }

// ========== 批量删除 ==========
const selectedRows = ref([])

function handleSelectionChange(rows) {
  selectedRows.value = rows
}

async function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要操作的记录')
    return
  }
  try {
    await ElMessageBox.confirm('确定删除选中的组织机构?', '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })
  } catch {
    return
  }
  loading.value = true
  try {
    await delBatchOrgnazition(selectedRows.value)
    ElMessage.success('删除成功')
    selectedRows.value = []
    await fetchData()
    tableKey.value++
  } finally {
    loading.value = false
  }
}

// ========== 加载完整组织树（弹窗打开时调用，复用表格数据） ==========
function loadFullTree() {
  fullOrgTree.value = orgTree.value
}

// ========== 新增/编辑 ==========
const dialogVisible = ref(false)
const treeSelectKey = ref(0)
const isEdit = ref(false)
const isAddChild = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑机构'
  if (isAddChild.value) return '添加子机构'
  return '新增机构'
})

const parentNodeName = computed(() => {
  if (!isAddChild.value || !formData.pid) return ''
  const found = findNodeById(fullOrgTree.value, formData.pid)
  return found ? found.name : ''
})

function findNodeById(tree, id) {
  for (const n of tree) {
    if (n.id === id) return n
    if (n.children && n.children.length) {
      const found = findNodeById(n.children, id)
      if (found) return found
    }
  }
  return null
}

function findParentById(tree, id, parent = null) {
  for (const n of tree) {
    if (n.id === id) return parent
    if (n.children && n.children.length) {
      const found = findParentById(n.children, id, n)
      if (found !== undefined) return found
    }
  }
  return undefined
}

const formData = reactive({
  pid: null, name: '', code: '', orgnazitionSort: 0, remark: ''
})

const formRules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  orgnazitionSort: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
}

function resetForm() {
  formData.pid = null; formData.name = ''; formData.code = ''
  formData.orgnazitionSort = 0; formData.remark = ''
}

async function handleAdd() {
  isEdit.value = false; isAddChild.value = false; editingId.value = null
  resetForm(); await loadFullTree(); treeSelectKey.value++; dialogVisible.value = true
}

async function handleAddChild(row) {
  isEdit.value = false; isAddChild.value = true; editingId.value = null
  resetForm()
  formData.pid = row.id
  await loadFullTree(); treeSelectKey.value++; dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true; isAddChild.value = false; editingId.value = row.id
  formData.pid = null
  formData.name = row.name
  formData.code = row.code || ''
  formData.orgnazitionSort = row.orgnazitionSort
  formData.remark = row.remark || ''
  await loadFullTree(); treeSelectKey.value++
  dialogVisible.value = true
  const pid = findParentById(fullOrgTree.value, row.id)?.id || null
  if (pid) {
    nextTick(() => {
      formData.pid = pid
    })
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const apiData = {
      pid: formData.pid,
      name: formData.name,
      code: formData.code,
      orgnazitionSort: formData.orgnazitionSort,
      remark: formData.remark
    }
    if (isEdit.value) { await updateOrgnazition({ id: editingId.value, ...apiData }); ElMessage.success('编辑成功') }
    else { await addOrgnazition(apiData); ElMessage.success('新增成功') }
    dialogVisible.value = false
    resetForm()
    await fetchData()
    tableKey.value++
  } finally { submitLoading.value = false }
}

// ========== 删除 ==========
function handleDelete(row) {
  const hasChildren = row.children && row.children.length > 0
  const msg = hasChildren
    ? `「${row.name}」下有子机构，删除后子机构也将被删除，确认删除吗？`
    : `确认删除机构「${row.name}」吗？删除后不可恢复。`

  ElMessageBox.confirm(msg, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delOrgnazition(row.id); ElMessage.success('删除成功'); await fetchData(); tableKey.value++ }
    catch { /* handled */ }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.org-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
  .table-card {
    .toolbar { margin-bottom: $gap-md; }
  }
  :deep(.el-table) th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
}
</style>
