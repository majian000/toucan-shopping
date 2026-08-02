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
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="roleId" label="角色ID" width="180" show-overflow-tooltip />
        <el-table-column prop="name" label="角色名称" width="180" />
        <el-table-column label="所属应用" width="220">
          <template #default="{ row }">
            <el-tag v-if="row.appName" size="small" type="info">{{ row.appName }}</el-tag>
            <span v-else class="text-muted">--</span>
          </template>
        </el-table-column>
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
          node-key="functionId"
          :props="{ label: 'name', isLeaf: isPermLeaf }"
          show-checkbox
          check-strictly
          lazy
          :load="loadPermTree"
          class="perm-tree"
          @check="handlePermCheck"
        >
          <template #default="{ data }">
            <span class="perm-node">
              <el-icon class="perm-node-icon" :style="{ color: typeIcon[data.type]?.color }">
                <component :is="typeIcon[data.type]?.icon || Document" />
              </el-icon>
              <span class="perm-node-label">{{ data.name }}</span>
              <el-tag size="small" :type="typeIcon[data.type]?.tagType" class="perm-node-tag">
                {{ typeIcon[data.type]?.label || data.type }}
              </el-tag>
              <template v-if="data.descendantCount > 0">
                <el-tag
                  size="small"
                  :type="data.cascaded ? 'success' : 'info'"
                  class="perm-count-tag"
                >{{ data.checkedDescendantCount || 0 }}/{{ data.descendantCount }} 项</el-tag>
              </template>
              <el-icon
                v-if="data.isParent"
                class="perm-cascade-btn"
                :class="{ cascaded: data.cascaded }"
                :title="data.cascaded ? '取消级联' : '级联所有子节点'"
                @click.stop="handleCascadeToggle(data)"
              ><CircleCheck /></el-icon>
            </span>
          </template>
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
import { Plus, Search, Refresh, Delete, Edit, Key, RefreshRight, FolderOpened, Document, Pointer, Setting, Link, Grid, CircleCheck } from '@element-plus/icons-vue'
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
const permRoleId = ref(null)
const permAppCode = ref('')
const permSaveLoading = ref(false)

const checkedPermCount = computed(() => {
  if (!permTreeRef.value) return 0
  return permTreeRef.value.getCheckedKeys().length
})

// 功能项类型图标配置
const typeIcon = {
  0: { icon: FolderOpened, color: '#e6a23c', tagType: 'warning', label: '目录' },
  1: { icon: Document,     color: '#409eff', tagType: 'primary', label: '菜单' },
  2: { icon: Pointer,      color: '#67c23a', tagType: 'success', label: '按钮' },
  3: { icon: Setting,      color: '#e6a23c', tagType: 'warning', label: '工具条按钮' },
  4: { icon: Link,         color: '#f56c6c', tagType: 'danger',  label: 'API' },
  5: { icon: Grid,         color: '#909399', tagType: 'info',    label: '页面控件' }
}

// 根据后端返回的 isParent 判断是否有子节点
function isPermLeaf(data) {
  return !data.isParent
}

// ========== 懒加载 ==========
function loadPermTree(node, resolve) {
  const isRoot = node.level === 0
  const pid = node.data && node.data.id ? node.data.id : -1
  getRoleFunctionTree(permRoleId.value, permAppCode.value, pid).then(res => {
    const rawData = res.data || []
    // 父节点已级联 → 子节点继承级联状态（在 resolve 前修改数据）
    if (node.data && node.data.cascaded) {
      rawData.forEach(item => {
        item.checked = true
        item.cascaded = true
        item.checkedDescendantCount = item.descendantCount || 0
      })
    }
    resolve(rawData)
    nextTick(() => {
      nextTick(() => {
        rawData.forEach(item => {
          if (item.checked === true || item.checked === 'true') {
            permTreeRef.value?.setChecked(item.functionId, true, false)
          }
        })
        syncTreeState()
        if (isRoot) permTreeLoading.value = false
      })
    })
  }).catch(() => {
    resolve([])
    if (isRoot) permTreeLoading.value = false
  })
}

// ========== 操作入口（全部走 syncTreeState） ==========
function handlePermCheck() {
  nextTick(() => syncTreeState())
}

