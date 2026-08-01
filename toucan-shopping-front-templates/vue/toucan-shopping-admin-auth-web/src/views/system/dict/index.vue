<template>
  <div class="dict-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <div class="dict-layout">
      <!-- 左侧：字典分类 -->
      <div class="left-panel">
        <el-card shadow="never" class="category-card">
          <template #header>
            <div class="card-header">
              <span>字典分类</span>
              <el-button type="primary" link size="small" :icon="Refresh" @click="loadCategories">刷新</el-button>
            </div>
          </template>
          <div class="category-list" v-loading="categoryLoading">
            <div
              v-for="c in categoryList"
              :key="c.id"
              class="category-item"
              :class="{ active: selectedCategoryId === c.id }"
              @click="selectCategory(c)"
            >
              <span class="category-name">{{ c.name }}</span>
              <el-tag :type="c.enableStatus === 1 ? 'success' : 'danger'" size="small">
                {{ c.enableStatus === 1 ? '启用' : '禁用' }}
              </el-tag>
            </div>
            <el-empty v-if="!categoryLoading && categoryList.length === 0" description="暂无分类" :image-size="60" />
          </div>
        </el-card>
      </div>

      <!-- 右侧：字典管理 -->
      <div class="right-panel">
        <!-- 搜索栏 -->
        <el-card shadow="never" class="search-card" v-if="selectedCategoryId">
          <el-form :model="searchForm" inline>
            <el-form-item label="名称">
              <el-input v-model="searchForm.name" placeholder="请输入名称" clearable style="width:200px" />
            </el-form-item>
            <el-form-item label="编码">
              <el-input v-model="searchForm.code" placeholder="请输入编码" clearable style="width:200px" />
            </el-form-item>
            <el-form-item label="启用状态">
              <el-select v-model="searchForm.enableStatus" placeholder="请选择状态" clearable style="width:140px">
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" v-permission="'pms:dict:item:list'" @click="handleSearch">搜索</el-button>
              <el-button :icon="Refresh" v-permission="'pms:dict:item:list'" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <span class="current-category" v-if="selectedCategory">
              当前分类：<strong>{{ selectedCategory.name }}</strong>
            </span>
            <div>
              <el-button :icon="Expand" v-permission="'pms:dict:item:list'" @click="expandAll">展开全部</el-button>
              <el-button :icon="Fold" v-permission="'pms:dict:item:list'" @click="collapseAll">折叠全部</el-button>
              <el-button type="primary" :icon="Plus" v-permission="'pms:dict:item:add'" @click="handleAdd">新增字典</el-button>
              <el-button type="danger" :icon="Delete" v-permission="'pms:dict:item:delete'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">删除</el-button>
              <el-button :icon="Refresh" @click="fetchData">刷新</el-button>
            </div>
          </div>

          <el-table
            :data="dictTree"
            :key="tableKey"
            border stripe row-key="id"
            :tree-props="{ children: 'children' }"
            :default-expand-all="expandAllFlag"
            v-loading="loading"
            @selection-change="handleSelectionChange"
            style="width:100%"
          >
            <el-table-column type="selection" width="50" />
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="name" label="字典名称" width="240" />
            <el-table-column prop="code" label="编码" width="160" />
            <el-table-column prop="parentName" label="上级节点" width="150" show-overflow-tooltip />
            <el-table-column prop="extendProperty" label="扩展属性" min-width="160" show-overflow-tooltip>
              <template #default="{ row }">
                <span v-if="row.extendProperty">{{ row.extendProperty }}</span>
                <span v-else style="color:#c0c4cc">--</span>
              </template>
            </el-table-column>
            <el-table-column prop="dictSort" label="排序" width="80" align="center" />
            <el-table-column prop="appName" label="关联应用" width="150" show-overflow-tooltip />
            <el-table-column label="是否快照" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="row.isSnapshot == 1 ? 'warning' : 'info'" size="small">
                  {{ row.isSnapshot == 1 ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
                  {{ row.enableStatus === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createAdminName" label="创建人" width="120" />
            <el-table-column prop="createDate" label="创建时间" width="170" />
            <el-table-column prop="updateAdminName" label="修改人" width="120" />
            <el-table-column prop="updateDate" label="修改时间" width="170" />
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="{ row }">
                <el-button type="success" link size="small" :icon="Plus" @click="handleAddChild(row)">添加子项</el-button>
                <el-button type="primary" link size="small" :icon="Edit" v-permission="'pms:dict:item:edit'" @click="handleEdit(row)">编辑</el-button>
                <el-button type="danger" link size="small" :icon="Delete" v-permission="'pms:dict:item:delete'" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="560px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级字典" prop="pid">
          <el-input v-if="isAddChild && parentNodeName" :model-value="parentNodeName" disabled />
          <el-tree-select v-else v-model="formData.pid"
            :key="treeSelectKey"
            :data="treeSelectData" :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择上级（留空为顶级）" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item label="字典分类" prop="categoryId">
          <el-input v-if="isAddChild" :model-value="selectedCategory?.name" disabled />
          <el-select v-else v-model="formData.categoryId" placeholder="请选择字典分类" style="width:100%">
            <el-option v-for="c in categoryList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入字典名称" maxlength="20" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码" maxlength="100" />
        </el-form-item>
        <el-form-item label="扩展属性" prop="extendProperty">
          <el-input v-model="formData.extendProperty" placeholder="请输入扩展属性（JSON格式）" maxlength="255" />
        </el-form-item>
        <el-form-item label="排序" prop="dictSort">
          <el-input-number v-model="formData.dictSort" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="状态" prop="enableStatus">
          <el-switch v-model="formData.enableStatus" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
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
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, Expand, Fold } from '@element-plus/icons-vue'
import { addDict, updateDict, delDict, delBatchDict, queryDictTree } from '@/api/system/dict'
import { listAllDictCategory } from '@/api/system/dictCategory'

const route = useRoute()

// ========== 搜索 ==========
const searchForm = reactive({ name: '', code: '', enableStatus: '' })

function handleSearch() {
  fetchData()
}

function handleReset() {
  searchForm.name = ''
  searchForm.code = ''
  searchForm.enableStatus = ''
  fetchData()
}

// ========== 左侧分类 ==========
const categoryList = ref([])
const categoryLoading = ref(false)
const selectedCategoryId = ref(null)
const selectedCategory = ref(null)

async function loadCategories() {
  categoryLoading.value = true
  try {
    const res = await listAllDictCategory()
    categoryList.value = res.data || []
    if (categoryList.value.length && !selectedCategoryId.value) {
      selectCategory(categoryList.value[0])
    }
  } finally {
    categoryLoading.value = false
  }
}

function selectCategory(c) {
  if (selectedCategoryId.value === c.id) return
  selectedCategoryId.value = c.id
  selectedCategory.value = c
  // 切换分类时关闭弹窗，避免 categoryId/上级字典 与当前分类不一致
  dialogVisible.value = false
  dictTree.value = []
  treeSelectData.value = []
  fetchData()
}

// ========== 树构建 ==========
function buildTree(flatList) {
  if (!flatList || flatList.length === 0) return []
  const map = {}
  const roots = []
  flatList.forEach(d => { map[d.id] = { ...d, children: [] } })
  flatList.forEach(d => {
    const node = map[d.id]
    const pid = d.pid != null ? d.pid : -1
    if (pid === -1 || !map[pid]) {
      roots.push(node)
    } else {
      map[pid].children.push(node)
    }
  })
  // 递归清理空 children
  function cleanEmpty(arr) {
    arr.forEach(n => { if (n.children.length === 0) delete n.children; else cleanEmpty(n.children) })
  }
  cleanEmpty(roots)
  // 按 sort 排序
  function sortTree(arr) {
    arr.sort((a, b) => (a.dictSort || 0) - (b.dictSort || 0))
    arr.forEach(n => { if (n.children) sortTree(n.children) })
  }
  sortTree(roots)
  return roots
}

// ========== 右侧字典树 ==========
const dictTree = ref([])
const tableKey = ref(0)
const expandAllFlag = ref(true)
const loading = ref(false)
// 树选择器数据（用于上级字典选择）
const treeSelectData = ref([])
const treeSelectKey = ref(0)

async function fetchData() {
  if (!selectedCategoryId.value) { dictTree.value = []; return }
  loading.value = true
  try {
    const params = { categoryId: selectedCategoryId.value }
    if (searchForm.name) params.name = searchForm.name
    if (searchForm.code) params.code = searchForm.code
    if (searchForm.enableStatus !== '') params.enableStatus = searchForm.enableStatus
    const res = await queryDictTree(params)
    const flat = res.data || []
    dictTree.value = buildTree(flat)
    treeSelectData.value = buildTree(flat)
    treeSelectKey.value++
    tableKey.value++
  } finally {
    loading.value = false
  }
}

onMounted(() => { loadCategories() })

// ========== 展开/折叠 ==========
function expandAll() {
  expandAllFlag.value = true
  tableKey.value++
}
function collapseAll() {
  expandAllFlag.value = false
  tableKey.value++
}

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
    await ElMessageBox.confirm('确定删除选中的字典?', '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })
  } catch {
    return
  }
  loading.value = true
  try {
    await delBatchDict(selectedRows.value)
    ElMessage.success('删除成功')
    selectedRows.value = []
    fetchData()
  } finally {
    loading.value = false
  }
}

// ========== 新增/编辑 ==========
const dialogVisible = ref(false)
const isEdit = ref(false)
const isAddChild = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)
const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑字典'
  if (isAddChild.value) return '添加子字典'
  return '新增字典'
})

