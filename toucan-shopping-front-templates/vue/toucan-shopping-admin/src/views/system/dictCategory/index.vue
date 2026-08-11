<template>
  <div class="dict-category-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入分类名称" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="searchForm.code" placeholder="请输入编码" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.enableStatus" placeholder="请选择状态" clearable style="width:140px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" v-permission="'toucan:admin:dictCategory'" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" v-permission="'toucan:admin:dictCategory'" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'toucan:admin:dictCategory:toolbar:save'" @click="handleAdd">新增分类</el-button>
        <el-button type="danger" :icon="Delete" v-permission="'toucan:admin:dictCategory:toolbar:delete'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">删除</el-button>
      </div>

      <el-table :data="list" border stripe v-loading="loading" @selection-change="handleSelectionChange" style="width:100%">
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="分类名称" width="150" />
        <el-table-column prop="code" label="编码" width="150" />
        <el-table-column prop="dictCategorySort" label="排序" width="80" align="center" />
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />

        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="createAdminUsername" label="创建人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column prop="updateAdminUsername" label="修改人" width="120" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="info" link size="small" :icon="View" v-permission="'toucan:admin:dictCategory:row:view'" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:admin:dictCategory:row:update'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:admin:dictCategory:row:delete'" @click="handleDelete(row)">删除</el-button>
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

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="520px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入分类名称" maxlength="20" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码" maxlength="100" />
        </el-form-item>

        <el-form-item label="排序" prop="dictCategorySort">
          <el-input-number v-model="formData.dictCategorySort" :min="0" placeholder="请输入排序" style="width:100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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

    <!-- 查看弹窗 -->
    <el-dialog v-model="viewVisible" title="查看字典分类" width="550px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="viewLoading">
        <el-empty v-if="!viewDetail && !viewLoading" description="暂无数据" />
        <el-descriptions v-if="viewDetail" :column="2" border class="view-detail-desc">
          <el-descriptions-item label="分类名称" :span="2">{{ viewDetail.name }}</el-descriptions-item>
          <el-descriptions-item label="编码">{{ viewDetail.code }}</el-descriptions-item>
          <el-descriptions-item label="排序">{{ viewDetail.dictCategorySort }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="viewDetail.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ viewDetail.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ viewDetail.remark || '--' }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ viewDetail.createAdminUsername || '--' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ viewDetail.createDate || '--' }}</el-descriptions-item>
          <el-descriptions-item label="修改人">{{ viewDetail.updateAdminUsername || '--' }}</el-descriptions-item>
          <el-descriptions-item label="修改时间">{{ viewDetail.updateDate || '--' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, View } from '@element-plus/icons-vue'
import { listDictCategory, addDictCategory, updateDictCategory, delDictCategory, delBatchDictCategory, getDictCategoryDetail } from '@/api/system/dictCategory'

const route = useRoute()

const list = ref([])
const tableTotal = ref(0)
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name || undefined,
      code: searchForm.code || undefined,
      enableStatus: searchForm.enableStatus !== '' ? searchForm.enableStatus : undefined
    }
    const res = await listDictCategory(params)
    list.value = res.data || []
    tableTotal.value = res.count || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => { fetchData() })

const searchForm = reactive({ name: '', code: '', enableStatus: '' })
function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() { searchForm.name = ''; searchForm.code = ''; searchForm.enableStatus = ''; pagination.page = 1; fetchData() }

const pagination = reactive({ page: 1, size: 10 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

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
    await ElMessageBox.confirm('确定删除选中的字典分类?', '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })
  } catch {
    return
  }
  loading.value = true
  try {
    await delBatchDictCategory(selectedRows.value)
    ElMessage.success('删除成功')
    selectedRows.value = []
    fetchData()
  } finally {
    loading.value = false
  }
}

const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑分类' : '新增分类')

// ========== 查看 ==========
const viewVisible = ref(false)
const viewDetail = ref(null)
const viewLoading = ref(false)

async function handleView(row) {
  viewVisible.value = true
  viewDetail.value = null
  viewLoading.value = true
  try {
    const res = await getDictCategoryDetail(row.id)
    viewDetail.value = res.data?.basicInfo || res.data
  } catch { /* ignore */ }
  finally { viewLoading.value = false }
}

const formData = reactive({ name: '', code: '', dictCategorySort: 0, remark: '', enableStatus: 1 })
const formRules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入编码', trigger: 'blur' }]
}

function resetForm() {
  formData.name = ''; formData.code = ''; formData.dictCategorySort = 0; formData.remark = ''; formData.enableStatus = 1
}

function handleAdd() {
  isEdit.value = false; editingId.value = null; resetForm(); dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true; editingId.value = row.id
  formData.name = row.name; formData.code = row.code
  formData.dictCategorySort = row.dictCategorySort || 0; formData.remark = row.remark || ''
  formData.enableStatus = row.enableStatus
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateDictCategory({ id: editingId.value, name: formData.name, code: formData.code, dictCategorySort: formData.dictCategorySort, remark: formData.remark, enableStatus: formData.enableStatus })
      ElMessage.success('编辑成功')
    } else {
      await addDictCategory({ name: formData.name, code: formData.code, dictCategorySort: formData.dictCategorySort, remark: formData.remark, enableStatus: formData.enableStatus })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false; resetForm(); fetchData()
  } catch { } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确认删除分类"${row.name}"吗？`, '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await delDictCategory(row.id); ElMessage.success('删除成功'); fetchData() } catch { } })
    .catch(() => {})
}
</script>

<style lang="scss" scoped>
.dict-category-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
  .table-card {
    .toolbar { margin-bottom: $gap-md; }
    .pagination-wrapper { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  .view-detail-desc :deep(.el-descriptions__label) { white-space: nowrap; min-width: 80px; }
}
</style>
