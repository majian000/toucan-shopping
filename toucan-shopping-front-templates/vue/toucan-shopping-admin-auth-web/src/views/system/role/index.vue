<template>
  <div class="role-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="角色ID">
          <el-input v-model="searchForm.roleId" placeholder="请输入角色ID" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.name" placeholder="请输入角色名称" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="所属应用">
          <el-select v-model="searchForm.appCode" placeholder="请选择" clearable style="width:220px">
            <el-option v-for="a in appOptions" :key="a.code" :label="a.code + ' ' + a.name" :value="a.code" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" v-permission="'pms:system:role:list'" @click="handleSearch">搜 索</el-button>
          <el-button :icon="Refresh" v-permission="'pms:system:role:list'" @click="handleReset">重 置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" :icon="Plus" v-permission="'pms:system:role:add'" @click="handleAdd">添加角色</el-button>
          <el-button type="danger" :icon="Delete" v-permission="'pms:system:role:delete'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        </div>
      </div>

      <el-table
        :data="roles"
        border
        stripe
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width:100%"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="roleId" label="角色ID" width="180" show-overflow-tooltip />
        <el-table-column prop="name" label="角色名称" width="180" />
        <el-table-column prop="appCode" label="应用编码" width="180" show-overflow-tooltip />
        <el-table-column prop="appName" label="所属应用" width="180" show-overflow-tooltip />
        <el-table-column prop="createAdminUsername" label="创建人" width="180" />
        <el-table-column prop="createDate" label="创建时间" width="180" sortable />
        <el-table-column prop="updateAdminUsername" label="修改人" width="180" />
        <el-table-column prop="updateDate" label="修改时间" width="180" sortable />
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === '1' || row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === '1' || row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="350" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'pms:system:role:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'pms:system:role:delete'" @click="handleDelete(row)">删除</el-button>
            <el-button type="warning" link size="small" :icon="Key" v-permission="'pms:system:role:permission'" @click="handleAssignPermission(row)">权限</el-button>
            <el-button type="success" link size="small" :icon="RefreshRight" v-permission="'pms:system:role:refresh-cache'" @click="handleRefreshCache(row)">刷新权限缓存</el-button>
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
          background
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="520px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="所属应用" prop="appCode">
          <el-select v-model="formData.appCode" placeholder="请选择应用" style="width:100%">
            <el-option v-for="a in appOptions" :key="a.code" :label="a.code + ' ' + a.name" :value="a.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入角色名称" maxlength="50" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" maxlength="255" />
        </el-form-item>
        <el-form-item label="状态" prop="enableStatus">
          <el-switch
            v-model="formData.enableStatus"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 权限分配弹窗 -->
    <el-dialog
      v-model="permDialogVisible"
      title="选择权限"
      width="560px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div class="perm-dialog-body" v-loading="permTreeLoading">
        <div class="perm-toolbar">
          <el-button size="small" @click="handleTreeCheckAll">全选</el-button>
          <el-button size="small" @click="handleTreeExpandAll">展开全部</el-button>
          <el-button size="small" @click="handleTreeCollapseAll">折叠全部</el-button>
        </div>
        <el-tree
          ref="permTreeRef"
          :data="menuTreeData"
          node-key="functionId"
          :props="{ children: 'children', label: 'name' }"
          show-checkbox
          :default-checked-keys="currentPermKeys"
          class="perm-tree"
        >
        </el-tree>
        <div class="perm-stat">
          已选择 <strong>{{ checkedPermCount }}</strong> 项权限
        </div>
      </div>
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePerm" :loading="permSaveLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, Key, RefreshRight } from '@element-plus/icons-vue'
import { listRole, addRole, updateRole, delRole, batchDelRole, getRoleFunctionTree, saveRoleFunctions, refreshRoleFunctionCache } from '@/api/system/role'
import { listApp } from '@/api/system/app'

const route = useRoute()

// ========== 应用列表 ==========
const appOptions = ref([])

async function fetchApps() {
  try {
    const res = await listApp({ page: 1, size: 1000 })
    appOptions.value = res.data || []
  } catch { /* ignore */ }
}

// ========== 角色列表数据 ==========
const roles = ref([])
const tableTotal = ref(0)
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      roleId: searchForm.roleId || undefined,
      name: searchForm.name || undefined,
      appCode: searchForm.appCode || undefined
    }
    const res = await listRole(params)
    roles.value = res.data || []
    tableTotal.value = res.count || 0
  } catch {
    // error handled by request interceptor
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchApps()
  fetchData()
})

// ========== 搜索 ==========
const searchForm = reactive({ roleId: '', name: '', appCode: '' })

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() {
  searchForm.roleId = ''
  searchForm.name = ''
  searchForm.appCode = ''
  pagination.page = 1
  fetchData()
}

