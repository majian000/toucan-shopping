<template>
  <div class="role-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.name" placeholder="请输入角色名称" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.enableStatus" placeholder="请选择状态" clearable style="width:140px">
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" v-permission="'pms:system:role:list'" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" v-permission="'pms:system:role:list'" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'pms:system:role:add'" @click="handleAdd">新增角色</el-button>
      </div>

      <el-table :data="roles" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="角色名称" width="150" />
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" sortable />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="success" link size="small" :icon="Key" v-permission="'pms:system:role:permission'" @click="handleAssignPermission(row)">权限分配</el-button>
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'pms:system:role:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'pms:system:role:delete'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入角色名称" maxlength="20" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
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
      title="权限分配"
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
          :props="{ children: 'children', label: 'label' }"
          show-checkbox
          :default-checked-keys="currentPermKeys"
          check-strictly
          class="perm-tree"
          @check="onPermTreeCheck"
        >
          <template #default="{ node, data }">
            <span class="perm-tree-node">
              <span class="perm-node-label">{{ node.label }}</span>
              <span v-if="hasPermChildren(data)" class="perm-dot" :class="getPermDotClass(data)"></span>
              <template v-if="hasPermChildren(data)">
                <el-icon class="perm-select-all" title="全选子节点" @click.stop="handleNodeSelectAll(data)"><CircleCheck /></el-icon>
                <el-icon class="perm-deselect-all" title="取消全选" @click.stop="handleNodeDeselectAll(data)"><RemoveFilled /></el-icon>
              </template>
            </span>
          </template>
        </el-tree>
        <div class="perm-stat">
          已选择 <strong>{{ checkedPermCount }}</strong> 项权限
        </div>
      </div>
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePerm" :loading="permSaveLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, Key, CircleCheck, RemoveFilled } from '@element-plus/icons-vue'
import { listRole, addRole, updateRole, delRole, getRoleFunctionTree, saveRoleFunctions } from '@/api/system/role'

const route = useRoute()

// ========== 菜单树数据（用于权限分配） ==========
function transformMenuTree(tree) {
  return tree.map(node => ({
    ...node,
    label: node.name,
    children: node.children ? transformMenuTree(node.children) : undefined
  }))
}

const menuTreeData = ref([])

// 收集节点下所有后代ID
function collectDescendantIds(node) {
  const ids = []
  function walk(n) {
    if (n.children && n.children.length) {
      n.children.forEach(c => { ids.push(c.functionId); walk(c) })
    }
  }
  walk(node)
  return ids
}

// 判断节点是否有子节点
function hasPermChildren(node) {
  return !!(node.children && node.children.length > 0)
}

// 当前节点 + 所有后代都在 checkedKeys 中 → 绿点，否则红点
function getPermDotClass(data) {
  void permCheckTick.value
  if (!permTreeRef.value) return 'perm-partial'
  const checkedKeys = permTreeRef.value.getCheckedKeys()
  const descIds = collectDescendantIds(data)
  if (descIds.length === 0) return 'perm-partial'
  // 当前节点必须在，且所有后代都在
  if (!checkedKeys.includes(data.functionId)) return 'perm-partial'
  const allChecked = descIds.every(id => checkedKeys.includes(id))
  return allChecked ? 'perm-full' : 'perm-partial'
}

const permCheckTick = ref(0)
function onPermTreeCheck() {
  permCheckTick.value++
}

// ========== 收集树中所有节点ID ==========
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

// ========== 角色列表数据 ==========
const roles = ref([])
const tableTotal = ref(0)

// ========== 获取数据 ==========
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name || undefined,
      enableStatus: searchForm.enableStatus || undefined
    }
    const res = await listRole(params)
    roles.value = res.data || []
    tableTotal.value = res.count || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})

// ========== 搜索 ==========
const searchForm = reactive({ name: '', enableStatus: '' })

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() { searchForm.name = ''; searchForm.enableStatus = ''; pagination.page = 1; fetchData() }

