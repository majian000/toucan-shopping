<template>
  <div class="user-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="账号ID">
          <el-input v-model="searchForm.adminId" placeholder="请输入账号ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="searchForm.username" placeholder="请输入账号" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="账号状态">
          <el-select v-model="searchForm.enableStatus" placeholder="请选择" clearable style="width:120px">
            <el-option label="全部" :value="-1" />
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="searchForm.realName" placeholder="请输入真实姓名" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="searchForm.email" placeholder="请输入邮箱" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="请选择" clearable style="width:100px">
            <el-option label="全部" value="" />
            <el-option label="男" :value="1" />
            <el-option label="女" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="searchForm.idCard" placeholder="请输入身份证号" clearable style="width:180px" />
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
          <el-button type="primary" :icon="Plus" v-permission="'toucan:admin:admin:toolbar:save'" @click="handleAdd">新增用户</el-button>
          <el-button type="danger" :icon="Delete" v-permission="'toucan:admin:admin:toolbar:delete'" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
        </div>
      </div>

      <el-table
        :data="users"
        border
        stripe
        v-loading="loading"
        @selection-change="handleSelectionChange"
        :default-sort="{ prop: 'createDate', order: 'descending' }"
        @sort-change="handleSortChange"
        style="width:100%"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="id" label="主键" width="200" />
        <el-table-column prop="adminId" label="账号ID" width="170" show-overflow-tooltip />
        <el-table-column prop="username" label="账号" width="100" />
        <el-table-column label="状态" width="85" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 || row.enableStatus === '1' ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 || row.enableStatus === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="nickName" label="昵称" width="100" />
        <el-table-column prop="realName" label="真实姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip />
        <el-table-column label="性别" width="65" align="center">
          <template #default="{ row }">
            {{ row.gender === 1 || row.gender === '1' ? '男' : row.gender === 0 || row.gender === '0' ? '女' : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证号" width="175" show-overflow-tooltip />
        <el-table-column prop="birthday" label="出生日期" width="165" />
        <el-table-column prop="address" label="地址" width="200" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" width="150" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="165" sortable="custom" />
        <el-table-column prop="createAdminUsername" label="创建人" width="100" />
        <el-table-column prop="updateDate" label="修改时间" width="165" />
        <el-table-column prop="updateAdminUsername" label="修改人" width="100" />
        <el-table-column label="操作" width="420" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:admin:admin:row:update'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:admin:admin:row:delete'" @click="handleDelete(row)">删除</el-button>
            <el-button type="warning" link size="small" :icon="UserFilled" v-permission="'toucan:admin:admin:row:role'" @click="handleRole(row)">角色</el-button>
            <el-button type="success" link size="small" :icon="Share" v-permission="'pms:system:user:org'" @click="handleOrgnazition(row)">组织机构</el-button>
            <el-button type="warning" link size="small" :icon="Lock" v-permission="'toucan:admin:admin:row:password'" @click="handlePassword(row)">修改密码</el-button>
            <el-button type="info" link size="small" :icon="View" v-permission="'toucan:admin:admin:row:show'" @click="handleViewAdmin(row)">查看</el-button>
            <el-button type="info" link size="small" :icon="EditPen" v-permission="'toucan:admin:adminInfo:row:update'" @click="handleInfo(row)">完善信息</el-button>
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
        <el-form-item label="账号" prop="username">
          <el-input v-model="formData.username" placeholder="请输入账号" :disabled="isEdit" maxlength="20" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="formData.password" type="password" placeholder="请输入密码" show-password maxlength="25" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="确认密码" prop="confirmPwd">
          <el-input v-model="formData.confirmPwd" type="password" placeholder="请再次输入密码" show-password maxlength="25" />
        </el-form-item>
        <el-form-item label="账号状态" prop="enableStatus">
          <el-switch
            v-model="formData.enableStatus"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="formData.remark" placeholder="请输入备注信息" type="textarea" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 角色弹窗 -->
    <el-dialog
      v-model="roleDialogVisible"
      title="选择角色"
      width="560px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div v-loading="roleTreeLoading">
        <!-- 已选角色列表 -->
        <div class="checked-roles-bar" v-if="checkedRoleInfos.length > 0">
          <span class="checked-roles-label">已选角色：</span>
          <el-tag
            v-for="r in checkedRoleInfos"
            :key="r.id"
            size="small"
            closable
            @close="handleUncheckRole(r)"
          >
            {{ r.title }}
          </el-tag>
        </div>
        <div class="checked-roles-bar checked-roles-bar--empty" v-else>
          <span class="text-muted">未选择任何角色（提交空将清空角色）</span>
        </div>

        <div style="max-height:340px;overflow-y:auto">
          <el-tree
            ref="roleTreeRef"
            :data="roleTreeData"
            show-checkbox
            node-key="id"
            :props="{ label: 'title', children: 'children' }"
            default-expand-all
            @check="handleRoleTreeCheck"
          />
        </div>
      </div>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="roleTreeLoading" @click="handleSubmitRole">确定</el-button>
      </template>
    </el-dialog>

    <!-- 组织机构弹窗 -->
    <el-dialog
      v-model="orgDialogVisible"
      title="选择组织机构"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div v-loading="orgTreeLoading" element-loading-text="加载中...">
        <div class="org-tree-toolbar">
          <el-input v-model="orgFilterText" placeholder="搜索组织机构..." :prefix-icon="Search" clearable style="width:200px" />
        </div>
        <div v-if="orgForm.appCode" class="org-tree-body">
          <el-tree
            ref="orgTreeRef"
            :data="orgTreeData"
            show-checkbox
            node-key="id"
            :props="{ label: 'title', children: 'children' }"
            :default-checked-keys="orgCheckedKeys"
            :filter-node-method="filterOrgNode"
            default-expand-all
            highlight-current
            check-strictly
          >
            <template #default="{ node }">
              <span class="org-tree-node">
                <el-icon><Folder /></el-icon>
                <span>{{ node.label }}</span>
              </span>
            </template>
          </el-tree>
        </div>
        <el-empty v-if="orgForm.appCode && orgTreeData.length === 0" description="暂无组织机构数据" :image-size="80" />
      </div>
      <template #footer>
        <el-button @click="orgDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="orgTreeLoading" @click="handleSubmitOrg" :disabled="!orgForm.appCode">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="pwdDialogVisible" title="修改密码" width="440px" destroy-on-close>
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
        <el-button type="primary" :loading="pwdLoading" @click="handleSubmitResetPwd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 完善信息弹窗 -->
    <el-dialog
      v-model="infoDialogVisible"
      title="完善信息"
      width="620px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form ref="infoFormRef" :model="infoForm" :rules="infoRules" label-width="80px" v-loading="infoLoading">
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="infoForm.nickName" placeholder="请输入昵称" maxlength="50" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="infoForm.realName" placeholder="请输入真实姓名" maxlength="50" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="infoForm.phone" placeholder="请输入手机号" maxlength="20" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="infoForm.email" placeholder="请输入邮箱" maxlength="100" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="infoForm.gender" placeholder="请选择性别" style="width:100%">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="infoForm.idCard" placeholder="请输入身份证号" maxlength="18" />
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker v-model="infoForm.birthday" type="datetime" placeholder="请选择出生日期" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="infoForm.address" placeholder="请输入地址" type="textarea" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="infoDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="infoSubmitLoading" @click="handleSubmitInfo">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看弹窗 -->
    <el-dialog v-model="viewVisible" title="账号详情" width="800px">
      <el-tabs v-model="viewActiveTab" v-loading="viewLoading" class="view-tabs">
        <el-tab-pane label="基本信息" name="info">
          <el-descriptions :column="2" border class="view-descriptions">
            <el-descriptions-item label="账号">{{ viewData.username }}</el-descriptions-item>
            <el-descriptions-item label="昵称">{{ viewData.nickName || '--' }}</el-descriptions-item>
            <el-descriptions-item label="真实姓名">{{ viewData.realName || '--' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ viewData.phone || '--' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ viewData.email || '--' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ viewData.gender === 1 ? '男' : viewData.gender === 0 ? '女' : '--' }}</el-descriptions-item>
            <el-descriptions-item label="身份证号" :span="2">{{ viewData.idCard || '--' }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{ viewData.birthday || '--' }}</el-descriptions-item>
            <el-descriptions-item label="地址">{{ viewData.address || '--' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="viewData.enableStatus === 1 ? 'success' : 'danger'" size="small">
                {{ viewData.enableStatus === 1 ? '启用' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建人">{{ viewData.createAdminUsername || '--' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间" :span="2">{{ viewData.createDate || '--' }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '--' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="关联角色" name="role">
          <el-table :data="viewRoles" border size="small" max-height="300" v-if="viewRoles.length > 0">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="roleName" label="角色名" min-width="150" />
          </el-table>
          <div v-else class="view-empty-tab">当前暂无关联角色</div>
        </el-tab-pane>
        <el-tab-pane label="组织机构" name="org">
          <el-table :data="viewOrgs" border size="small" max-height="350" v-if="viewOrgs.length > 0" style="width:100%">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="remark" label="机构层级" width="300" show-overflow-tooltip />
            <el-table-column prop="name" label="机构名称" width="150" />
          </el-table>
          <div v-else class="view-empty-tab">当前暂无组织机构</div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Delete, Edit, Lock, UserFilled, Share, EditPen, Folder, View } from '@element-plus/icons-vue'
import {
  listAdmin, saveAdmin, updateAdmin, delAdmin, batchDelAdmin, resetAdminPwd,
  connectRoles, connectOrgs, queryAdminRoleTree, saveAdminInfo, listAdminApps, getAdminDetail,
  queryAdminOrgnazitionTree
} from '@/api/system/admin'

const route = useRoute()

// ========== 数据 ==========
const users = ref([])
const tableTotal = ref(0)
const loading = ref(false)

// ========== 搜索 ==========
const searchForm = reactive({
  adminId: '',
  username: '',
  enableStatus: -1,
  realName: '',
  phone: '',
  email: '',
  gender: '',
  idCard: ''
})

// ========== 分页 ==========
const pagination = reactive({ page: 1, size: 15 })
const sortField = ref('createDate')
const sortOrder = ref('desc')

function handleSortChange({ prop, order }) {
  if (prop) sortField.value = prop
  sortOrder.value = order === 'ascending' ? 'asc' : 'desc'
  pagination.page = 1; fetchData()
}

// ========== 选择 ==========
const selectedIds = ref([])
function handleSelectionChange(selection) { selectedIds.value = selection.map(s => s.id) }

// ========== 初始化 ==========
async function fetchData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      sortField: sortField.value,
      sortOrder: sortOrder.value,
      adminId: searchForm.adminId || undefined,
      username: searchForm.username || undefined,
      enableStatus: searchForm.enableStatus != null ? searchForm.enableStatus : undefined,
      realName: searchForm.realName || undefined,
      phone: searchForm.phone || undefined,
      email: searchForm.email || undefined,
      gender: searchForm.gender !== '' ? searchForm.gender : undefined,
      idCard: searchForm.idCard || undefined
    }
    const res = await listAdmin(params)
    users.value = res.data || []
    tableTotal.value = res.count || 0
  } catch (e) {
    console.error('获取管理员列表失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})

function handleSearch() { pagination.page = 1; fetchData() }
function handleReset() {
  Object.assign(searchForm, {
    adminId: '', username: '', enableStatus: -1,
    realName: '', phone: '', email: '', gender: '', idCard: ''
  })
  pagination.page = 1; fetchData()
}

watch(() => pagination.page, fetchData)
watch(() => pagination.size, () => { pagination.page = 1; fetchData() })

// ========== 新增/编辑弹窗 ==========
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const editingAdminId = ref(null)
const submitLoading = ref(false)
const dialogLoading = ref(false)
const formRef = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑用户' : '新增用户')

const formData = reactive({
  username: '',
  password: '',
  confirmPwd: '',
  enableStatus: 1,
  remark: ''
})

const validateConfirmPwd = (_rule, value, callback) => {
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
  ]
}

function resetForm() {
  formData.username = ''
  formData.password = ''
  formData.confirmPwd = ''
  formData.enableStatus = 1
  formData.remark = ''
}

async function handleAdd() {
  isEdit.value = false
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true
  editingId.value = row.id
  editingAdminId.value = row.adminId
  formData.username = row.username
  formData.password = ''
  formData.confirmPwd = ''
  formData.enableStatus = row.enableStatus === 1 || row.enableStatus === '1' ? 1 : 0
  formData.remark = row.remark || ''
  dialogVisible.value = true
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
        username: formData.username,
        enableStatus: formData.enableStatus,
        remark: formData.remark
      })
      ElMessage.success('编辑成功')
    } else {
      if (formData.password !== formData.confirmPwd) {
        ElMessage.error('密码与确认密码不一致')
        submitLoading.value = false
        return
      }
      await saveAdmin({
        username: formData.username,
        password: formData.password,
        enableStatus: formData.enableStatus,
        remark: formData.remark
      })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    resetForm()
    fetchData()
  } catch {
    // 错误由拦截器统一处理
  } finally {
    submitLoading.value = false
  }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm(`确认删除账号"${row.username}"吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await delAdmin(row.id); ElMessage.success('删除成功'); fetchData() } catch { /* 拦截器处理 */ }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的账号')
    return
  }
  ElMessageBox.confirm(`确认删除选中的 ${selectedIds.value.length} 个账号吗？`, '批量删除', {
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

// ========== 角色弹窗 ==========
const roleDialogVisible = ref(false)
const roleTreeLoading = ref(false)
const roleTreeData = ref([])
const roleTreeRef = ref(null)
const roleCheckedKeys = ref([])
const currentRoleAdminId = ref(null)
const checkedRoleInfos = ref([])

// 从树节点提取角色信息（叶子节点才有 roleId）
function extractRoleInfo(node) {
  const isLeaf = !node.children || node.children.length === 0
  if (isLeaf && node.roleId) {
    return {
      id: node.id,
      roleId: node.roleId,
      appCode: node.appCode,
      title: node.title
    }
  }
  return null
}

// 递归收集叶子节点信息
function collectLeafRoleInfos(nodes, keySet, result) {
  for (const node of nodes) {
    if (keySet.has(node.id)) {
      const info = extractRoleInfo(node)
      if (info) result.push(info)
    }
    if (node.children && node.children.length > 0) {
      collectLeafRoleInfos(node.children, keySet, result)
    }
  }
}

// 树选中状态变化时更新已选角色列表
function handleRoleTreeCheck(_node, treeState) {
  const idSet = new Set(treeState.checkedKeys)
  const infos = []
  collectLeafRoleInfos(roleTreeData.value, idSet, infos)
  checkedRoleInfos.value = infos
}

// 点击标签关闭按钮取消勾选
async function handleUncheckRole(role) {
  const tree = roleTreeRef.value
  if (!tree) return
  tree.setChecked(role.id, false, false)
  // setChecked 是程序化调用，需手动同步已选角色列表
  await nextTick()
  syncCheckedRoleInfos()
}

// 同步已选角色（初始化后调用）
function syncCheckedRoleInfos() {
  const tree = roleTreeRef.value
  if (!tree) return
  const checkedKeys = tree.getCheckedKeys()
  const idSet = new Set(checkedKeys)
  const infos = []
  collectLeafRoleInfos(roleTreeData.value, idSet, infos)
  checkedRoleInfos.value = infos
}

// 递归收集已选中节点ID（只收集叶子节点，避免父节点因联动导致全选）
function collectCheckedKeys(nodes, ids) {
  for (const node of nodes) {
    const isLeaf = !node.children || node.children.length === 0
    const isChecked = node.checked === true || node.checked === 'true'
      || (node.state && node.state.checked)
    // 只收集叶子节点的选中状态，父节点的 checked 可能是因部分子节点选中导致的
    if (isChecked && isLeaf) {
      ids.push(node.id)
    }
    if (node.children && node.children.length > 0) {
      collectCheckedKeys(node.children, ids)
    }
  }
}

// 递归清除节点上的 checked 属性，避免 el-tree 将其作为初始选中状态（统一由 default-checked-keys 控制）
function stripChecked(nodes) {
  for (const node of nodes) {
    delete node.checked
    if (node.children && node.children.length > 0) {
      stripChecked(node.children)
    }
  }
}

// 递归收集节点信息（角色ID和应用编码），包含父节点
function collectNodesFromTree(nodes, keySet, result) {
  for (const node of nodes) {
    if (keySet.has(node.id)) {
      result.push({ roleId: node.roleId, appCode: node.appCode })
    }
    if (node.children && node.children.length > 0) {
      collectNodesFromTree(node.children, keySet, result)
    }
  }
}

function handleRole(row) {
  currentRoleAdminId.value = row.adminId
  // 先清空旧数据再打开对话框，防止树组件用旧数据渲染
  roleTreeData.value = []
  roleCheckedKeys.value = []
  roleDialogVisible.value = true
  roleTreeLoading.value = true
  queryAdminRoleTree(row.adminId).then(async res => {
    if (res.code === 1 || res.data) {
      roleTreeData.value = res.data || []
      roleCheckedKeys.value = []
      collectCheckedKeys(roleTreeData.value, roleCheckedKeys.value)
      stripChecked(roleTreeData.value)
      // 数据加载后通过 setCheckedKeys 精确控制选中状态
      await nextTick()
      roleTreeRef.value?.setCheckedKeys(roleCheckedKeys.value)
      syncCheckedRoleInfos()
    }
  }).catch(() => {
    roleTreeData.value = []
  }).finally(() => {
    roleTreeLoading.value = false
  })
}

async function handleSubmitRole() {
  const tree = roleTreeRef.value
  if (!tree) return
  const checkedNodes = tree.getCheckedNodes(false, false)
  const idSet = new Set(checkedNodes.map(n => n.id))
  const rolesArray = []
  collectNodesFromTree(roleTreeData.value, idSet, rolesArray)
  roleTreeLoading.value = true
  try {
    await connectRoles({ roles: rolesArray, adminId: currentRoleAdminId.value })
    ElMessage.success(rolesArray.length === 0 ? '已清空角色' : '角色关联成功')
    roleDialogVisible.value = false
    fetchData()
  } catch {
    // 拦截器处理
  } finally {
    roleTreeLoading.value = false
  }
}

// ========== 组织机构弹窗 ==========
const orgDialogVisible = ref(false)
const orgTreeLoading = ref(false)
const orgTreeData = ref([])
const orgTreeRef = ref(null)
const orgCheckedKeys = ref([])
const currentOrgAdminId = ref(null)
const orgFilterText = ref('')

function filterOrgNode(value, data) {
  if (!value) return true
  return data.title.toLowerCase().includes(value.toLowerCase())
}

watch(orgFilterText, (val) => {
  orgTreeRef.value?.filter(val)
})
const orgForm = reactive({ appCode: '' })

function collectCheckedKeysFromTree(nodes, ids) {
  for (const node of nodes) {
    if (node.state && node.state.checked) {
      ids.push(node.id)
    }
    if (node.children && node.children.length > 0) {
      collectCheckedKeysFromTree(node.children, ids)
    }
  }
}

function collectOrgNodesFromTree(nodes, keySet, result, appCode) {
  for (const node of nodes) {
    if (keySet.has(node.id)) {
      result.push({ orgnazitionId: node.orgnazitionId, appCode: node.appCode || appCode })
    }
    if (node.children && node.children.length > 0) {
      collectOrgNodesFromTree(node.children, keySet, result, appCode)
    }
  }
}

async function handleOrgnazition(row) {
  currentOrgAdminId.value = row.adminId
  orgForm.appCode = ''
  orgTreeData.value = []
  orgCheckedKeys.value = []
  orgDialogVisible.value = true
  orgTreeLoading.value = true
  try {
    const res = await listAdminApps(row.adminId)
    const apps = (res.data || []).map(a => ({ code: a.appCode || a.code, name: a.appName || a.name }))
    if (apps.length > 0) {
      orgForm.appCode = apps[0].code
      await loadOrgTree(apps[0].code)
    }
  } catch { /* ignore */ }
  finally { orgTreeLoading.value = false }
}

async function loadOrgTree(appCode) {
  if (!appCode) {
    orgTreeData.value = []
    orgCheckedKeys.value = []
    return
  }
  orgTreeLoading.value = true
  try {
    const res = await queryAdminOrgnazitionTree({ adminId: currentOrgAdminId.value, appCode })
    if (res.code === 1 || res.data) {
      orgTreeData.value = res.data || []
      orgCheckedKeys.value = []
      collectCheckedKeysFromTree(orgTreeData.value, orgCheckedKeys.value)
    }
  } catch {
    orgTreeData.value = []
  } finally {
    orgTreeLoading.value = false
  }
}

async function handleSubmitOrg() {
  const tree = orgTreeRef.value
  if (!tree) return
  const checkedNodes = tree.getCheckedNodes(false, false)
  const idSet = new Set(checkedNodes.map(n => n.id))
  const orgArray = []
  collectOrgNodesFromTree(orgTreeData.value, idSet, orgArray, orgForm.appCode)
  orgTreeLoading.value = true
  try {
    await connectOrgs({
      adminOrgnazitions: orgArray,
      adminId: currentOrgAdminId.value,
      selectAppCode: orgForm.appCode
    })
    ElMessage.success('组织机构关联成功')
    orgDialogVisible.value = false
    fetchData()
  } catch {
    // 拦截器处理
  } finally {
    orgTreeLoading.value = false
  }
}

// ========== 修改密码弹窗 ==========
const pwdDialogVisible = ref(false)
const pwdTargetUser = ref(null)
const pwdLoading = ref(false)
const pwdFormRef = ref(null)
const pwdForm = reactive({ password: '', confirmPwd: '' })

const validatePwdConfirm = (_rule, value, callback) => {
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

function handlePassword(row) {
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
      id: pwdTargetUser.value.id,
      adminId: pwdTargetUser.value.adminId,
      password: pwdForm.password
    })
    ElMessage.success(`「${pwdTargetUser.value.username}」密码已修改`)
    pwdDialogVisible.value = false
  } catch {
    // 错误由拦截器统一处理
  } finally {
    pwdLoading.value = false
  }
}

// ========== 完善信息弹窗 ==========
const viewVisible = ref(false)
const viewLoading = ref(false)
const viewActiveTab = ref('info')
const viewData = ref({})
const viewRoles = ref([])
const viewOrgs = ref([])
const infoDialogVisible = ref(false)
const infoLoading = ref(false)
const infoSubmitLoading = ref(false)
const infoFormRef = ref(null)
const currentInfoAdminId = ref(null)
const infoForm = reactive({
  nickName: '',
  realName: '',
  phone: '',
  email: '',
  gender: '',
  idCard: '',
  birthday: '',
  address: ''
})

const infoRules = {
  realName: [{ required: true, message: '真实姓名不能为空', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  idCard: [{ pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '身份证号格式不正确', trigger: 'blur' }]
}

async function handleViewAdmin(row) {
  viewData.value = {}
  viewRoles.value = []
  viewOrgs.value = []
  viewVisible.value = true
  viewActiveTab.value = 'info'
  viewLoading.value = true
  try {
    const res = await getAdminDetail(row.id)
    if (res.data) {
      viewData.value = res.data.basicInfo || row
      viewRoles.value = res.data.roles || []
      viewOrgs.value = res.data.orgs || []
    } else {
      viewData.value = row
    }
  } catch { viewData.value = row }
  finally { viewLoading.value = false }
}

function handleInfo(row) {
  currentInfoAdminId.value = row.adminId
  infoForm.nickName = row.nickName || ''
  infoForm.realName = row.realName || ''
  infoForm.phone = row.phone || ''
  infoForm.email = row.email || ''
  infoForm.gender = row.gender !== '' && row.gender != null ? (row.gender === '0' ? 0 : 1) : ''
  infoForm.idCard = row.idCard || ''
  infoForm.birthday = row.birthday || ''
  infoForm.address = row.address || ''
  infoDialogVisible.value = true
}

async function handleSubmitInfo() {
  const valid = await infoFormRef.value.validate().catch(() => false)
  if (!valid) return
  infoSubmitLoading.value = true
  try {
    await saveAdminInfo({
      adminId: currentInfoAdminId.value,
      nickName: infoForm.nickName,
      realName: infoForm.realName,
      phone: infoForm.phone,
      email: infoForm.email,
      gender: infoForm.gender,
      idCard: infoForm.idCard,
      birthday: infoForm.birthday,
      address: infoForm.address
    })
    ElMessage.success('信息保存成功')
    infoDialogVisible.value = false
    fetchData()
  } catch {
    // 拦截器处理
  } finally {
    infoSubmitLoading.value = false
  }
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
.org-tree-toolbar {
  display: flex; gap: 12px; align-items: center; margin-bottom: 16px;
}

.org-tree-body {
  border: 1px solid $border-light; border-radius: 8px; padding: 12px;
  max-height: 380px; overflow-y: auto; background: #fafbfc;
}

.text-muted { color: $text-placeholder; }

.org-tree-node {
  display: flex; align-items: center; gap: 6px; font-size: 14px;
  .el-icon { color: $primary; }
}

.checked-roles-bar {
  padding: 8px 12px;
  margin-bottom: 12px;
  background: #f0f9eb;
  border: 1px solid #e1f3d8;
  border-radius: 6px;
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 6px;
  max-height: 120px;
  overflow-y: auto;
  .checked-roles-label {
    font-size: 13px;
    color: #67c23a;
    font-weight: 600;
    line-height: 22px;
    flex-shrink: 0;
  }
  &--empty {
    background: #fafafa;
    border-color: #ebeef5;
    .text-muted { font-size: 13px; }
  }
}
.view-empty-tab {
  padding: 40px 0;
  text-align: center;
  color: $text-placeholder;
  font-size: 14px;
}
.view-tabs {
  :deep(.el-tabs__content) { padding: 8px 4px; }
}
.view-descriptions {
  :deep(.el-descriptions__body) { padding: 8px; }
}
</style>