// ========== 分页 ==========
const pagination = reactive({ page: 1, size: 15 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

// ========== 表格选择 ==========
const selectedRows = ref([])
function handleSelectionChange(selection) { selectedRows.value = selection }

// ========== 新增/编辑弹窗 ==========
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => isEdit.value ? '编辑角色' : '添加角色')

const formData = reactive({ appCode: '', name: '', remark: '', enableStatus: 1 })

const formRules = {
  appCode: [{ required: true, message: '请选择所属应用', trigger: 'change' }],
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  remark: [{ required: false, message: '请输入备注', trigger: 'blur' }]
}

function resetForm() {
  formData.appCode = ''
  formData.name = ''
  formData.remark = ''
  formData.enableStatus = 1
}

function handleAdd() {
  isEdit.value = false
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  editingId.value = row.id
  formData.appCode = row.appCode || ''
  formData.name = row.name
  formData.remark = row.remark || ''
  formData.enableStatus = row.enableStatus === '1' || row.enableStatus === 1 ? 1 : 0
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateRole({
        id: editingId.value,
        appCode: formData.appCode,
        name: formData.name,
        remark: formData.remark,
        enableStatus: formData.enableStatus
      })
      ElMessage.success('编辑成功')
    } else {
      await addRole({
        appCode: formData.appCode,
        name: formData.name,
        remark: formData.remark,
        enableStatus: formData.enableStatus
      })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    resetForm()
    fetchData()
  } catch {
    // error handled by request interceptor
  } finally {
    submitLoading.value = false
  }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm('确定删除该角色?', '删除确认', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await delRole(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch {
      // error handled by request interceptor
    }
  }).catch(() => {})
}

// ========== 批量删除 ==========
function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要操作的记录')
    return
  }
  ElMessageBox.confirm('确定删除角色?', '批量删除', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedRows.value.map(r => r.id)
      await batchDelRole(ids)
      selectedRows.value = []
      ElMessage.success('删除成功')
      fetchData()
    } catch {
      // error handled by request interceptor
    }
  }).catch(() => {})
}

// ========== 权限分配 ==========
const permDialogVisible = ref(false)
const permTreeRef = ref(null)
const permTreeLoading = ref(false)
const currentPermKeys = ref([])
const permRoleId = ref(null)
const permAppCode = ref('')
const permSaveLoading = ref(false)

const checkedPermCount = computed(() => {
  if (!permTreeRef.value) return 0
  return permTreeRef.value.getCheckedKeys().length
})

// 收集树中 checked 为 true 的节点ID
function collectCheckedIds(tree) {
  const ids = []
  function walk(nodes) {
    nodes.forEach(n => {
      if (n.checked === true || n.checked === 'true') ids.push(n.functionId)
      if (n.children && n.children.length) walk(n.children)
    })
  }
  walk(tree)
  return ids
}

async function handleAssignPermission(row) {
  permRoleId.value = row.roleId
  permAppCode.value = row.appCode
  permDialogVisible.value = true
  permTreeLoading.value = true
  try {
    const res = await getRoleFunctionTree(row.roleId, row.appCode)
    const rawData = res.data || []
    currentPermKeys.value = collectCheckedIds(rawData)
    menuTreeData.value = rawData
  } catch {
    // error handled by request interceptor
  } finally {
    permTreeLoading.value = false
  }
}

// ========== 权限树操作 ==========
// 收集树中所有节点ID
function collectAllNodeIds(tree) {
  const ids = []
  function walk(nodes) {
    nodes.forEach(n => {
      ids.push(n.functionId)
      if (n.children && n.children.length) walk(n.children)
    })
  }
  walk(tree)
  return ids
}

function handleTreeCheckAll() {
  if (!permTreeRef.value) return
  const currentKeys = permTreeRef.value.getCheckedKeys()
  const allIds = collectAllNodeIds(menuTreeData.value)
  const checkedCount = allIds.filter(id => currentKeys.includes(id)).length
  if (checkedCount >= allIds.length) {
    permTreeRef.value.setCheckedKeys([])
  } else {
    permTreeRef.value.setCheckedKeys(allIds)
  }
}

function handleTreeExpandAll() {
  const nodes = permTreeRef.value?.store?.nodesMap
  if (nodes) {
    Object.keys(nodes).forEach(k => {
      nodes[k].expanded = true
    })
  }
}

function handleTreeCollapseAll() {
  const nodes = permTreeRef.value?.store?.nodesMap
  if (nodes) {
    Object.keys(nodes).forEach(k => {
      nodes[k].expanded = false
    })
  }
}

async function handleSavePerm() {
  permSaveLoading.value = true
  try {
    const checkedKeys = permTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = permTreeRef.value.getHalfCheckedKeys()
    const allKeys = [...checkedKeys, ...halfCheckedKeys]
    const functions = allKeys.map(functionId => ({ functionId }))
    await saveRoleFunctions({ roleId: permRoleId.value, appCode: permAppCode.value, functions })
    ElMessage.success('权限已更新')
    permDialogVisible.value = false
    fetchData()
  } catch {
    // error handled by request interceptor
  } finally {
    permSaveLoading.value = false
  }
}

// ========== 刷新权限缓存 ==========
function handleRefreshCache(row) {
  ElMessageBox.confirm('确定刷新角色权限?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await refreshRoleFunctionCache(row.roleId)
      ElMessage.success('操作成功')
    } catch {
      // error handled by request interceptor
    }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.role-management {
  .search-card {
    margin-bottom: $gap-md;
    :deep(.el-card__body) { padding: 16px 20px 0; }
  }

  .table-card {
    .toolbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: $gap-md;
      .toolbar-left { display: flex; gap: $gap-sm; }
    }

    .pagination-wrapper {
      margin-top: $gap-md;
      display: flex;
      justify-content: flex-end;
    }
  }

  :deep(.el-table) {
    th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
  }

  .perm-dialog-body {
    .perm-toolbar {
      display: flex;
      gap: $gap-xs;
      margin-bottom: 12px;
      padding-bottom: 12px;
      border-bottom: 1px solid $border-light;
    }
    .perm-tree {
      max-height: 400px;
      overflow-y: auto;
    }
    .perm-stat {
      margin-top: 12px;
      padding-top: 12px;
      border-top: 1px solid $border-light;
      color: $text-secondary;
      strong { color: $primary; font-size: 16px; margin: 0 4px; }
    }
  }
}
</style>
