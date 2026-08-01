<template>
  <div class="menu-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="table-card">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'system:menu:add'" @click="handleAdd">新增菜单</el-button>
        <el-button :icon="Refresh" @click="handleRefresh">刷新</el-button>
      </div>

      <!-- 树形表格（懒加载） -->
      <el-table
        :key="tableKey"
        :data="menuTree"
        border stripe row-key="id" lazy
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :load="loadChildren"
        v-loading="loading"
        style="width:100%"
      >
        <el-table-column prop="name" label="菜单名称" min-width="220">
          <template #default="{ row }">
            <el-icon v-if="row.icon" style="margin-right:6px; vertical-align:middle">
              <component :is="MenuIcon" />
            </el-icon>
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="图标" width="100">
          <template #default="{ row }">
            <span class="icon-name" v-if="row.icon">{{ row.icon }}</span>
            <span v-else class="no-icon">--</span>
          </template>
        </el-table-column>
        <el-table-column label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="row.type === '目录' ? '' : row.type === '菜单' ? 'success' : 'info'"
              size="small"
            >
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="路由地址" width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.url">{{ row.url }}</span>
            <span v-else class="no-icon">--</span>
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限标识" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.permission">{{ row.permission }}</span>
            <span v-else class="no-icon">--</span>
          </template>
        </el-table-column>
        <el-table-column prop="functionSort" label="排序" width="80" align="center" sortable />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'system:menu:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button
              v-if="row.type !== '按钮'"
              type="success" link size="small" :icon="Plus"
              @click="handleAddChild(row)">添加子菜单</el-button>
            <el-button type="danger" link size="small" :icon="Delete"
              v-permission="'system:menu:delete'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px"
      :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级菜单" prop="pid">
          <!-- 添加子菜单时显示父级名称（只读） -->
          <el-input v-if="isAddChild" :model-value="parentNodeName" disabled />
          <!-- 新增/编辑时可选择 -->
          <el-tree-select v-else v-model="formData.pid"
            :key="treeSelectKey"
            :data="fullMenuTree"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择上级菜单（留空为顶级）" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio value="目录">目录</el-radio>
            <el-radio value="菜单">菜单</el-radio>
            <el-radio value="按钮">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="formData.type !== '按钮'">
          <el-input v-model="formData.icon" placeholder="请输入 Element Plus 图标名（如：HomeFilled）" />
          <div class="form-tip">请输入 Element Plus Icons 组件名</div>
        </el-form-item>
        <el-form-item label="路由地址" prop="url">
          <el-input v-model="formData.url" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="formData.permission" placeholder="请输入权限标识（如：system:admin:list）" />
        </el-form-item>
        <el-form-item label="排序" prop="functionSort">
          <el-input-number v-model="formData.functionSort" :min="0" :max="9999" style="width:160px" />
        </el-form-item>
        <el-form-item label="状态" prop="enableStatus">
          <el-switch v-model="formData.enableStatus" :active-value="1" :inactive-value="0"
            active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading" :disabled="treeLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Refresh, Menu as MenuIcon } from '@element-plus/icons-vue'
import { listMenuByPid, listMenuTree, addMenu, updateMenu, delMenu } from '@/api/system/menu'

const route = useRoute()

// ========== 类型转换 ==========
const typeMap = { 0: '目录', 1: '菜单', 2: '按钮', directory: '目录', menu: '菜单', button: '按钮' }
const typeMapReverse = { '目录': 'directory', '菜单': 'menu', '按钮': 'button' }
const typeCodeMap = { '目录': 0, '菜单': 1, '按钮': 2 }

function transformTypes(tree) {
  return tree.map(node => {
    const hasChild = node.haveChild === true || node.haveChild === 'true'
    return {
      ...node,
      type: typeMap[node.type] || node.type,
      hasChildren: hasChild,
      leaf: !hasChild
    }
  })
}

// ========== 树形菜单数据（懒加载用） ==========
const menuTree = ref([])
const tableKey = ref(0)
const loading = ref(false)
// 全量菜单树（上级菜单选择器用，过滤按钮）
const fullMenuTree = ref([])

async function fetchData() {
  loading.value = true
  try {
    const res = await listMenuByPid({ pid: -1 })
    menuTree.value = transformTypes(res.data || [])
  } finally {
    loading.value = false
  }
}

// 加载完整菜单树（弹窗打开时调用）
async function loadFullTree(excludeId) {
  treeLoading.value = true
  try {
    const fullRes = await listMenuTree()
    let tree = filterButtons(transformTypes(fullRes.data || []))
    if (excludeId) {
      tree = filterNode(tree, excludeId)
    }
    fullMenuTree.value = tree
  } finally {
    treeLoading.value = false
  }
}

// 过滤掉指定id的节点及其子节点
function filterNode(nodes, id) {
  if (!nodes) return []
  return nodes
    .filter(n => n.id !== id)
    .map(n => ({
      ...n,
      children: n.children ? filterNode(n.children, id) : undefined
    }))
}

