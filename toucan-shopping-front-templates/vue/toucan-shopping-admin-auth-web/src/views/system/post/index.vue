<template>
  <div class="post-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="岗位名称">
          <el-input v-model="searchForm.name" placeholder="请输入岗位名称" clearable style="width:220px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.enableStatus" placeholder="请选择状态" clearable style="width:140px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" v-permission="'system:post:list'" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" v-permission="'system:post:list'" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'system:post:add'" @click="handleAdd">新增岗位</el-button>
      </div>

      <el-table :data="posts" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="岗位名称" width="150" />
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" sortable />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'system:post:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'system:post:delete'" @click="handleDelete(row)">删除</el-button>
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
        <el-form-item label="岗位名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入岗位名称" maxlength="20" />
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
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit } from '@element-plus/icons-vue'
import { listPost, addPost, updatePost, delPost } from '@/api/system/post'

const route = useRoute()

// ========== 岗位列表数据 ==========
const posts = ref([])
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
      enableStatus: searchForm.enableStatus !== '' ? searchForm.enableStatus : undefined
    }
    const res = await listPost(params)
    posts.value = res.data.rows || res.data.list || []
    tableTotal.value = res.data.total || 0
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

const dialogTitle = computed(() => isEdit.value ? '编辑岗位' : '新增岗位')

const formData = reactive({ name: '', remark: '', enableStatus: 1 })

const formRules = {
  name: [{ required: true, message: '请输入岗位名称', trigger: 'blur' }],
  remark: []
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
      await updatePost({
        id: editingId.value,
        name: formData.name,
        remark: formData.remark,
        enableStatus: formData.enableStatus
      })
      ElMessage.success('编辑成功')
    } else {
      await addPost({
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
  ElMessageBox.confirm(`确认删除岗位"${row.name}"吗？删除后不可恢复。`, '删除确认', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await delPost(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch {
      // error handled by request interceptor
    }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.post-management {
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