// ========== 分页 ==========
const pagination = reactive({ page: 1, size: 10 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

// ========== 新增/编辑弹窗 ==========
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => isEdit.value ? '编辑角色' : '新增角色')

const formData = reactive({ name: '', remark: '', enableStatus: 1 })

const formRules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  remark: [{ required: false, message: '请输入备注', trigger: 'blur' }]
}

function resetForm() {
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
  formData.name = row.name
  formData.remark = row.remark
  formData.enableStatus = row.enableStatus
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
        name: formData.name,
        remark: formData.remark,
        enableStatus: formData.enableStatus
      })
      ElMessage.success('编辑成功')
    } else {
      await addRole({
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

// ========== 切换状态 ==========
async function handleToggleStatus(row, val) {
  const newStatus = val ? 1 : 0
  if (row.enableStatus === newStatus) return
  try {
    await updateRole({ id: row.id, enableStatus: newStatus })
    row.enableStatus = newStatus
    ElMessage.success(`已${val ? '启用' : '禁用'}`)
  } catch {
    // error handled by request interceptor
  }
}

// ========== 删除 ==========
function handleDelete(row) {
ElMessageBox.confirm(`确认删除角色"${row.name}"吗？删除后不可恢复。`, '删除确认', {
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

// ========== 权限分配 ==========
const permDialogVisible = ref(false)
const permTreeRef = ref(null)
const permTreeLoading = ref(false)
const currentPermKeys = ref([])
const permRoleId = ref(null)
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
  permDialogVisible.value = true
  permTreeLoading.value = true
  try {
    const res = await getRoleFunctionTree(row.roleId)
    const rawData = res.data || []
    currentPermKeys.value = collectCheckedIds(rawData)
    menuTreeData.value = transformMenuTree(rawData)
  } catch {
    // error handled by request interceptor
  } finally {
    permTreeLoading.value = false
  }
}

// 顶部全选/取消全选所有节点
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
  onPermTreeCheck()
}

// 勾选某节点及其所有后代
function selectDescendants(data) {
  const checkedKeys = permTreeRef.value.getCheckedKeys()
  const descIds = collectDescendantIds(data)
  // 包含当前节点自身
  const newKeys = [...new Set([...checkedKeys, data.functionId, ...descIds])]
  permTreeRef.value.setCheckedKeys(newKeys)
  onPermTreeCheck()
}

// 取消勾选某节点及其所有后代
function deselectDescendants(data) {
  const checkedKeys = permTreeRef.value.getCheckedKeys()
  const descIds = collectDescendantIds(data)
  // 包含当前节点自身
  const allIds = [data.functionId, ...descIds]
  const newKeys = checkedKeys.filter(id => !allIds.includes(id))
  permTreeRef.value.setCheckedKeys(newKeys)
  onPermTreeCheck()
}

function handleNodeSelectAll(data) {
  selectDescendants(data)
}

function handleNodeDeselectAll(data) {
  deselectDescendants(data)
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
    const functions = checkedKeys.map(functionId => ({ functionId }))
    await saveRoleFunctions({ roleId: permRoleId.value, functions })
    ElMessage.success('权限已更新')
    permDialogVisible.value = false
    fetchData()
  } catch {
    // error handled by request interceptor
  } finally {
    permSaveLoading.value = false
  }
}
</script>

<style lang="scss" scoped>
.role-management {
  .search-card {
    margin-bottom: $gap-md;
    :deep(.el-card__body) { padding: 16px 20px 0; }
  }

  .table-card {
    .toolbar { margin-bottom: $gap-md; }

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

      .perm-tree-node {
        display: flex;
        align-items: center;
        gap: 6px;
        flex: 1;
        .perm-node-label { flex: 1; }
        .perm-dot {
          width: 8px; height: 8px;
          border-radius: 50%;
          flex-shrink: 0;
          &.perm-full { background-color: #67c23a; }
          &.perm-partial { background-color: #f56c6c; }
        }
        .perm-select-all {
          cursor: pointer; color: #67c23a; font-size: 14px;
          &:hover { color: #529b2e; }
        }
        .perm-deselect-all {
          cursor: pointer; color: #f56c6c; font-size: 14px;
          &:hover { color: #c45656; }
        }
      }
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
