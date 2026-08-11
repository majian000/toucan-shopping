<template>
  <div class="message-user-management">
    <h2 class="page-title">用户消息管理</h2>

    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userMainId" placeholder="请输入用户ID" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width:140px">
            <el-option label="未读" :value="0" />
            <el-option label="已读" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="消息类型">
          <el-select v-model="searchForm.messageTypeCode" placeholder="全部" clearable style="width:180px">
            <el-option v-for="mt in messageTypeList" :key="mt.code" :label="mt.name" :value="mt.code" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'toucan:content:messageUser:toolbar:save'" @click="handleAdd">发送消息</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="id" label="主键" width="200" show-overflow-tooltip />
        <el-table-column prop="title" label="消息标题" width="200" show-overflow-tooltip />
        <el-table-column prop="content" label="消息内容" min-width="200" show-overflow-tooltip />
        <el-table-column label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.contentType === '1'" size="small">文本</el-tag>
            <span v-else>{{ row.contentType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userMainId" label="用户ID" width="180" show-overflow-tooltip />
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '0' ? 'warning' : 'success'" size="small">
              {{ row.status === 0 ? '未读' : '已读' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="messageTypeCode" label="消息编码" width="150" />
        <el-table-column prop="messageTypeName" label="消息名称" width="120" />
        <el-table-column prop="sendDate" label="发送时间" width="170" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:content:messageUser:btn:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:content:messageUser:row:delete'" @click="handleDelete(row)">删除</el-button>
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
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="560px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" v-loading="dialogLoading">
        <!-- 新增时才显示用户范围 -->
        <el-form-item v-if="!isEdit" label="用户范围" prop="userScope">
          <el-radio-group v-model="formData.userScope">
            <el-radio :value="1">指定用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="!isEdit && formData.userScope === 1" label="接收用户" prop="userMainIdListString">
          <div style="display:flex;gap:8px;width:100%">
            <el-input
              v-model="formData.userMainIdListString"
              type="textarea"
              :rows="3"
              placeholder="请输入用户ID，多个用逗号分隔"
              maxlength="9500"
              style="flex:1"
            />
            <el-button :icon="User" @click="openUserSelectDialog" style="height:auto">选择用户</el-button>
          </div>
          <div class="form-tip">填写用户ID，多个以英文逗号分隔，最多500个用户</div>
        </el-form-item>
        <el-form-item label="消息标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入消息标题" maxlength="255" />
        </el-form-item>
        <el-form-item label="发送时间" prop="sendDate">
          <el-date-picker
            v-model="formData.sendDate"
            type="datetime"
            placeholder="请选择发送时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="消息内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="3" placeholder="请输入消息内容" maxlength="500" />
        </el-form-item>
        <el-form-item label="消息类型" prop="messageTypeCode">
          <el-select v-model="formData.messageTypeCode" placeholder="请选择消息类型" style="width:100%">
            <el-option v-for="mt in messageTypeList" :key="mt.code" :label="mt.name" :value="mt.code" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 用户选择弹窗 -->
    <el-dialog
      v-model="userSelectVisible"
      title="选择用户"
      width="80%"
      :close-on-click-modal="false"
      destroy-on-close
      @opened="onUserDialogOpened"
    >
      <!-- 搜索 -->
      <el-form :model="userSearchForm" :inline="true">
        <el-form-item label="用户ID">
          <el-input v-model="userSearchForm.userMainId" placeholder="请输入用户ID" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userSearchForm.mobilePhone" placeholder="请输入手机号" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userSearchForm.email" placeholder="请输入邮箱" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="userSearchForm.username" placeholder="请输入用户名" clearable style="width:160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchUsers">搜索</el-button>
          <el-button :icon="RefreshRight" @click="resetUserSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 已选提示 -->
      <div v-if="selectedUsers.length > 0" style="margin-bottom:12px">
        <span style="color:#999">已选 <b>{{ selectedUsers.length }}</b> 个用户：</span>
        <el-tag
          v-for="(uid, idx) in selectedUsers"
          :key="uid"
          closable
          size="small"
          style="margin:2px 4px"
          @close="removeSelectedUser(uid)"
        >{{ uid }}</el-tag>
      </div>

      <!-- 表格 -->
      <el-table
        ref="userTableRef"
        :data="userTableData"
        border stripe
        v-loading="userLoading"
        row-key="userMainId"
        @selection-change="onUserSelectionChange"
        max-height="400"
      >
        <el-table-column type="selection" width="50" align="center" :reserve-selection="true" />
        <el-table-column prop="userMainId" label="用户ID" width="180" show-overflow-tooltip />
        <el-table-column prop="mobilePhone" label="手机号" width="160" />
        <el-table-column prop="nickName" label="昵称" width="160" show-overflow-tooltip />
        <el-table-column prop="email" label="邮箱" width="200" show-overflow-tooltip />
        <el-table-column prop="username" label="用户名" width="160" show-overflow-tooltip />
        <el-table-column prop="trueName" label="真实姓名" width="120" />
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 || row.enableStatus === '1' ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 || row.enableStatus === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="注册时间" width="170" />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="userPagination.page"
          v-model:page-size="userPagination.size"
          :page-sizes="[15, 30, 100, 300]"
          :total="userTableTotal"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="onUserSizeChange"
          @current-change="onUserPageChange"
        />
      </div>

      <template #footer>
        <el-button @click="userSelectVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUserSelect">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Search, RefreshRight, User } from '@element-plus/icons-vue'
import { listMessageUser, sendMessage, updateMessage, delMessage, listAllMessageType } from '@/api/message/messageUser'
import { listUser } from '@/api/user'

// ========== 消息类型列表（用于搜索和表单下拉） ==========
const messageTypeList = ref([])

async function loadMessageTypes() {
  try {
    const res = await listAllMessageType()
    messageTypeList.value = res.data || []
  } catch { }
}

// ========== 搜索 ==========
const searchForm = reactive({ userMainId: '', status: '', messageTypeCode: '' })

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() { searchForm.userMainId = ''; searchForm.status = ''; searchForm.messageTypeCode = ''; pagination.page = 1; fetchData() }

// ========== 分页 ==========
const pagination = reactive({ page: 1, size: 15 })
const tableTotal = ref(0)

function handlePageChange(page) { pagination.page = page; fetchData() }
function handleSizeChange(size) { pagination.size = size; pagination.page = 1; fetchData() }

// ========== 表格 ==========
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])

function onSelectionChange(rows) { selectedRows.value = rows }

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      limit: pagination.size,
      userMainId: searchForm.userMainId || undefined,
      status: searchForm.status !== '' ? searchForm.status : undefined,
      messageTypeCode: searchForm.messageTypeCode || undefined
    }
    const res = await listMessageUser(params)
    tableData.value = res.data || []
    tableTotal.value = res.count || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => { loadMessageTypes(); fetchData() })

