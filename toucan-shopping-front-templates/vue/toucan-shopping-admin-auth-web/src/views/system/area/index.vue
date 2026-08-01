<template>
  <div class="area-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="table-card">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'system:area:add'" @click="handleAdd">新增地区</el-button>
        <el-button :icon="Refresh" @click="handleRefresh">刷新</el-button>
      </div>

      <!-- 树形表格（懒加载） -->
      <el-table
        :key="tableKey"
        :data="areaTree"
        border stripe row-key="id" lazy
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :load="loadChildren"
        v-loading="loading"
        style="width:100%"
      >
        <el-table-column prop="code" label="编码" width="260" />
        <el-table-column label="地区名称" min-width="70">
          <template #default="{ row }">
            <span v-if="row.type === 1">{{ row.province }}</span>
            <span v-else-if="row.type === 2">{{ row.city }}</span>
            <span v-else-if="row.type === 3">{{ row.area }}</span>
            <span v-else>{{ row.province || '' }}{{ row.city || '' }}{{ row.area || '' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="级别" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="typeTagType(row.type)" size="small">{{ typeLabel(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="直辖市" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isMunicipality === 1 ? 'warning' : 'info'" size="small">
              {{ row.isMunicipality === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="areaSort" label="排序" width="70" align="center" />
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'system:area:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button
              v-if="row.type !== 3"
              type="success" link size="small" :icon="Plus"
              @click="handleAddChild(row)">添加下级</el-button>
            <el-button type="danger" link size="small" :icon="Delete"
              v-permission="'system:area:delete'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px"
      :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级地区" prop="parentCode">
          <el-input v-if="isAddChild" :model-value="parentNodeName" disabled />
          <el-tree-select
            v-else
            v-model="formData.parentCode"
            :key="treeSelectKey"
            :data="fullAreaTree"
            :props="{ label: 'areaDisplayName', value: 'code', children: 'children' }"
            placeholder="请选择上级地区（留空为顶级）" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item label="级别" prop="type">
          <el-select v-model="formData.type" placeholder="请选择级别" style="width:100%">
            <el-option label="省" :value="1" />
            <el-option label="市" :value="2" />
            <el-option label="区县" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码" maxlength="20" />
        </el-form-item>
        <template v-if="formData.type === 1">
          <el-form-item label="省名称" prop="province">
            <el-input v-model="formData.province" placeholder="请输入省名称" maxlength="20" />
          </el-form-item>
        </template>
        <template v-if="formData.type === 2">
          <el-form-item label="市名称" prop="city">
            <el-input v-model="formData.city" placeholder="请输入市名称" maxlength="20" />
          </el-form-item>
          <el-form-item label="是否直辖市">
            <el-switch v-model="formData.isMunicipality" :active-value="1" :inactive-value="0" />
          </el-form-item>
        </template>
        <el-form-item v-if="formData.type === 3" label="区县名称" prop="area">
          <el-input v-model="formData.area" placeholder="请输入区县名称" maxlength="20" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.areaSort" :min="0" style="width:160px" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" maxlength="255" />
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
import { Plus, Delete, Edit, Refresh } from '@element-plus/icons-vue'
import { listAreaByPid, getAreaTree, addArea, updateArea, delArea } from '@/api/system/area'

const route = useRoute()

// ========== 类型映射 ==========
function typeLabel(type) {
  const map = { 1: '省', 2: '市', 3: '区县' }
  return map[type] || ''
}
function typeTagType(type) {
  const map = { 1: 'danger', 2: 'warning', 3: 'info' }
  return map[type] || 'info'
}

// ========== 树形表格数据（懒加载用） ==========
const areaTree = ref([])
const tableKey = ref(0)
const loading = ref(false)
// 全量树（上级选择器用）
const fullAreaTree = ref([])

function transformArea(nodes) {
  if (!nodes) return []
  return nodes.map(node => {
    const hasChild = node.haveChild === true || node.haveChild === 'true'
    return {
      ...node,
      hasChildren: hasChild,
      leaf: !hasChild
    }
  })
}

async function fetchData() {
  loading.value = true
  try {
    const res = await listAreaByPid({ pid: -1 })
    areaTree.value = transformArea(res.data || [])
  } finally {
    loading.value = false
  }
}

// 加载完整树（弹窗打开时调用）
async function loadFullTree(excludeCode) {
  const fullRes = await getAreaTree()
  let tree = buildDisplayName(fullRes.data || [])
  if (excludeCode) {
    tree = filterNode(tree, excludeCode)
  }
  fullAreaTree.value = tree
}

// 过滤掉指定code的节点及其子节点
function filterNode(nodes, code) {
  if (!nodes) return []
  return nodes
    .filter(node => node.code !== code)
    .map(node => ({
      ...node,
      children: node.children ? filterNode(node.children, code) : undefined
    }))
}

function buildDisplayName(tree) {
  if (!tree) return []
  return tree.map(item => ({
    ...item,
    areaDisplayName: [item.province, item.city, item.area].filter(Boolean).join(' - ') || item.code,
    children: item.children ? buildDisplayName(item.children) : undefined
  }))
}

onMounted(() => fetchData())

async function handleRefresh() { await fetchData(); tableKey.value++ }

// ========== 懒加载：展开时请求子节点 ==========
async function loadChildren(row, _treeNode, resolve) {
  try {
    const res = await listAreaByPid({ pid: row.id })
    resolve(transformArea(res.data || []))
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
const formRef = ref(null)
const parentNodeName = ref('')

const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑地区'
  if (isAddChild.value) return '添加下级地区'
  return '新增地区'
})

const formData = reactive({
  parentCode: '', code: '', type: 1, province: '', city: '', area: '',
  isMunicipality: 0, areaSort: 0, remark: ''
})

const formRules = {
  code: [{ required: true, message: '请输入编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择级别', trigger: 'change' }],
  province: [{ required: true, message: '请输入省名称', trigger: 'blur' }],
  city: [{ required: true, message: '请输入市名称', trigger: 'blur' }],
  area: [{ required: true, message: '请输入区县名称', trigger: 'blur' }]
}

function resetForm() {
  formData.parentCode = ''; formData.code = ''; formData.type = 1
  formData.province = ''; formData.city = ''; formData.area = ''
  formData.isMunicipality = 0; formData.areaSort = 0; formData.remark = ''
}

async function handleAdd() {
  isEdit.value = false; isAddChild.value = false; editingId.value = null
  resetForm(); await loadFullTree(); treeSelectKey.value++; dialogVisible.value = true
}

async function handleAddChild(row) {
  isEdit.value = false; isAddChild.value = true; editingId.value = null
  resetForm()
  formData.parentCode = row.code
  formData.type = (row.type || 1) + 1
  parentNodeName.value = [row.province, row.city, row.area].filter(Boolean).join(' - ')
  await loadFullTree(); treeSelectKey.value++; dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true; isAddChild.value = false; editingId.value = row.id
  formData.parentCode = (row.parentCode === '0' || row.pid === -1 || row.pid === '-1') ? '' : (row.parentCode || '')
  formData.code = row.code
  formData.type = row.type
  formData.province = row.province || ''
  formData.city = row.city || ''
  formData.area = row.area || ''
  formData.isMunicipality = row.isMunicipality || 0
  formData.areaSort = row.areaSort || 0
  formData.remark = row.remark || ''
  await loadFullTree(row.code); treeSelectKey.value++; dialogVisible.value = true
}

async function handleSubmit() {
  const fields = ['code', 'type']
  if (formData.type === 1) fields.push('province')
  else if (formData.type === 2) fields.push('city')
  else if (formData.type === 3) fields.push('area')

  const valid = await formRef.value.validateField(fields).catch(() => false)
  if (valid === false) return

  submitLoading.value = true
  try {
    const hasParent = formData.parentCode && formData.parentCode !== '0'
    const payload = {
      parentCode: formData.parentCode || '0',
      pid: hasParent ? null : -1,
      code: formData.code, type: formData.type,
      province: formData.province, city: formData.city, area: formData.area,
      isMunicipality: formData.isMunicipality, areaSort: formData.areaSort, remark: formData.remark
    }
    if (isEdit.value) {
      await updateArea({ id: editingId.value, ...payload })
      ElMessage.success('编辑成功')
    } else {
      await addArea(payload)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    resetForm()
    await fetchData()
    tableKey.value++
  } finally {
    submitLoading.value = false
  }
}

// ========== 删除 ==========
function handleDelete(row) {
  const name = [row.province, row.city, row.area].filter(Boolean).join('')
  ElMessageBox.confirm(`「${name}」下有子地区将被一并删除，确认删除吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await delArea(row.id)
      ElMessage.success('删除成功')
      await fetchData()
      tableKey.value++
    } catch { /* handled */ }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.area-management {
  .table-card {
    .toolbar { margin-bottom: $gap-md; }
  }
  :deep(.el-table) th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
}
</style>
