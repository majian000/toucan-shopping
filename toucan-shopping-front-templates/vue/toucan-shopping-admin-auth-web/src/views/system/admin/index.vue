<template>
  <div class="user-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="账号">
          <el-input v-model="searchForm.username" placeholder="请输入账号" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="管理员ID">
          <el-input v-model="searchForm.adminId" placeholder="请输入管理员ID" clearable style="width:240px" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.roleId" placeholder="请选择角色" clearable style="width:150px">
            <el-option v-for="r in roleOptions" :key="r.roleId" :label="r.name" :value="r.roleId" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位">
          <el-select v-model="searchForm.postId" placeholder="请选择岗位" clearable style="width:150px">
            <el-option v-for="p in postOptions" :key="p.postId" :label="p.name" :value="p.postId" />
          </el-select>
        </el-form-item>
        <el-form-item label="机构">
          <el-tree-select v-model="searchForm.orgId"
            :data="orgTreeData"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择机构" check-strictly clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable style="width:150px" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="searchForm.email" placeholder="请输入邮箱" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.sex" placeholder="请选择" clearable style="width:100px">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.enableStatus" placeholder="请选择状态" clearable style="width:120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" :icon="Plus" v-permission="'system:admin:add'" @click="handleAdd">新增用户</el-button>
          <el-button type="danger" :icon="Delete" v-permission="'system:admin:batchDelete'" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
        </div>

      </div>

      <el-table
        :data="users"
        border
        stripe
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width:100%"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="adminId" label="管理员ID" width="280" show-overflow-tooltip />
        <el-table-column prop="username" label="账号" width="120" />
        <el-table-column prop="nickName" label="昵称" width="100" />
        <el-table-column prop="roleNames" label="角色" min-width="140" show-overflow-tooltip />
        <el-table-column prop="postName" label="岗位" width="120" show-overflow-tooltip />
        <el-table-column prop="orgPath" label="所属机构" min-width="180" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" min-width="160" show-overflow-tooltip />
        <el-table-column label="性别" width="70" align="center">
          <template #default="{ row }">
            {{ row.sex === 1 || row.sex === '1' ? '男' : row.sex === 0 || row.sex === '0' ? '女' : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" sortable />
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'system:admin:edit'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link size="small" :icon="Lock" v-permission="'system:admin:resetPwd'" @click="handleResetPassword(row)">重置密码</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'system:admin:delete'" @click="handleDelete(row)">删除</el-button>
            <el-button type="danger" link size="small" :icon="SwitchButton" v-permission="'system:admin:forceLogout'" @click="handleForceLogout(row)">退出登录</el-button>
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
      width="620px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px" v-loading="dialogLoading">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="formData.nickName" placeholder="请输入昵称" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号" prop="username">
              <el-input v-model="formData.username" placeholder="请输入账号" :disabled="isEdit" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="!isEdit" :gutter="16">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="formData.password" type="password" placeholder="请输入密码" show-password maxlength="25" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPwd">
              <el-input v-model="formData.confirmPwd" type="password" placeholder="请再次输入密码" show-password maxlength="25" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="formData.sex" placeholder="请选择性别" style="width:100%">
                <el-option label="男" :value="1" />
                <el-option label="女" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="enableStatus">
              <el-switch
                v-model="formData.enableStatus"
                :active-value="1"
                :inactive-value="0"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="formData.roleIds" multiple placeholder="请选择角色" style="width:100%" :loading="roleLoading">
            <el-option v-for="r in roleOptions" :key="r.roleId" :label="r.name" :value="String(r.roleId)" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位" prop="postId">
          <el-select v-model="formData.postId" placeholder="请选择岗位" style="width:100%" :loading="postLoading" clearable>
            <el-option v-for="p in postOptions" :key="p.postId" :label="p.name" :value="String(p.postId)" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属机构" prop="orgId">
          <el-tree-select v-model="formData.orgId"
            :data="orgTreeData"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择所属机构" check-strictly clearable style="width:100%" />
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

    <!-- 重置密码弹窗 -->
    <el-dialog v-model="pwdDialogVisible" title="重置密码" width="440px" destroy-on-close>
      <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px">
        <el-form-item label="用户">
          <el-input :model-value="pwdTargetUser?.username" disabled />
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input v-model="pwdForm.password" type="password" placeholder="请输入新密码" show-password maxlength="25" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input v-model="pwdForm.confirmPwd" type="password" placeholder="请再次输入新密码" show-password maxlength="25" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="pwdLoading" @click="handleSubmitResetPwd">确认重置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, Lock, Download, SwitchButton } from '@element-plus/icons-vue'
import { listAdmin, saveAdmin, updateAdmin, roleList, postList, resetAdminPwd, delAdmin, batchDelAdmin, forceLogout } from '@/api/system/admin'
import { listOrgnazitionTree } from '@/api/system/orgnazition'

const route = useRoute()

const users = ref([])
const tableTotal = ref(0)

async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      username: searchForm.username || undefined,
      adminId: searchForm.adminId || undefined,
      roleId: searchForm.roleId || undefined,
      postId: searchForm.postId || undefined,
      orgId: searchForm.orgId || undefined,
      phone: searchForm.phone || undefined,
      email: searchForm.email || undefined,
      sex: searchForm.sex != null ? searchForm.sex : undefined,
      enableStatus: searchForm.enableStatus ?? undefined
    }
    const res = await listAdmin(params)
    users.value = res.data.list || []
    tableTotal.value = res.data.total || 0
  } catch (e) {
    console.error('获取管理员列表失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  Promise.all([fetchRoles(), fetchPosts(), fetchOrgTree()])
  fetchData()
})