// ========== 新增/编辑 ==========
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const submitLoading = ref(false)
const formRef = ref(null)

const dialogTitle = computed(() => isEdit.value ? '编辑消息' : '发送消息')

const formData = reactive({
  userScope: 1, userMainIdListString: '',
  title: '', sendDate: '', content: '', messageTypeCode: '',
  userMainId: null, messageBodyId: null
})

const formRules = computed(() => {
  const rules = {
    title: [{ required: true, message: '请输入消息标题', trigger: 'blur' }],
    sendDate: [{ required: true, message: '请选择发送时间', trigger: 'change' }],
    messageTypeCode: [{ required: true, message: '请选择消息类型', trigger: 'change' }]
  }
  if (!isEdit.value) {
    rules.userMainIdListString = [{ required: true, message: '请输入接收用户ID', trigger: 'blur' }]
  }
  return rules
})

function resetForm() {
  formData.userScope = 1; formData.userMainIdListString = ''
  formData.title = ''; formData.sendDate = ''; formData.content = ''; formData.messageTypeCode = ''
  formData.userMainId = null; formData.messageBodyId = null
}

function handleAdd() {
  isEdit.value = false; editingId.value = null
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true; editingId.value = String(row.id)
  formData.userMainId = row.userMainId != null ? String(row.userMainId) : null
  formData.messageBodyId = row.messageBodyId != null ? String(row.messageBodyId) : null
  formData.title = row.title || ''
  formData.sendDate = row.sendDate || ''
  formData.content = row.content || ''
  formData.messageTypeCode = row.messageTypeCode || ''
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateMessage({
        id: editingId.value,
        userMainId: formData.userMainId,
        messageBodyId: formData.messageBodyId,
        title: formData.title,
        content: formData.content,
        sendDate: formData.sendDate,
        messageTypeCode: formData.messageTypeCode
      })
      ElMessage.success('编辑成功')
    } else {
      await sendMessage({
        userScope: formData.userScope,
        userMainIdListString: formData.userMainIdListString,
        title: formData.title,
        content: formData.content,
        sendDate: formData.sendDate,
        messageTypeCode: formData.messageTypeCode
      })
      ElMessage.success('发送成功')
    }
    dialogVisible.value = false; resetForm(); fetchData()
  } catch { } finally { submitLoading.value = false }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm(`确定删除该消息吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delMessage({ id: String(row.id) }); ElMessage.success('删除成功'); fetchData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 条消息吗？`, '批量删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      for (const row of selectedRows.value) {
        await delMessage({ id: String(row.id) })
      }
      ElMessage.success('批量删除成功'); fetchData()
    } catch { }
  }).catch(() => {})
}

