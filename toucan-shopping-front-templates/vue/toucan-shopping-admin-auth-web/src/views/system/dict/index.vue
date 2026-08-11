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
        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <span class="current-category" v-if="selectedCategory">
              当前分类：<strong>{{ selectedCategory.name }}</strong>
            </span>
            <div>
              <el-button type="primary" :icon="Plus" v-permission="'pms:dict:item:add'" @click="handleAdd">新增字典</el-button>
              <el-button :icon="Refresh" @click="fetchData">刷新</el-button>
            </div>
          </div>

          <el-table
            :data="dictTree"
            :key="tableKey"
            border stripe row-key="id"
            :tree-props="{ children: 'children' }"
            :default-expand-all="false"
            v-loading="loading"
            style="width:100%"
          >
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
            <el-table-column label="关联应用" width="150">
              <template #default="{ row }">
                <el-tag v-if="row.appName" type="info" size="small">{{ row.appName }}</el-tag>
                <span v-else style="color:#c0c4cc">--</span>
              </template>
            </el-table-column>
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
                <el-button type="success" link size="small" :icon="Plus" v-permission="'pms:dict:item:addChild'" @click="handleAddChild(row)">添加子项</el-button>
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
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" v-loading="dialogLoading">
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
        <el-form-item label="关联应用">
          <el-tag v-if="selectedCategory?.appName" type="info">{{ selectedCategory.appName }}</el-tag>
          <el-tag v-else-if="selectedCategory?.appCode" type="info">{{ selectedCategory.appCode }}</el-tag>
          <span v-else style="color:#c0c4cc">--</span>
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
import { Plus, Refresh, Delete, Edit } from '@element-plus/icons-vue'
import { addDict, updateDict, delDict, queryDictTreeAll } from '@/api/system/dict'
import { listAllDictCategory } from '@/api/system/dictCategory'

const route = useRoute()

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

// ========== 右侧字典树 ==========
const dictTree = ref([])
const tableKey = ref(0)
const loading = ref(false)
const treeSelectData = ref([])
const treeSelectKey = ref(0)

async function fetchData() {
  if (!selectedCategoryId.value) { dictTree.value = []; return }
  loading.value = true
  try {
    const res = await queryDictTreeAll({ categoryId: selectedCategoryId.value })
    dictTree.value = res.data || []
    treeSelectData.value = res.data || []
    treeSelectKey.value++
    tableKey.value++
  } finally {
    loading.value = false
  }
}

onMounted(() => { loadCategories() })

// ========== 新增/编辑 ==========
const dialogVisible = ref(false)
const dialogLoading = ref(false)
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

// 加载上级字典树选择器
async function loadTreeSelectData(categoryId) {
  if (!categoryId) return
  try {
    const res = await queryDictTreeAll({ categoryId })
    treeSelectData.value = res.data || []
    treeSelectKey.value++
  } catch { }
}

async function handleAdd() {
  if (!selectedCategoryId.value) { ElMessage.warning('请先选择字典分类'); return }
  isEdit.value = false; isAddChild.value = false; editingId.value = null
  resetForm()
  dialogVisible.value = true
  dialogLoading.value = true
  try {
    await loadTreeSelectData(formData.categoryId)
  } finally {
    dialogLoading.value = false
  }
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
  formData.categoryId = row.categoryId
  formData.name = row.name; formData.code = row.code
  formData.extendProperty = row.extendProperty || ''
  formData.dictSort = row.dictSort || 0; formData.remark = row.remark
  formData.enableStatus = row.enableStatus
  dialogVisible.value = true
  dialogLoading.value = true
  try {
    await loadTreeSelectData(formData.categoryId)
  } finally {
    dialogLoading.value = false
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const data = {
      name: formData.name, code: formData.code,
      categoryId: formData.categoryId, appCode: selectedCategory.value?.appCode || '',
      pid: formData.pid != null ? formData.pid : null,
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
      min-height: 80px;
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
