<template>
  <div class="app-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline @keyup.enter="handleSearch">
        <el-form-item label="应用编码">
          <el-input v-model="searchForm.code" placeholder="请输入应用编码" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="应用名称">
          <el-input v-model="searchForm.name" placeholder="请输入应用名称" clearable style="width:180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'pms:system:app:add'" @click="handleAdd">新增应用</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="code" label="应用编码" width="160" />
        <el-table-column prop="name" label="应用名称" width="180" />
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'pms:system:app:edit-row'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="info" link size="small" :icon="View" v-permission="'pms:system:app:show'" @click="handleView(row)">查看</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'pms:system:app:delete-row'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 查看弹窗 -->
      <el-dialog v-model="viewVisible" title="应用详情" width="500px">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="应用编码">{{ viewData.code }}</el-descriptions-item>
          <el-descriptions-item label="应用名称">{{ viewData.name }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="viewData.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ viewData.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注">{{ viewData.remark || '--' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ viewData.createDate || '--' }}</el-descriptions-item>
        </el-descriptions>
      </el-dialog>

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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="应用编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入应用编码" :disabled="isEdit" maxlength="32" />
        </el-form-item>
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入应用名称" maxlength="50" />
        </el-form-item>
        <el-form-item label="状态" prop="enableStatus">
          <el-switch v-model="formData.enableStatus" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" placeholder="请输入备注" type="textarea" maxlength="255" />
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
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, View } from '@element-plus/icons-vue'
import { listApp, saveApp, updateApp, delApp } from '@/api/system/app'

const route = useRoute()

const tableData = ref([])
const tableTotal = ref(0)
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      code: searchForm.code || undefined,
      name: searchForm.name || undefined
    }
    const res = await listApp(params)
    tableData.value = res.data || []
    tableTotal.value = res.count || 0
  } catch { /* ignore */ }
  finally { loading.value = false }
}

onMounted(fetchData)

const searchForm = reactive({ code: '', name: '' })
function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() { searchForm.code = ''; searchForm.name = '' }

const pagination = reactive({ page: 1, size: 10 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

const dialogVisible = ref(false)
const viewVisible = ref(false)
const viewData = ref({})
const isEdit = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑应用' : '新增应用')

const formData = reactive({ code: '', name: '', remark: '', enableStatus: 1 })
const formRules = {
  code: [{ required: true, message: '请输入应用编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入应用名称', trigger: 'blur' }]
}

function resetForm() { formData.code = ''; formData.name = ''; formData.remark = ''; formData.enableStatus = 1 }

function handleAdd() { isEdit.value = false; resetForm(); dialogVisible.value = true }
function handleView(row) { viewData.value = row; viewVisible.value = true }

function handleEdit(row) {
  isEdit.value = true
  editingId.value = row.id
  formData.code = row.code
  formData.name = row.name
  formData.remark = row.remark || ''
  formData.enableStatus = row.enableStatus
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateApp({ id: editingId.value, code: formData.code, name: formData.name, remark: formData.remark, enableStatus: formData.enableStatus })
      ElMessage.success('编辑成功')
    } else {
      await saveApp({ code: formData.code, name: formData.name, remark: formData.remark, enableStatus: formData.enableStatus })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch { /* ignore */ }
  finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确认删除应用"${row.name}"吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delApp(row.id); ElMessage.success('删除成功'); fetchData() } catch { /* ignore */ }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.app-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
  .table-card {
    .toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: $gap-md; }
    .pagination-wrapper { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
}
</style>