// ========== 用户选择弹窗 ==========
const userSelectVisible = ref(false)
const userLoading = ref(false)
const userTableRef = ref(null)
const userTableData = ref([])
const userTableTotal = ref(0)
const userSearchForm = reactive({ userMainId: '', mobilePhone: '', email: '', username: '' })
const userPagination = reactive({ page: 1, size: 15 })
const selectedUsers = ref([])

function onUserDialogOpened() {
  if (userTableData.value.length === 0) {
    fetchUsers()
  }
}

async function fetchUsers() {
  userLoading.value = true
  try {
    const params = {
      page: userPagination.page,
      limit: userPagination.size,
      userMainId: userSearchForm.userMainId || undefined,
      mobilePhone: userSearchForm.mobilePhone || undefined,
      email: userSearchForm.email || undefined,
      username: userSearchForm.username || undefined
    }
    const res = await listUser(params)
    userTableData.value = res.data || []
    userTableTotal.value = res.count || 0
  } finally {
    userLoading.value = false
  }
}

function searchUsers() {
  userPagination.page = 1
  fetchUsers()
}

function resetUserSearch() {
  userSearchForm.userMainId = ''
  userSearchForm.mobilePhone = ''
  userSearchForm.email = ''
  userSearchForm.username = ''
  userPagination.page = 1
  fetchUsers()
}

function onUserPageChange(page) { userPagination.page = page; fetchUsers() }
function onUserSizeChange(size) { userPagination.size = size; userPagination.page = 1; fetchUsers() }

function onUserSelectionChange(rows) {
  selectedUsers.value = rows.map(r => String(r.userMainId))
}

function removeSelectedUser(uid) {
  selectedUsers.value = selectedUsers.value.filter(u => u !== uid)
}

function openUserSelectDialog() {
  userSearchForm.userMainId = ''
  userSearchForm.mobilePhone = ''
  userSearchForm.email = ''
  userSearchForm.username = ''
  userPagination.page = 1
  selectedUsers.value = []
  userTableData.value = []
  userTableTotal.value = 0
  userSelectVisible.value = true
}

function confirmUserSelect() {
  const existing = formData.userMainIdListString
    ? formData.userMainIdListString.split(',').map(s => s.trim()).filter(Boolean)
    : []
  for (const uid of selectedUsers.value) {
    if (!existing.includes(uid)) {
      existing.push(uid)
    }
  }
  formData.userMainIdListString = existing.join(',')
  userSelectVisible.value = false
}
</script>

<style lang="scss" scoped>
.message-user-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card {
    .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrapper { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
  .form-tip { color: $text-secondary; font-size: 12px; margin-top: 4px; }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