// 递归过滤按钮节点
function filterButtons(nodes) {
  return nodes
    .filter(n => n.type !== '按钮')
    .map(n => ({
      ...n,
      children: n.children ? filterButtons(n.children) : undefined
    }))
}

onMounted(() => fetchData())

async function handleRefresh() { await fetchData(); tableKey.value++ }

// ========== 懒加载：表格展开节点时请求接口获取子节点 ==========
async function loadChildren(row, _treeNode, resolve) {
  try {
    const res = await listMenuByPid({ pid: row.id })
    resolve(transformTypes(res.data || []))
  } catch {
    resolve([])
  }
}

// ========== 新增/编辑 ==========
const dialogVisible = ref(false)
const treeSelectKey = ref(0)
const isEdit = ref(false)
const isAddChild = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const treeLoading = ref(false)    // 菜单树加载中，加载完成前禁用提交
const formRef = ref(null)

const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑菜单'
  if (isAddChild.value) return '添加子菜单'
  return '新增菜单'
})

// 添加子菜单时显示的父节点名称
const parentNodeName = computed(() => {
  if (!isAddChild.value || !formData.pid) return ''
  const found = findNodeById(fullMenuTree.value, formData.pid)
  return found ? found.name : ''
})

// 在菜单树中按ID查找节点
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

// 全量树中查找父节点
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
  pid: null, type: '菜单', name: '', icon: '',
  url: '', permission: '', functionSort: 0, enableStatus: 1
})

const formRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
  functionSort: [{ required: true, message: '请输入排序号', trigger: 'blur' }],
  permission: [{ required: true, message: '请输入权限标识', trigger: 'blur' }]
}

function resetForm() {
  formData.pid = null; formData.type = '菜单'; formData.name = ''
  formData.icon = ''; formData.url = ''
  formData.permission = ''; formData.functionSort = 0; formData.enableStatus = 1
}

async function handleAdd() {
  isEdit.value = false; isAddChild.value = false; editingId.value = null
  resetForm(); await loadFullTree(); treeSelectKey.value++; dialogVisible.value = true
}

async function handleAddChild(row) {
  isEdit.value = false; isAddChild.value = true; editingId.value = null
  resetForm()
  formData.pid = row.id
  formData.type = row.type === '目录' ? '菜单' : '按钮'
  await loadFullTree(); treeSelectKey.value++; dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true; isAddChild.value = false; editingId.value = row.id
  formData.type = row.type; formData.name = row.name
  formData.icon = row.icon || ''
  formData.url = row.url || ''; formData.permission = row.permission || ''
  formData.functionSort = row.functionSort; formData.enableStatus = row.enableStatus
  // 先加载完整树查找父节点（不能用 loadFullTree(excludeId)，排除后找不到父级）
  await loadFullTree(); treeSelectKey.value++
  const pid = findParentById(fullMenuTree.value, row.id)?.id || null
  formData.pid = pid
  // 再从 tree-select 中排除自身，避免选自己为父级
  fullMenuTree.value = filterNode(fullMenuTree.value, row.id)
  await nextTick()
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (isEdit.value && formData.pid === editingId.value) {
    ElMessage.warning('上级菜单不能选择自己')
    return
  }
  submitLoading.value = true
  try {
    const apiData = {
      pid: formData.pid,
      type: typeCodeMap[formData.type] ?? formData.type,
      name: formData.name, icon: formData.icon,
      url: formData.url,
      permission: formData.permission, functionSort: formData.functionSort, enableStatus: formData.enableStatus
    }
    if (isEdit.value) { await updateMenu({ id: editingId.value, ...apiData }); ElMessage.success('编辑成功') }
    else { await addMenu(apiData); ElMessage.success('新增成功') }
    dialogVisible.value = false
    resetForm()
    await fetchData()
    tableKey.value++
  } finally { submitLoading.value = false }
}


// ========== 删除（根节点也可删除） ==========
function handleDelete(row) {
  const hasChildren = row.children && row.children.length > 0
  const msg = hasChildren
    ? `「${row.name}」下有子菜单，删除后子菜单也将被删除，确认删除吗？`
    : `确认删除菜单「${row.name}」吗？删除后不可恢复。`

  ElMessageBox.confirm(msg, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delMenu(row.id, row.functionId); ElMessage.success('删除成功'); await fetchData(); tableKey.value++ }
    catch { /* handled */ }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.menu-management {
  .table-card {
    .toolbar { margin-bottom: $gap-md; }
  }
  :deep(.el-table) th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
  .icon-name {
    display: inline-block; padding: 2px 8px; background: #ecf5ff;
    color: $primary; border-radius: 3px; font-size: 12px; font-family: monospace;
  }
  .no-icon { color: $text-placeholder; }
  .form-tip { font-size: 12px; color: $text-secondary; margin-top: 4px; line-height: 1.5; }
}
</style>