// ========== 搜索 ==========
const searchForm = reactive({
  username: '', adminId: '', roleId: '', postId: '',
  orgId: null, phone: '', email: '', sex: null, enableStatus: ''
})

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() {
  Object.assign(searchForm, {
    username: '', adminId: '', roleId: '', postId: '',
    orgId: null, phone: '', email: '', sex: null, enableStatus: ''
  })
  pagination.page = 1; fetchData()
}

// ========== 分页 ==========
const pagination = reactive({ page: 1, size: 10 })
watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

// ========== 选择 ==========
const selectedIds = ref([])
function handleSelectionChange(selection) { selectedIds.value = selection.map(s => s.id) }

const loading = ref(false)

// ========== 新增/编辑弹窗 ==========
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const editingAdminId = ref(null)
const submitLoading = ref(false)
const dialogLoading = ref(false)
const formRef = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑用户' : '新增用户')

const roleOptions = ref([])
const roleLoading = ref(false)
const postOptions = ref([])
const postLoading = ref(false)
const orgTreeData = ref([])

async function fetchRoles() {
  roleLoading.value = true
  try {
    const res = await roleList()
    roleOptions.value = res.data || []
  } catch { /* ignore */ }
  finally { roleLoading.value = false }
}

async function fetchPosts() {
  postLoading.value = true
  try {
    const res = await postList()
    postOptions.value = res.data || []
  } catch { /* ignore */ }
  finally { postLoading.value = false }
}

async function fetchOrgTree() {
  try {
    const res = await listOrgnazitionTree()
    orgTreeData.value = res.data || []
  } catch { /* ignore */ }
}

const formData = reactive({ username: '', password: '', confirmPwd: '', nickName: '', phone: '', email: '', sex: '', roleIds: [], postId: null, orgId: null, remark: '', enableStatus: 1 })

const validateConfirmPwd = (rule, value, callback) => {
  if (value !== formData.password) callback(new Error('两次输入的密码不一致'))
  else callback()
}

const formRules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 25, message: '密码长度6-25位', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPwd, trigger: 'blur' }
  ],
  nickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}

function resetForm() {
  formData.username = ''
  formData.password = ''
  formData.confirmPwd = ''
  formData.nickName = ''
  formData.phone = ''
  formData.email = ''
  formData.sex = ''
  formData.roleIds = []
  formData.postId = null
  formData.orgId = null
  formData.remark = ''
  formData.enableStatus = 1
}

async function handleAdd() {
  isEdit.value = false
  editingId.value = null
  resetForm()
  dialogVisible.value = true
  dialogLoading.value = true
  await Promise.all([fetchRoles(), fetchPosts(), fetchOrgTree()])
  dialogLoading.value = false
}

