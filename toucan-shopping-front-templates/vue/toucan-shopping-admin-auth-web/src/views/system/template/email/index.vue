<template>
  <div class="email-template-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="模板名称">
          <el-input v-model="searchForm.name" placeholder="请输入模板名称" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="模板编码">
          <el-input v-model="searchForm.code" placeholder="请输入模板编码" clearable style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" v-permission="'system:emailTemplate:list'" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" v-permission="'system:emailTemplate:list'" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'system:emailTemplate:add'" @click="handleAdd">新增模板</el-button>
        <el-button type="success" :icon="Refresh" v-permission="'system:emailTemplate:list'" @click="handleRefreshCache">刷新缓存</el-button>
      </div>

      <el-table :data="templates" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="模板名称" width="150" />
        <el-table-column prop="code" label="模板编码" width="150" show-overflow-tooltip />
        <el-table-column prop="subject" label="邮件主题" width="180" show-overflow-tooltip />
        <el-table-column prop="content" label="模板内容" min-width="250" show-overflow-tooltip />
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
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'system:emailTemplate:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'system:emailTemplate:delete'" @click="handleDelete(row)">删除</el-button>
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
      width="650px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入模板名称" maxlength="100" />
        </el-form-item>
        <el-form-item label="模板编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入模板编码（唯一）" maxlength="100" />
        </el-form-item>
        <el-form-item label="邮件主题" prop="subject">
          <el-input v-model="formData.subject" placeholder="请输入邮件主题" maxlength="200" />
        </el-form-item>
        <el-form-item label="模板内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            :rows="8"
            placeholder="请输入邮件模板内容（支持HTML），变量可使用 ${变量名} 占位"
            maxlength="5000"
            show-word-limit
          />
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
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" maxlength="255" />
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
      title="查看邮件模板"
      width="650px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="模板名称">{{ viewData.name }}</el-descriptions-item>
        <el-descriptions-item label="模板编码">{{ viewData.code }}</el-descriptions-item>
        <el-descriptions-item label="邮件主题" :span="2">{{ viewData.subject || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.enableStatus === 1 ? 'success' : 'danger'" size="small">
            {{ viewData.enableStatus === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="模板内容" :span="2">
          <div class="email-content-preview" v-html="viewData.content || '无内容'"></div>
        </el-descriptions-item>
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
import { listEmailTemplate, addEmailTemplate, updateEmailTemplate, delEmailTemplate, refreshEmailTemplateCache } from '@/api/system/emailTemplate'

const route = useRoute()

const templates = ref([])
const tableTotal = ref(0)
const loading = ref(false)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name || undefined,
      code: searchForm.code || undefined
    }
    const res = await listEmailTemplate(params)
    templates.value = res.data.rows || res.data.list || []
    tableTotal.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchData())

const searchForm = reactive({ name: '', code: '' })

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() {
  searchForm.name = ''
  searchForm.code = ''
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
  name: '', code: '', subject: '', content: '',
  enableStatus: 1, remark: '', createDate: '', updateDate: ''
})

function handleView(row) {
  viewData.name = row.name
  viewData.code = row.code
  viewData.subject = row.subject || ''
  viewData.content = row.content || ''
  viewData.enableStatus = row.enableStatus
  viewData.remark = row.remark || ''
  viewData.createDate = row.createDate || ''
  viewData.updateDate = row.updateDate || ''
  viewDialogVisible.value = true
}

// 刷新缓存
async function handleRefreshCache() {
  try {
    const res = await refreshEmailTemplateCache()
    ElMessage.success(res.data?.msg || res.msg || '刷新成功')
  } catch {
    // error handled by request interceptor
  }
}

const dialogTitle = computed(() => isEdit.value ? '编辑邮件模板' : '新增邮件模板')

const formData = reactive({
  name: '',
  code: '',
  subject: '',
  content: '',
  remark: '',
  enableStatus: 1
})

const formRules = {
  name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入模板编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9._]*$/, message: '编码只能使用英文、数字、小数点、下划线，且不能以数字开头', trigger: 'blur' }
  ],
  subject: [],
  content: [],
  remark: []
}

function resetForm() {
  formData.name = ''
  formData.code = ''
  formData.subject = ''
  formData.content = ''
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
  formData.code = row.code
  formData.subject = row.subject || ''
  formData.content = row.content || ''
  formData.remark = row.remark || ''
  formData.enableStatus = row.enableStatus
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const payload = {
      name: formData.name,
      code: formData.code,
      subject: formData.subject,
      content: formData.content,
      remark: formData.remark,
      enableStatus: formData.enableStatus
    }
    if (isEdit.value) {
      await updateEmailTemplate({ id: editingId.value, ...payload })
      ElMessage.success('编辑成功')
    } else {
      await addEmailTemplate(payload)
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
  ElMessageBox.confirm(`确认删除模板"${row.name}"吗？删除后不可恢复。`, '删除确认', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await delEmailTemplate(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch {
      // error handled by request interceptor
    }
  }).catch(() => {})
}
</script>

<style lang="scss" scoped>
.email-template-management {
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

  .email-content-preview {
    max-height: 400px;
    overflow-y: auto;
    padding: 8px;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: #fafafa;
  }
}
</style>