// 添加子项时显示的父节点名称
const parentNodeName = computed(() => {
  if (!isAddChild.value || !formData.pid) return ''
  const found = findNodeById(treeSelectData.value, formData.pid)
  return found ? found.name : ''
})

function findNodeById(tree, id) {
  for (const n of tree) {
    if (n.id === id) return n
    if (n.children) { const f = findNodeById(n.children, id); if (f) return f }
  }
  return null
}

const formData = reactive({
  pid: null, categoryId: null, name: '', code: '',
  extendProperty: '', dictSort: 0, remark: '', enableStatus: 1
})

const formRules = {
  name: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入编码', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择字典分类', trigger: 'change' }]
}

function resetForm() {
  formData.pid = null; formData.categoryId = selectedCategoryId.value
  formData.name = ''; formData.code = ''; formData.extendProperty = ''
  formData.dictSort = 0; formData.remark = ''; formData.enableStatus = 1
}

// 弹窗中切换字典分类时，自动刷新上级字典树
watch(() => formData.categoryId, (newVal) => {
  if (dialogVisible.value && !isAddChild.value && newVal != null) {
    loadTreeSelectData(newVal)
  }
})

// 按分类ID加载上级字典树选择器数据
async function loadTreeSelectData(categoryId) {
  if (!categoryId) return
  try {
    const res = await queryDictTree({ categoryId })
    const flat = res.data || []
    treeSelectData.value = buildTree(flat)
    treeSelectKey.value++
  } catch { }
}

