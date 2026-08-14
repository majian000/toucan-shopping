<template>
  <div class="user-management">
    <h2 class="page-title">用户列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="关键字">
          <el-input v-model="searchForm.keyword" placeholder="手机号/昵称/用户名/用户ID/邮箱/身份证" clearable style="width:260px" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.mobilePhone" placeholder="请输入手机号" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="searchForm.nickName" placeholder="请输入昵称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="searchForm.email" placeholder="请输入邮箱" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select v-model="searchForm.enableStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'toucan:user:regist'" @click="handleRegist">注册</el-button>
        <el-button type="danger" :icon="Delete" v-permission="'toucan:user:disabled:ids'" :disabled="selectedRows.length === 0" @click="handleBatchDisable">批量禁用</el-button>
      </div>
      <el-table ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="userMainId" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="userMainId" label="用户ID" width="180" show-overflow-tooltip />
        <el-table-column label="头像" width="70" align="center">
          <template #default="{ row }">
            <el-image v-if="row.httpHeadSculpture" :src="row.httpHeadSculpture" :preview-src-list="[row.httpHeadSculpture]" preview-teleported fit="cover" style="width:30px;height:30px;border-radius:50%;cursor:zoom-in" />
          </template>
        </el-table-column>
        <el-table-column prop="mobilePhone" label="手机号" width="130" />
        <el-table-column prop="username" label="用户名" width="140" show-overflow-tooltip />
        <el-table-column prop="nickName" label="昵称" width="140" show-overflow-tooltip />
        <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip />
        <el-table-column prop="enableStatus" label="启用状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 || row.enableStatus === '1' ? 'success' : 'danger'" size="small">
              {{ row.enableStatus === 1 || row.enableStatus === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="trueNameStatus" label="实名状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.trueNameStatus === 1 || row.trueNameStatus === '1' ? 'success' : 'info'" size="small">
              {{ row.trueNameStatus === 1 || row.trueNameStatus === '1' ? '已实名' : '未实名' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="380" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:user:update:detail'" @click="handleEdit(row)">修改资料</el-button>
            <el-button type="warning" link size="small" :icon="Key" v-permission="'toucan:user:reset:password'" @click="handleResetPassword(row)">重置密码</el-button>
            <el-button v-if="row.enableStatus === 1 || row.enableStatus === '1'" type="danger" link size="small" :icon="CircleClose" v-permission="'toucan:user:disabled:enabled'" @click="handleToggleEnable(row)">停用</el-button>
            <el-button v-else type="success" link size="small" :icon="CircleCheck" v-permission="'toucan:user:disabled:enabled'" @click="handleToggleEnable(row)">启用</el-button>
            <el-button type="primary" link size="small" :icon="Refresh" v-permission="'toucan:user:flushCache'" @click="handleFlushCache(row)">刷新缓存</el-button>
            <el-dropdown trigger="click" @command="(cmd) => handleMoreCommand(cmd, row)">
              <el-button type="primary" link size="small" :icon="MoreFilled">更多</el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="mobilePhone">手机号</el-dropdown-item>
                  <el-dropdown-item command="email">邮箱</el-dropdown-item>
                  <el-dropdown-item command="username">用户名</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSearch" @current-change="handleSearch"
        />
      </div>
    </el-card>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="viewVisible" title="查看用户" width="760px" :close-on-click-modal="false" destroy-on-close>
      <el-descriptions v-if="viewData" :column="2" border label-width="110px">
        <el-descriptions-item label="用户ID">{{ viewData.userMainId }}</el-descriptions-item>
        <el-descriptions-item label="启用状态">
          <el-tag :type="viewData.enableStatus === 1 || viewData.enableStatus === '1' ? 'success' : 'danger'" size="small">
            {{ viewData.enableStatus === 1 || viewData.enableStatus === '1' ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="手机号">{{ viewData.mobilePhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ viewData.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ viewData.nickName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ viewData.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ viewData.trueName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ viewData.idCard || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实名状态">
          <el-tag :type="viewData.trueNameStatus === 1 || viewData.trueNameStatus === '1' ? 'success' : 'info'" size="small">
            {{ viewData.trueNameStatus === 1 || viewData.trueNameStatus === '1' ? '已实名' : '未实名' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="性别">{{ sexText(viewData.sex) }}</el-descriptions-item>
        <el-descriptions-item label="个性签名" :span="2">{{ viewData.personalizedSignature || '-' }}</el-descriptions-item>
        <el-descriptions-item label="头像" :span="2">
          <el-image v-if="viewData.httpHeadSculpture" :src="viewData.httpHeadSculpture" :preview-src-list="[viewData.httpHeadSculpture]" preview-teleported fit="cover" style="width:80px;height:80px;border-radius:50%" />
        </el-descriptions-item>
        <el-descriptions-item label="证件照正面" :span="2">
          <el-image v-if="viewData.httpIdcardImg1" :src="viewData.httpIdcardImg1" :preview-src-list="[viewData.httpIdcardImg1]" preview-teleported fit="cover" style="width:140px;height:90px" />
        </el-descriptions-item>
        <el-descriptions-item label="证件照背面" :span="2">
          <el-image v-if="viewData.httpIdcardImg2" :src="viewData.httpIdcardImg2" :preview-src-list="[viewData.httpIdcardImg2]" preview-teleported fit="cover" style="width:140px;height:90px" />
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="删除状态">{{ viewData.deleteStatus === 1 || viewData.deleteStatus === '1' ? '已删除' : '正常' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 注册弹窗 -->
    <el-dialog v-model="registVisible" title="注册用户" width="520px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="registFormRef" :model="registForm" :rules="registRules" label-width="100px">
        <el-form-item label="手机号" prop="mobilePhone">
          <el-input v-model="registForm.mobilePhone" placeholder="请输入注册手机号" maxlength="11" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registForm.username" placeholder="选填" maxlength="30" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registForm.email" placeholder="选填" maxlength="50" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registForm.confirmPassword" type="password" show-password placeholder="请再次输入密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="registVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRegistSubmit" :loading="registLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改资料弹窗 -->
    <el-dialog v-model="editVisible" title="修改资料" width="680px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="editFormRef" :model="editForm" label-width="110px">
        <el-form-item label="用户ID">
          <el-input :model-value="editForm.userMainId" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickName" placeholder="请输入昵称" maxlength="30" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="editForm.trueName" placeholder="请输入真实姓名" maxlength="30" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="editForm.idCard" placeholder="请输入身份证号" maxlength="20" />
        </el-form-item>
        <el-form-item label="证件类型">
          <el-select v-model="editForm.idcardType" style="width:100%">
            <el-option label="身份证" :value="1" />
            <el-option label="护照" :value="2" />
            <el-option label="海外" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="editForm.sex">
            <el-radio :value="0">女</el-radio>
            <el-radio :value="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="个性签名">
          <el-input v-model="editForm.personalizedSignature" type="textarea" :rows="2" maxlength="200" placeholder="请输入个性签名" />
        </el-form-item>
        <el-form-item label="头像">
          <el-upload :auto-upload="false" :show-file-list="false" :on-change="onHeadSculptureChange" :before-upload="beforeUpload" accept="image/*">
            <el-button type="primary" :icon="Upload">上传头像</el-button>
          </el-upload>
          <el-image v-if="editForm.httpHeadSculpture" :src="editForm.httpHeadSculpture" :preview-src-list="[editForm.httpHeadSculpture]" preview-teleported fit="cover" style="width:60px;height:60px;border-radius:50%;margin-top:8px" />
        </el-form-item>
        <el-form-item label="证件照正面">
          <el-upload :auto-upload="false" :show-file-list="false" :on-change="onIdcardImg1Change" :before-upload="beforeUpload" accept="image/*">
            <el-button type="primary" :icon="Upload">上传正面</el-button>
          </el-upload>
          <el-image v-if="editForm.httpIdcardImg1" :src="editForm.httpIdcardImg1" :preview-src-list="[editForm.httpIdcardImg1]" preview-teleported fit="cover" style="width:120px;height:80px;margin-top:8px" />
        </el-form-item>
        <el-form-item label="证件照背面">
          <el-upload :auto-upload="false" :show-file-list="false" :on-change="onIdcardImg2Change" :before-upload="beforeUpload" accept="image/*">
            <el-button type="primary" :icon="Upload">上传背面</el-button>
          </el-upload>
          <el-image v-if="editForm.httpIdcardImg2" :src="editForm.httpIdcardImg2" :preview-src-list="[editForm.httpIdcardImg2]" preview-teleported fit="cover" style="width:120px;height:80px;margin-top:8px" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit" :loading="editLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 重置密码弹窗 -->
    <el-dialog v-model="resetVisible" title="重置密码" width="460px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="resetFormRef" :model="resetForm" :rules="resetRules" label-width="100px">
        <el-form-item label="用户ID">
          <el-input :model-value="resetForm.userMainId" disabled />
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input v-model="resetForm.password" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetVisible = false">取消</el-button>
        <el-button type="primary" @click="handleResetSubmit" :loading="resetLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 手机号/邮箱/用户名 子表弹窗 -->
    <el-dialog v-model="subDialog.visible" :title="subDialog.title" width="620px" :close-on-click-modal="false" destroy-on-close>
      <div class="sub-toolbar">
        <el-input v-model="subDialog.connectValue" :placeholder="subDialog.placeholder" clearable style="width:240px" />
        <el-button type="primary" :icon="Plus" v-permission="['toucan:user:mobile:phone:connectMobilePhone', 'toucan:user:email:email', 'toucan:user:username:username']" @click="handleConnect">关联</el-button>
      </div>
      <el-table :data="subDialog.list" border stripe v-loading="subDialog.loading">
        <el-table-column prop="id" label="ID" width="200" show-overflow-tooltip />
        <el-table-column :prop="subDialog.valueProp" :label="subDialog.valueLabel" min-width="160" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="deleteStatus" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.deleteStatus === 1 || row.deleteStatus === '1' ? 'danger' : 'success'" size="small">
              {{ row.deleteStatus === 1 || row.deleteStatus === '1' ? '停用' : '启用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button v-if="row.deleteStatus === 1 || row.deleteStatus === '1'" type="success" link size="small" v-permission="['toucan:user:mobile:phone:list:disabled:enabled', 'toucan:user:email:list:disabled:enabled', 'toucan:user:username:list:disabled:enabled']" @click="handleToggleSub(row)">启用</el-button>
            <el-button v-else type="danger" link size="small" v-permission="['toucan:user:mobile:phone:list:disabled:enabled', 'toucan:user:email:list:disabled:enabled', 'toucan:user:username:list:disabled:enabled']" @click="handleToggleSub(row)">停用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="subDialog.visible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Search, RefreshRight, View, Key, Refresh, Upload, CircleCheck, CircleClose, MoreFilled } from '@element-plus/icons-vue'
import {
  listUser, regist, updateDetail, resetPassword, connectMobilePhone, connectEmail, connectUsername,
  disabledEnabled, disabledByIds, flushCache, listMobilePhone, listEmail, listUsername,
  disabledEnabledMobilePhone, disabledEnabledEmail, disabledEnabledUsername
} from '@/api/user/user'

// ========== 搜索 ==========
const searchForm = reactive({ keyword: '', mobilePhone: '', username: '', nickName: '', email: '', enableStatus: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.keyword = ''; searchForm.mobilePhone = ''; searchForm.username = ''; searchForm.nickName = ''; searchForm.email = ''; searchForm.enableStatus = '' }

// ========== 表格 ==========
const tableRef = ref(null)
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listUser(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function sexText(sex) {
  if (sex === 1 || sex === '1') return '男'
  if (sex === 0 || sex === '0') return '女'
  return '-'
}

// ========== 查看 ==========
const viewVisible = ref(false)
const viewData = ref(null)
function handleView(row) { viewData.value = row; viewVisible.value = true }

// ========== 注册 ==========
const registVisible = ref(false)
const registLoading = ref(false)
const registFormRef = ref(null)
const registForm = reactive({ mobilePhone: '', username: '', email: '', password: '', confirmPassword: '' })
const registRules = {
  mobilePhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式错误', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请再次输入密码', trigger: 'blur' }]
}

function handleRegist() {
  registForm.mobilePhone = ''; registForm.username = ''; registForm.email = ''
  registForm.password = ''; registForm.confirmPassword = ''
  registVisible.value = true
}

async function handleRegistSubmit() {
  const valid = await registFormRef.value.validate().catch(() => false)
  if (!valid) return
  if (registForm.password !== registForm.confirmPassword) { ElMessage.warning('密码与确认密码不一致'); return }
  registLoading.value = true
  try {
    await regist({ ...registForm })
    ElMessage.success('注册成功')
    registVisible.value = false
    loadTableData()
  } catch { } finally { registLoading.value = false }
}

// ========== 修改资料 ==========
const editVisible = ref(false)
const editLoading = ref(false)
const editFormRef = ref(null)
const editForm = reactive({ userMainId: null, nickName: '', trueName: '', idCard: '', idcardType: 1, sex: 1, personalizedSignature: '', headSculpture: '', idcardImg1: '', idcardImg2: '', httpHeadSculpture: '', httpIdcardImg1: '', httpIdcardImg2: '', headSculptureBase64: '', idcardImg1Base64: '', idcardImg2Base64: '' })

function handleEdit(row) {
  editForm.userMainId = row.userMainId
  editForm.nickName = row.nickName || ''
  editForm.trueName = row.trueName || ''
  editForm.idCard = row.idCard || ''
  editForm.idcardType = row.idcardType != null ? row.idcardType : 1
  editForm.sex = row.sex != null ? row.sex : 1
  editForm.personalizedSignature = row.personalizedSignature || ''
  editForm.headSculpture = row.headSculpture || ''
  editForm.idcardImg1 = row.idcardImg1 || ''
  editForm.idcardImg2 = row.idcardImg2 || ''
  editForm.httpHeadSculpture = row.httpHeadSculpture || ''
  editForm.httpIdcardImg1 = row.httpIdcardImg1 || ''
  editForm.httpIdcardImg2 = row.httpIdcardImg2 || ''
  editForm.headSculptureBase64 = ''
  editForm.idcardImg1Base64 = ''
  editForm.idcardImg2Base64 = ''
  editVisible.value = true
}

async function handleEditSubmit() {
  editLoading.value = true
  try {
    await updateDetail({ ...editForm })
    ElMessage.success('修改成功')
    editVisible.value = false
    loadTableData()
  } catch { } finally { editLoading.value = false }
}

function beforeUpload(file) {
  if (!file.type.startsWith('image/')) { ElMessage.error('只能上传图片文件'); return false }
  return true
}
function onHeadSculptureChange(file) {
  const reader = new FileReader()
  reader.onload = (e) => {
    editForm.headSculptureBase64 = e.target.result
    editForm.httpHeadSculpture = e.target.result
  }
  reader.readAsDataURL(file.raw)
}
function onIdcardImg1Change(file) {
  const reader = new FileReader()
  reader.onload = (e) => {
    editForm.idcardImg1Base64 = e.target.result
    editForm.httpIdcardImg1 = e.target.result
  }
  reader.readAsDataURL(file.raw)
}
function onIdcardImg2Change(file) {
  const reader = new FileReader()
  reader.onload = (e) => {
    editForm.idcardImg2Base64 = e.target.result
    editForm.httpIdcardImg2 = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

// ========== 重置密码 ==========
const resetVisible = ref(false)
const resetLoading = ref(false)
const resetFormRef = ref(null)
const resetForm = reactive({ userMainId: null, password: '', confirmPassword: '' })
const resetRules = {
  password: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请再次输入新密码', trigger: 'blur' }]
}

function handleResetPassword(row) {
  resetForm.userMainId = row.userMainId
  resetForm.password = ''; resetForm.confirmPassword = ''
  resetVisible.value = true
}

async function handleResetSubmit() {
  const valid = await resetFormRef.value.validate().catch(() => false)
  if (!valid) return
  if (resetForm.password !== resetForm.confirmPassword) { ElMessage.warning('密码与确认密码不一致'); return }
  resetLoading.value = true
  try {
    await resetPassword({ ...resetForm })
    ElMessage.success('重置成功')
    resetVisible.value = false
  } catch { } finally { resetLoading.value = false }
}

// ========== 启用/停用 ==========
function handleToggleEnable(row) {
  const action = (row.enableStatus === 1 || row.enableStatus === '1') ? '停用' : '启用'
  ElMessageBox.confirm(`确定${action}该用户吗？`, '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try { await disabledEnabled(row.userMainId); ElMessage.success(`${action}成功`); loadTableData() } catch { }
    }).catch(() => {})
}

function handleFlushCache(row) {
  ElMessageBox.confirm('确定刷新该用户缓存吗？', '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try { const res = await flushCache(row.userMainId); ElMessage.success(res.msg || '刷新成功') } catch { }
    }).catch(() => {})
}

function handleBatchDisable() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要禁用的记录'); return }
  ElMessageBox.confirm(`确定禁用选中的 ${selectedRows.value.length} 个用户吗？`, '批量禁用确认', { confirmButtonText: '确定禁用', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      const list = selectedRows.value.map(r => ({ userMainId: r.userMainId }))
      try { await disabledByIds(list); ElMessage.success('批量禁用成功'); loadTableData() } catch { }
    }).catch(() => {})
}

// ========== 手机号/邮箱/用户名 子表 ==========
const subDialog = reactive({
  visible: false, loading: false, type: '', title: '', valueProp: '', valueLabel: '', placeholder: '',
  userMainId: null, list: [], connectValue: ''
})

function handleMoreCommand(cmd, row) {
  if (cmd === 'mobilePhone') openSubDialog('mobilePhone', row)
  else if (cmd === 'email') openSubDialog('email', row)
  else if (cmd === 'username') openSubDialog('username', row)
}

const subConfig = {
  mobilePhone: { title: '手机号列表', valueProp: 'mobilePhone', valueLabel: '手机号', placeholder: '请输入要关联的手机号' },
  email: { title: '邮箱列表', valueProp: 'email', valueLabel: '邮箱', placeholder: '请输入要关联的邮箱' },
  username: { title: '用户名列表', valueProp: 'username', valueLabel: '用户名', placeholder: '请输入要关联的用户名' }
}

function openSubDialog(type, row) {
  const cfg = subConfig[type]
  subDialog.type = type
  subDialog.title = cfg.title
  subDialog.valueProp = cfg.valueProp
  subDialog.valueLabel = cfg.valueLabel
  subDialog.placeholder = cfg.placeholder
  subDialog.userMainId = row.userMainId
  subDialog.list = []
  subDialog.connectValue = ''
  subDialog.visible = true
  loadSubList()
}

async function loadSubList() {
  subDialog.loading = true
  try {
    const params = { userMainId: subDialog.userMainId, page: 1, limit: 1000 }
    let res
    if (subDialog.type === 'mobilePhone') res = await listMobilePhone(params)
    else if (subDialog.type === 'email') res = await listEmail(params)
    else res = await listUsername(params)
    subDialog.list = res.data || []
  } catch { } finally { subDialog.loading = false }
}

async function handleConnect() {
  if (!subDialog.connectValue) { ElMessage.warning('请输入要关联的内容'); return }
  const data = { userMainId: subDialog.userMainId }
  if (subDialog.type === 'mobilePhone') {
    data.mobilePhone = subDialog.connectValue
    try { await connectMobilePhone(data); ElMessage.success('关联成功'); subDialog.connectValue = ''; loadSubList() } catch { }
  } else if (subDialog.type === 'email') {
    data.email = subDialog.connectValue
    try { await connectEmail(data); ElMessage.success('关联成功'); subDialog.connectValue = ''; loadSubList() } catch { }
  } else {
    data.username = subDialog.connectValue
    try { await connectUsername(data); ElMessage.success('关联成功'); subDialog.connectValue = ''; loadSubList() } catch { }
  }
}

function handleToggleSub(row) {
  const data = { id: row.id }
  const fn = subDialog.type === 'mobilePhone' ? disabledEnabledMobilePhone : subDialog.type === 'email' ? disabledEnabledEmail : disabledEnabledUsername
  fn(data).then(() => { ElMessage.success('操作成功'); loadSubList() }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.user-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  .sub-toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
  :deep(.el-descriptions__table) { width: 100%; }
  :deep(.el-descriptions__label) { word-break: keep-all; }
  :deep(.el-descriptions__content) { word-break: break-all; overflow-wrap: anywhere; min-width: 0; }
}
</style>
