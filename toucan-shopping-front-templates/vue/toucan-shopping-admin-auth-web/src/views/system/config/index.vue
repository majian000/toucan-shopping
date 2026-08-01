<template>
  <div class="config-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-row :gutter="16">
      <!-- 左侧：分类树 -->
      <el-col :span="5">
        <el-card shadow="never" class="category-tree-card">
          <template #header>
            <div class="card-header-row">
              <span class="card-header-title">配置分类</span>
              <el-button :icon="Refresh" link size="small" title="刷新分类" @click="loadCategories" />
            </div>
          </template>
          <el-tree
            v-loading="categoryLoading"
            :data="categoryTree"
            :props="{ children: 'children', label: 'name' }"
            node-key="id"
            :default-expand-all="true"
            highlight-current
            @node-click="handleCategoryClick"
          >
            <template #default="{ data }">
              <span class="tree-node">
                <span class="tree-node-label">{{ data.name }}</span>
              </span>
            </template>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 右侧：配置列表 -->
      <el-col :span="19">
        <!-- 搜索栏 -->
        <el-card shadow="never" class="search-card">
          <el-form :model="searchForm" inline>
            <el-form-item label="配置名称">
              <el-input v-model="searchForm.name" placeholder="请输入配置名称" clearable style="width:180px" />
            </el-form-item>
            <el-form-item label="配置编码">
              <el-input v-model="searchForm.code" placeholder="请输入配置编码" clearable style="width:180px" />
            </el-form-item>
            <el-form-item label="类型">
              <el-select v-model="searchForm.type" placeholder="请选择类型" clearable style="width:120px">
                <el-option label="STRING" value="STRING" />
                <el-option label="NUMBER" value="NUMBER" />
                <el-option label="BOOLEAN" value="BOOLEAN" />
                <el-option label="JSON" value="JSON" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" v-permission="'system:config:list'" @click="handleSearch">搜索</el-button>
              <el-button :icon="Refresh" v-permission="'system:config:list'" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 数据表格 -->
        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <el-button type="primary" :icon="Plus" v-permission="'system:config:add'" @click="handleAdd">新增配置</el-button>
            <el-button type="success" :icon="Refresh" v-permission="'system:config:list'" @click="handleRefreshCache">刷新缓存</el-button>
          </div>

          <el-table :data="configs" border stripe v-loading="loading" style="width:100%">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="name" label="配置名称" width="150" />
            <el-table-column prop="code" label="配置编码" width="160" show-overflow-tooltip />
            <el-table-column prop="value" label="配置值" min-width="180" show-overflow-tooltip />
            <el-table-column label="类型" width="90" align="center">
              <template #default="{ row }">
                <el-tag size="small" :type="typeTagType(row.type)">{{ row.type || 'STRING' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="categoryName" label="所属分类" width="120" />
            <el-table-column label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
                  {{ row.enableStatus === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createDate" label="创建时间" width="170" sortable />
            <el-table-column label="操作" width="240" fixed="right">
              <template #default="{ row }">
                <el-button type="info" link size="small" :icon="View" @click="handleView(row)">查看</el-button>
                <el-button type="primary" link size="small" :icon="Edit" v-permission="'system:config:edit'" @click="handleEdit(row)">编辑</el-button>
                <el-button type="danger" link size="small" :icon="Delete" v-permission="'system:config:delete'" @click="handleDelete(row)">删除</el-button>
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
      </el-col>
    </el-row>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="所属分类" prop="categoryId">
          <el-select v-model="formData.categoryId" placeholder="请选择分类" clearable style="width:100%">
            <el-option
              v-for="cat in categoryOptions"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入配置名称" maxlength="100" />
        </el-form-item>
        <el-form-item label="配置编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入配置编码（唯一）" maxlength="100" />
        </el-form-item>
        <el-form-item label="值类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择值类型" style="width:100%">
            <el-option label="STRING - 字符串" value="STRING" />
            <el-option label="NUMBER - 数字" value="NUMBER" />
            <el-option label="BOOLEAN - 布尔" value="BOOLEAN" />
            <el-option label="JSON - JSON对象" value="JSON" />
          </el-select>
        </el-form-item>
        <el-form-item label="配置值" prop="value">
          <!-- STRING -->
          <el-input
            v-if="formData.type === 'STRING' || formData.type === '' || formData.type === undefined"
            v-model="formData.value"
            placeholder="请输入配置值"
          />
          <!-- NUMBER -->
          <el-input-number
            v-else-if="formData.type === 'NUMBER'"
            v-model="formData.value"
            :controls="false"
            placeholder="请输入数字值"
            style="width:100%"
          />
          <!-- BOOLEAN -->
          <el-switch
            v-else-if="formData.type === 'BOOLEAN'"
            v-model="formData.value"
            active-value="true"
            inactive-value="false"
            active-text="true"
            inactive-text="false"
          />
          <!-- JSON -->
          <el-input
            v-else-if="formData.type === 'JSON'"
            v-model="formData.value"
            type="textarea"
            :rows="6"
            placeholder="请输入JSON格式的值"
          />
          <!-- default fallback -->
          <el-input v-else v-model="formData.value" placeholder="请输入配置值" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" placeholder="越小越靠前" />
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
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入配置说明" maxlength="500" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="查看配置"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="配置名称">{{ viewData.name }}</el-descriptions-item>
        <el-descriptions-item label="配置编码">{{ viewData.code }}</el-descriptions-item>
        <el-descriptions-item label="所属分类">{{ viewData.categoryName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="值类型">
          <el-tag size="small" :type="typeTagType(viewData.type)">{{ viewData.type || 'STRING' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="排序">{{ viewData.sort }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.enableStatus === 1 ? 'success' : 'danger'" size="small">
            {{ viewData.enableStatus === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="配置值" :span="2">
          <el-input :model-value="viewData.value" type="textarea" :rows="6" readonly />
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ viewData.updateDate || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, View } from '@element-plus/icons-vue'
import { listConfig, addConfig, updateConfig, delConfig, refreshConfigCache } from '@/api/system/config'
import { listAllConfigCategory } from '@/api/system/configCategory'

const route = useRoute()

const configs = ref([])
const tableTotal = ref(0)
const loading = ref(false)

// 分类
const categoryTree = ref([])
const categoryOptions = ref([])
const selectedCategoryId = ref(null)

const categoryLoading = ref(false)

async function loadCategories() {
  categoryLoading.value = true
  try {
    const res = await listAllConfigCategory()
    const list = res.data || []
    categoryOptions.value = list
    categoryTree.value = list.map(item => ({
      id: item.id,
      name: item.name,
      code: item.code,
      enableStatus: item.enableStatus
    }))
  } catch {
    // error handled by interceptor
  } finally {
    categoryLoading.value = false
  }
}

function handleCategoryClick(data) {
  if (data && data.id) {
    selectedCategoryId.value = data.id
  } else {
    selectedCategoryId.value = null
  }
  pagination.page = 1
  fetchData()
}

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name || undefined,
      code: searchForm.code || undefined,
      type: searchForm.type || undefined,
      categoryId: selectedCategoryId.value || undefined
    }
    const res = await listConfig(params)
    configs.value = res.data.rows || res.data.list || []
    tableTotal.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCategories()
  fetchData()
})

const searchForm = reactive({ name: '', code: '', type: '' })

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() {
  searchForm.name = ''
  searchForm.code = ''
  searchForm.type = ''
  pagination.page = 1
  fetchData()
}

const pagination = reactive({ page: 1, size: 10 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

// 查看弹窗
const viewDialogVisible = ref(false)
const viewData = reactive({
  name: '', code: '', value: '', type: '', categoryName: '',
  sort: 0, enableStatus: 1, remark: '', createDate: '', updateDate: ''
})

function handleView(row) {
  viewData.name = row.name
  viewData.code = row.code
  viewData.value = row.value
  viewData.type = row.type || 'STRING'
  viewData.categoryName = row.categoryName || '-'
  viewData.sort = row.sort || 0
  viewData.enableStatus = row.enableStatus
  viewData.remark = row.remark || ''
  viewData.createDate = row.createDate || ''
  viewData.updateDate = row.updateDate || ''
  viewDialogVisible.value = true
}

// 刷新缓存
const refreshLoading = ref(false)
async function handleRefreshCache() {
  refreshLoading.value = true
  try {
    const res = await refreshConfigCache()
    ElMessage.success(res.data?.msg || res.msg || '刷新成功')
  } catch {
    // error handled by request interceptor
  } finally {
    refreshLoading.value = false
  }
}

const dialogTitle = computed(() => isEdit.value ? '编辑配置' : '新增配置')

const formData = reactive({
  categoryId: null,
  name: '',
  code: '',
  type: 'STRING',
  value: '',
  sort: 0,
  remark: '',
  enableStatus: 1
})

const formRules = {
  name: [{ required: true, message: '请输入配置名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入配置编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9._]*$/, message: '编码只能使用英文、数字、小数点、下划线，且不能以数字开头', trigger: 'blur' }
  ],
  type: [],
  value: [],
  sort: [],
  remark: []
}

function typeTagType(type) {
  const map = { STRING: '', NUMBER: 'warning', BOOLEAN: 'success', JSON: 'danger' }
  return map[type] || ''
}

function resetForm() {
  formData.categoryId = null
  formData.name = ''
  formData.code = ''
  formData.type = 'STRING'
  formData.value = ''
  formData.sort = 0
  formData.remark = ''
  formData.enableStatus = 1
}

function handleAdd() {
  isEdit.value = false
  editingId.value = null
  resetForm()
  if (selectedCategoryId.value) {
    formData.categoryId = selectedCategoryId.value
  }
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  editingId.value = row.id
  formData.categoryId = row.categoryId
  formData.name = row.name
  formData.code = row.code
  formData.type = row.type || 'STRING'
  formData.value = row.value
  formData.sort = row.sort || 0
  formData.remark = row.remark
  formData.enableStatus = row.enableStatus
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  // JSON 类型校验
  if (formData.type === 'JSON' && formData.value) {
    try {
      JSON.parse(formData.value)
    } catch {
      ElMessage.warning('配置值不是有效的JSON格式，请检查')
      return
    }
  }
  submitLoading.value = true
  try {
    const payload = {
      name: formData.name,
      code: formData.code,
      type: formData.type,
      value: String(formData.value),
      sort: formData.sort,
      remark: formData.remark,
      enableStatus: formData.enableStatus,
      categoryId: formData.categoryId
    }
    if (isEdit.value) {
      await updateConfig({ id: editingId.value, ...payload })
      ElMessage.success('编辑成功')
    } else {
      await addConfig(payload)
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

function handleDelete(row) {
  ElMessageBox.confirm(`确认删除配置"${row.name}"吗？删除后不可恢复。`, '删除确认', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await delConfig(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch {
      // error handled by request interceptor
    }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.config-management {
  .category-tree-card {
    .card-header-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .card-header-title {
      font-weight: 600;
      color: $text-primary;
    }
    :deep(.el-card__body) {
      padding: 12px;
    }
    :deep(.el-tree) {
      .tree-node {
        display: flex;
        align-items: center;
        .tree-node-label {
          font-size: 14px;
        }
      }
    }
  }

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
}
</style>