function handleCascadeToggle(data) {
  data.cascaded = !data.cascaded
  const node = permTreeRef.value?.store?.getNode(data.functionId)
  if (!node) return
  // 递归标记所有已加载子孙
  function walk(n) {
    n.childNodes.forEach(child => {
      child.setChecked(data.cascaded, false)
      if (child.data && child.data.descendantCount > 0) {
        child.data.cascaded = data.cascaded
        child.data.checkedDescendantCount = data.cascaded ? child.data.descendantCount : 0
      }
      walk(child)
    })
  }
  walk(node)
  node.setChecked(data.cascaded, false)
  // 未展开时显式覆盖，避免 syncTreeState 用后端旧值
  if (node.childNodes.length === 0 && data.descendantCount > 0) {
    data.checkedDescendantCount = data.cascaded ? data.descendantCount : (node.checked ? 1 : 0)
  }
  nextTick(() => syncTreeState())
}

// syncTreeState 中：遇到 data.cascaded=true 的节点，直接取 descendantCount
function syncTreeState() {
  const tree = permTreeRef.value
  if (!tree) return
  const checkedSet = new Set(tree.getCheckedKeys())
  function calc(node) {
    if (!node || !node.data) return
    node.childNodes.forEach(calc)
    // 未展开 → 保留后端的子孙统计，只修正自身勾选变化
    if (node.childNodes.length === 0 && node.data.descendantCount > 0) {
      const selfChecked = checkedSet.has(node.key) ? 1 : 0
      // 后端 checkedDescendantCount 含自身，需减去旧自身状态 + 新自身状态
      const descChecked = (node.data.checkedDescendantCount || 0) - (node.data.checked ? 1 : 0) + selfChecked
      node.data.checkedDescendantCount = Math.max(0, Math.min(node.data.descendantCount, descChecked))
      if (node.data.cascaded && !checkedSet.has(node.key)) {
        node.data.cascaded = false
      }
      node.data.cascaded = node.data.checkedDescendantCount === node.data.descendantCount
      return
    }
    if (node.data.descendantCount > 0) {
      // 自身是否勾选（descendantCount 含自身，child 不重复加 has(key)）
      let checked = checkedSet.has(node.key) ? 1 : 0
      node.childNodes.forEach(child => {
        if (child.data?.descendantCount > 0) {
          // child 的 descendantCount 已含自身，直接用 checkedDescendantCount
          checked += child.data?.cascaded ? child.data.descendantCount : (child.data?.checkedDescendantCount || 0)
        } else {
          // 叶子节点，加 check 状态
          if (checkedSet.has(child.key)) checked++
          checked += (child.data?.checkedDescendantCount || 0)
        }
      })
      node.data.checkedDescendantCount = checked
      node.data.cascaded = checked > 0 && checked === node.data.descendantCount
    }
  }
  tree.store.root?.childNodes?.forEach(calc)
}

async function handleAssignPermission(row) {
  permRoleId.value = row.roleId
  permAppCode.value = row.appCode
  permTreeLoading.value = true
  permDialogVisible.value = true
}

// ========== 权限树操作 ==========
// 递归收集已加载节点的所有 functionId
function collectLoadedNodeIds(store) {
  const ids = []
  function walk(nodes) {
    nodes.forEach(n => {
      ids.push(n.key)
      if (n.childNodes && n.childNodes.length) walk(n.childNodes)
    })
  }
  if (store.root && store.root.childNodes) walk(store.root.childNodes)
  return ids
}

function handleTreeCheckAll() {
  const store = permTreeRef.value?.store
  if (!store) return
  const currentKeys = permTreeRef.value.getCheckedKeys()
  const allIds = collectLoadedNodeIds(store)
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
    // 勾选哪个关联哪个，不做级联（用"全选子节点"按钮显式操作）
    const checkedNodes = permTreeRef.value.getCheckedNodes(false, false)
    const functions = checkedNodes.map(n => ({ id: n.id, functionId: n.functionId, pid: n.pid, cascaded: n.cascaded || false }))
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
    .perm-node {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      .perm-node-icon { font-size: 16px; flex-shrink: 0; }
      .perm-node-label { font-size: 14px; }
      .perm-node-tag { flex-shrink: 0; margin-left: 4px; }
      .perm-count-tag { flex-shrink: 0; margin-left: 4px; font-size: 11px; }
      .perm-cascade-btn {
        flex-shrink: 0; margin-left: 4px; font-size: 16px; cursor: pointer;
        color: #c0c4cc; transition: color 0.2s;
        &:hover { color: #67c23a; }
        &.cascaded { color: #67c23a; }
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
