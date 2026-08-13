<template>
  <div class="category-management">
    <h2 class="page-title">类别列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">添加类别</el-button>
        <el-button type="danger" :icon="Delete" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="warning" :icon="Refresh" @click="handleFlushCache">刷新缓存</el-button>
        <el-button :icon="Delete" @click="handleClearCache">清空首页缓存</el-button>
      </div>
      <el-table
        ref="tableRef" :key="tableKey" :data="tableData" border stripe v-loading="loading" row-key="id"
        lazy :load="loadChildren" :tree-props="{ children: 'children', hasChildren: 'haveChild' }"
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="name" label="名称" width="260" />
        <el-table-column prop="icon" label="图标" width="270" >
          <template #default="{ row }"><img v-if="row.httpIconPath" :src="row.httpIconPath" style="width:24px;height:24px" /></template>
        </el-table-column>
        <el-table-column prop="categorySort" label="排序" width="80" align="center" />
        <el-table-column prop="typeNames" label="类型" width="150" />
        <el-table-column prop="showStatus" label="显示状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.showStatus === 1 ? 'success' : 'info'" size="small">
              {{ row.showStatus === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="href" label="跳转路径" width="180" show-overflow-tooltip />
        <el-table-column prop="noticeTips" label="注意事项" width="200" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" width="200" show-overflow-tooltip />
        <el-table-column prop="createAdminUsername" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminUsername" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="210" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" v-permission="'toucan:category:detail'" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px" v-loading="dialogLoading">
        <el-form-item label="上级类别">
          <el-tree-select
            v-model="formData.parentId"
            :props="{ children: 'children', label: 'name', value: 'id', isLeaf: 'leaf' }"
            check-strictly clearable placeholder="请选择上级类别（不选则为根节点）" style="width:100%"
            :load="loadParentCategoryTree" lazy node-key="id"
          />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" maxlength="100" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="formData.icon" placeholder="请输入图标CSS类名或URL" maxlength="100" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-checkbox-group v-model="formData.type">
            <el-checkbox v-for="t in categoryTypeList" :key="t.code" :value="t.code">{{ t.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.categorySort" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-select v-model="formData.showStatus" style="width:100%">
            <el-option label="显示" :value="1" />
            <el-option label="隐藏" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="跳转路径">
          <el-input v-model="formData.href" placeholder="请输入跳转路径" maxlength="255" />
        </el-form-item>
        <el-form-item label="注意事项">
          <el-input v-model="formData.noticeTips" type="textarea" :rows="2" maxlength="255" placeholder="请输入注意事项" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="2" maxlength="255" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="查看类别" width="650px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detailInfo" :column="2" border label-width="120px">
          <el-descriptions-item label="名称" :span="2">{{ detailInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="类型">{{ detailInfo.typeNames }}</el-descriptions-item>
          <el-descriptions-item label="排序">{{ detailInfo.categorySort }}</el-descriptions-item>
          <el-descriptions-item label="显示状态">
            <el-tag :type="detailInfo.showStatus === 1 || detailInfo.showStatus === '1' ? 'success' : 'info'" size="small">
              {{ detailInfo.showStatus === 1 || detailInfo.showStatus === '1' ? '显示' : '隐藏' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="图标">{{ detailInfo.icon }}</el-descriptions-item>
          <el-descriptions-item label="跳转路径" :span="2">{{ detailInfo.href }}</el-descriptions-item>
          <el-descriptions-item label="注意事项" :span="2">{{ detailInfo.noticeTips }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detailInfo.remark }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ detailInfo.createAdminUsername }}</el-descriptions-item>
          <el-descriptions-item label="修改人">{{ detailInfo.updateAdminUsername }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detailInfo.createDate }}</el-descriptions-item>
          <el-descriptions-item label="修改时间">{{ detailInfo.updateDate }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Search, RefreshRight, Refresh, View } from '@element-plus/icons-vue'
import { queryCategoryTreeTable, queryCategoryTreeByPid, queryCategoryTypeList, saveCategory, updateCategory, detailCategory, deleteCategory, deleteCategories, flushAllCategoryCache, clearCategoryIndexCache } from '@/api/content/category'

const searchForm = reactive({ name: '' })
function handleSearch() { loadRootData() }
function handleReset() { searchForm.name = '' }

const tableData = ref([]); const loading = ref(false); const selectedRows = ref([])
const tableRef = ref(null)
const tableKey = ref(0)
function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, parentId: -1 }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadRootData() {
  loading.value = true
  try {
    const res = await queryCategoryTreeTable(buildSearchParams())
    tableData.value = res.data || []
    // 重建表格，清空懒加载缓存和展开状态，避免删除/修改后仍显示旧数据
    tableKey.value++
  } catch { } finally { loading.value = false }
}

async function loadChildren(row, treeNode, resolve) {
  try { const params = { ...searchForm, parentId: row.id }; const res = await queryCategoryTreeTable(params); resolve(res.data || []) } catch { resolve([]) }
}

const dialogVisible = ref(false); const dialogLoading = ref(false); const isEdit = ref(false)
const submitLoading = ref(false); const formRef = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑类别' : '添加类别')
const detailVisible = ref(false); const detailInfo = ref(null); const detailLoading = ref(false)

const formData = reactive({ id: null, parentId: null, name: '', icon: '', type: [], categorySort: 0, showStatus: 1, href: '', noticeTips: '', remark: '' })
const formRules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  type: [{ required: true, type: 'array', message: '请选择类型', trigger: 'change' }]
}

const categoryTypeList = ref([])
async function loadCategoryTypeList() {
  try {
    const res = await queryCategoryTypeList()
    if (res.code === 1 && res.data) categoryTypeList.value = res.data || []
  } catch { }
}

function resetForm() {
  formData.id = null; formData.parentId = null; formData.name = ''; formData.icon = ''
  formData.type = []; formData.categorySort = 0; formData.showStatus = 1; formData.href = ''; formData.noticeTips = ''; formData.remark = ''
}

async function loadParentCategoryTree(node, resolve) {
  try { const res = await queryCategoryTreeByPid(node?.data?.id != null ? node.data.id : -1); resolve((res.data || []).map(item => ({ ...item, leaf: false }))) } catch { resolve([]) }
}

async function openDialog(row) {
  dialogVisible.value = true
  dialogLoading.value = true
  if (row) {
    isEdit.value = true
    formData.id = row.id; formData.parentId = row.parentId != null && Number(row.parentId) !== -1 ? row.parentId : null; formData.name = row.name || ''
    formData.icon = row.icon || ''; formData.categorySort = row.categorySort || 0
    formData.showStatus = row.showStatus != null ? row.showStatus : 1; formData.href = row.href || ''
    formData.noticeTips = row.noticeTips || ''; formData.remark = row.remark || ''
    formData.type = row.type ? String(row.type).split(',').filter(Boolean) : []
  } else {
    isEdit.value = false
    resetForm()
  }
  await loadCategoryTypeList()
  dialogLoading.value = false
}

function handleAdd() { openDialog() }
function handleEdit(row) { openDialog(row) }

async function handleView(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  try {
    const res = await detailCategory({ id: row.id })
    if (res.code === 1 && res.data) {
      detailInfo.value = res.data.basicInfo || res.data
    }
  } catch { } finally { detailLoading.value = false }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false); if (!valid) return
  const params = { ...formData }
  params.type = Array.isArray(formData.type) ? formData.type.join(',') : formData.type
  // 上级分类ID以字符串传参，避免Long精度丢失
  if (params.parentId === null || params.parentId === undefined || params.parentId === '') {
    params.parentId = -1
  } else {
    params.parentId = String(params.parentId)
  }
  if (isEdit.value && params.parentId != null && String(params.parentId) === String(params.id)) {
    ElMessage.warning('不能选择自己作为上级类别')
    return
  }
  submitLoading.value = true
  try {
    if (isEdit.value) { await updateCategory(params) } else { await saveCategory(params) }
    ElMessage.success(isEdit.value ? '修改成功' : '添加成功'); dialogVisible.value = false; loadRootData()
  } catch { } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该类别吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteCategory({ id: row.id }); ElMessage.success('删除成功'); loadRootData() } catch { } }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个类别吗？`, '批量删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteCategories(selectedRows.value); ElMessage.success('批量删除成功'); loadRootData() } catch { } }).catch(() => {})
}

function handleFlushCache() {
  ElMessageBox.confirm('确定刷新缓存?', '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { const res = await flushAllCategoryCache(); ElMessage.success(res.msg || '刷新成功') } catch { } }).catch(() => {})
}

function handleClearCache() {
  ElMessageBox.confirm('确定清空缓存?', '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { const res = await clearCategoryIndexCache(); ElMessage.success(res.msg || '清空成功') } catch { } }).catch(() => {})
}

loadRootData()
</script>

<style lang="scss" scoped>
.category-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  :deep(.el-descriptions__table) { width: 100%; }
  :deep(.el-descriptions__label) { word-break: keep-all; }
  :deep(.el-descriptions__content) { word-break: break-all; overflow-wrap: anywhere; min-width: 0; }
}
</style>