async function handleAdd() {
  if (!selectedCategoryId.value) { ElMessage.warning('请先选择字典分类'); return }
  isEdit.value = false; isAddChild.value = false; editingId.value = null
  resetForm()
  await loadTreeSelectData(formData.categoryId)
  dialogVisible.value = true
}

function handleAddChild(row) {
  isEdit.value = false; isAddChild.value = true; editingId.value = null
  resetForm()
  formData.pid = row.id
  formData.categoryId = row.categoryId
  dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true; isAddChild.value = false; editingId.value = row.id
  formData.pid = row.pid || null
  formData.categoryId = row.categoryId; formData.name = row.name
  formData.code = row.code; formData.extendProperty = row.extendProperty || ''
  formData.dictSort = row.dictSort || 0; formData.remark = row.remark
  formData.enableStatus = row.enableStatus
  // 加载当前 dict 所属分类的上级字典树（可能和左侧选中的分类不同）
  await loadTreeSelectData(formData.categoryId)
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const data = {
      name: formData.name, code: formData.code,
      categoryId: formData.categoryId, pid: formData.pid != null ? formData.pid : null,
      extendProperty: formData.extendProperty, dictSort: formData.dictSort,
      remark: formData.remark, enableStatus: formData.enableStatus
    }
    if (isEdit.value) {
      await updateDict({ id: editingId.value, ...data })
      ElMessage.success('编辑成功')
    } else {
      await addDict(data)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false; resetForm(); fetchData()
  } catch { } finally { submitLoading.value = false }
}

// ========== 删除 ==========
function handleDelete(row) {
  const hasChildren = row.children && row.children.length > 0
  const msg = hasChildren
    ? `「${row.name}」下有子字典，删除后子项也将被删除，确认删除吗？`
    : `确认删除字典「${row.name}」吗？删除后不可恢复。`
  ElMessageBox.confirm(msg, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delDict(row.id); ElMessage.success('删除成功'); fetchData() } catch { }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.dict-management {
  .dict-layout {
    display: flex;
    gap: $gap-md;
  }

  .left-panel {
    width: 240px;
    flex-shrink: 0;

    .category-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-weight: 600;
      }
      :deep(.el-card__body) { padding: 0; }
    }

    .category-list {
      max-height: calc(100vh - 200px);
      overflow-y: auto;
    }

    .category-item {
      display: flex;
      align-items: center;
      padding: 10px 16px;
      cursor: pointer;
      border-left: 3px solid transparent;
      transition: all 0.2s;

      &:hover { background-color: #f5f7fa; }

      &.active {
        background-color: #ecf5ff;
        border-left-color: $primary;
        color: $primary;
        font-weight: 600;
      }
    }
  }

  .right-panel {
    flex: 1;
    min-width: 0;

    .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }

    .table-card {
      .toolbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: $gap-md;
        .current-category { color: $text-secondary; font-size: 14px; }
      }
    }
  }

  :deep(.el-table) {
    th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; }
  }
}
</style>