async function handleEdit(row) {
  isEdit.value = true
  editingId.value = row.id
  editingAdminId.value = row.adminId
  formData.username = row.username
  formData.password = ''
  formData.nickName = row.nickName || ''
  formData.phone = row.phone || ''
  formData.email = row.email || ''
  formData.sex = row.sex || ''
  formData.orgId = null
  formData.remark = row.remark || ''
  formData.enableStatus = row.enableStatus
  const ids = row.roleIdsString ? String(row.roleIdsString).split(',').map(s => s.trim()).filter(Boolean) : []
  formData.roleIds = ids
  formData.postId = row.postId != null ? String(row.postId) : null
  dialogVisible.value = true
  dialogLoading.value = true
  await Promise.all([fetchRoles(), fetchPosts(), fetchOrgTree()])
  // 树数据加载完后再设置 orgId（转为字符串，与 tree 节点 id 类型一致）
  formData.orgId = row.orgId != null ? String(row.orgId) : null
  await nextTick()
  dialogLoading.value = false
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateAdmin({
        id: editingId.value,
        adminId: editingAdminId.value,
        nickName: formData.nickName,
        phone: formData.phone,
        email: formData.email,
        sex: formData.sex,
        roleIds: formData.roleIds,
        postId: formData.postId,
        orgId: formData.orgId,
        remark: formData.remark,
        enableStatus: formData.enableStatus
      })
      ElMessage.success('编辑成功')
      fetchData()
    } else {
      await saveAdmin({
        username: formData.username,
        password: formData.password,
        nickName: formData.nickName,
        phone: formData.phone,
        email: formData.email,
        sex: formData.sex,
        roleIds: formData.roleIds,
        postId: formData.postId,
        orgId: formData.orgId,
        remark: formData.remark,
        enableStatus: formData.enableStatus
      })
      ElMessage.success('新增成功')
      fetchData()
    }
    dialogVisible.value = false
    resetForm()
  } catch {
    // 错误信息由 request.js 拦截器统一处理
  } finally {
    submitLoading.value = false
  }
}

// ========== 切换状态 ==========
function handleToggleStatus(row, val) {
  const newStatus = val ? 1 : 0
  if (row.enableStatus === newStatus) return
  row.enableStatus = newStatus
  ElMessage.success(`已${val ? '启用' : '禁用'}`)
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm(`确认删除用户"${row.username}"吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delAdmin(row.id); ElMessage.success('删除成功'); fetchData() } catch { /* 拦截器处理 */ }
  }).catch(() => {})
}

function handleForceLogout(row) {
  ElMessageBox.confirm(`确认强制退出"${row.username}"的登录吗？将立即清除其登录会话。`, '退出登录确认', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await forceLogout(row.adminId); ElMessage.success(`「${row.username}」已被强制退出登录`); } catch { /* 拦截器处理 */ }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的用户')
    return
  }
  ElMessageBox.confirm(`确认删除选中的 ${selectedIds.value.length} 个用户吗？`, '批量删除', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await batchDelAdmin(selectedIds.value)
      selectedIds.value = []
      ElMessage.success('删除成功')
      fetchData()
    } catch { /* 拦截器处理 */ }
  }).catch(() => {})
}

// ========== 重置密码 ==========
const pwdDialogVisible = ref(false)
const pwdTargetUser = ref(null)
const pwdLoading = ref(false)
const pwdFormRef = ref(null)
const pwdForm = reactive({ password: '', confirmPwd: '' })

const validatePwdConfirm = (rule, value, callback) => {
  if (value !== pwdForm.password) callback(new Error('两次输入的密码不一致'))
  else callback()
}

const pwdRules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 25, message: '密码长度6-25位', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePwdConfirm, trigger: 'blur' }
  ]
}

function handleResetPassword(row) {
  pwdTargetUser.value = row
  pwdForm.password = ''
  pwdForm.confirmPwd = ''
  pwdDialogVisible.value = true
}

async function handleSubmitResetPwd() {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return
  pwdLoading.value = true
  try {
    await resetAdminPwd({
      adminId: pwdTargetUser.value.adminId,
      password: pwdForm.password
    })
    ElMessage.success(`「${pwdTargetUser.value.username}」密码已重置`)
    pwdDialogVisible.value = false
  } catch {
    // 错误由拦截器统一处理
  } finally {
    pwdLoading.value = false
  }
}

// ========== 导出 ==========
function handleExport() {
  ElMessage.success('正在导出用户数据...')
}
</script>

<style lang="scss" scoped>
.user-management {
  .search-card {
    margin-bottom: $gap-md;
    :deep(.el-card__body) { padding: 16px 20px 0; }
  }
  .table-card {
    .toolbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: $gap-md;
      .toolbar-left { display: flex; gap: $gap-sm; }
    }
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
