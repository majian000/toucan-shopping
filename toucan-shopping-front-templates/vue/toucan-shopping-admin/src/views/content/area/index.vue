<template>
  <div class="area-management">
    <h2 class="page-title">地区列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="searchForm.code" placeholder="请输入编码" clearable style="width:160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">添加地区</el-button>
        <el-button type="danger" :icon="Delete" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="warning" :icon="Refresh" @click="handleFlushCache">刷新全部缓存</el-button>
      </div>
      <el-table
        ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id"
        lazy :load="loadChildren" :tree-props="{ children: 'children', hasChildren: 'haveChild' }"
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="name" label="名称" width="200" />
        <el-table-column prop="code" label="编码" width="120" />
        <el-table-column prop="areaSort" label="排序" width="80" align="center" />
        <el-table-column prop="isMunicipality" label="直辖市" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isMunicipality === 1 ? 'warning' : 'info'" size="small">
              {{ row.isMunicipality === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="countryName" label="国家" width="120" />
        <el-table-column prop="countryCode" label="国家编码" width="100" />
        <el-table-column prop="bigAreaName" label="大区名称" width="120" />
        <el-table-column prop="bigAreaCode" label="大区编码" width="100" />
        <el-table-column prop="createAdminName" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminName" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="210" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" v-permission="'toucan:area:detail'" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px" v-loading="dialogLoading">
        <el-form-item label="上级地区">
          <el-tree-select
            v-model="formData.pid" :data="parentAreaTree"
            :props="{ children: 'children', label: 'name', value: 'id', isLeaf: 'leaf' }"
            check-strictly clearable placeholder="请选择上级地区（不选则为根节点）" style="width:100%"
            :load="loadParentAreaTree" lazy node-key="id"
          />
        </el-form-item>
        <el-form-item label="国家">
          <el-select v-model="formData.countryCode" style="width:100%">
            <el-option label="中国" value="CHN" />
          </el-select>
        </el-form-item>
        <el-form-item label="大区">
          <el-select v-model="formData.bigAreaCode" style="width:100%">
            <el-option label="华东" value="HUADONG" />
            <el-option label="华北" value="HUABEI" />
            <el-option label="华中" value="HUAZHONG" />
            <el-option label="华南" value="HUANAN" />
            <el-option label="东北" value="DONGBEI" />
            <el-option label="西北" value="XIBEI" />
            <el-option label="西南" value="XINAN" />
            <el-option label="港澳台" value="GANGAOTAI" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="formData.type" @change="onTypeChange">
            <el-radio :value="1">省</el-radio>
            <el-radio :value="2">市</el-radio>
            <el-radio :value="3">区县</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="formData.type === 1" label="省名称" prop="province">
          <el-input v-model="formData.province" placeholder="请输入省名称" maxlength="20" />
        </el-form-item>
        <el-form-item v-if="formData.type === 2" label="市名称" prop="city">
          <el-input v-model="formData.city" placeholder="请输入市名称" maxlength="20" />
        </el-form-item>
        <el-form-item v-if="formData.type === 3" label="区县名称" prop="area">
          <el-input v-model="formData.area" placeholder="请输入区县名称" maxlength="20" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码" maxlength="20" />
        </el-form-item>
        <el-form-item label="排序" prop="areaSort">
          <el-input-number v-model="formData.areaSort" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="是否直辖市">
          <el-switch v-model="formData.isMunicipality" :active-value="1" :inactive-value="0" />
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
    <el-dialog v-model="detailVisible" title="查看地区" width="650px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detailInfo" :column="2" border>
          <el-descriptions-item label="名称" :span="2">{{ detailInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="编码">{{ detailInfo.code }}</el-descriptions-item>
          <el-descriptions-item label="类型">
            <el-tag size="small">{{ detailInfo.type === 1 ? '省' : detailInfo.type === 2 ? '市' : detailInfo.type === 3 ? '区县' : detailInfo.type }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="排序">{{ detailInfo.areaSort }}</el-descriptions-item>
          <el-descriptions-item label="是否直辖市">
            <el-tag :type="detailInfo.isMunicipality === 1 ? 'warning' : 'info'" size="small">
              {{ detailInfo.isMunicipality === 1 ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="国家">{{ detailInfo.countryName }}</el-descriptions-item>
          <el-descriptions-item label="大区">{{ detailInfo.bigAreaName }}</el-descriptions-item>
          <el-descriptions-item label="国家编码">{{ detailInfo.countryCode }}</el-descriptions-item>
          <el-descriptions-item label="大区编码">{{ detailInfo.bigAreaCode }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detailInfo.remark }}</el-descriptions-item>
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
import { queryAreaTreeTable, queryAreaTree, saveArea, updateArea, deleteArea, deleteAreas, flushAllAreaCache, detailArea } from '@/api/content/area'

const searchForm = reactive({ name: '', code: '' })
function handleSearch() { loadRootData() }
function handleReset() { searchForm.name = ''; searchForm.code = '' }

const tableData = ref([]); const loading = ref(false); const selectedRows = ref([])
function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, pid: -1 }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadRootData() {
  loading.value = true
  try { const res = await queryAreaTreeTable(buildSearchParams()); tableData.value = res.data || [] } catch { } finally { loading.value = false }
}

async function loadChildren(row, treeNode, resolve) {
  try { const params = { ...searchForm, pid: row.id }; const res = await queryAreaTreeTable(params); resolve(res.data || []) } catch { resolve([]) }
}

const dialogVisible = ref(false); const dialogLoading = ref(false); const isEdit = ref(false)
const submitLoading = ref(false); const formRef = ref(null)
const parentAreaTree = ref([])
const dialogTitle = computed(() => isEdit.value ? '编辑地区' : '添加地区')

const formData = reactive({ id: null, pid: null, name: '', code: '', type: 1, province: '', city: '', area: '', areaSort: 0, isMunicipality: 0, countryCode: 'CHN', bigAreaCode: 'HUADONG', parentCode: '', remark: '' })
const formRules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  province: [{ required: true, message: '请输入省名称', trigger: 'blur' }],
  city: [{ required: true, message: '请输入市名称', trigger: 'blur' }],
  area: [{ required: true, message: '请输入区县名称', trigger: 'blur' }]
}

function resetForm() {
  formData.id = null; formData.pid = null; formData.name = ''; formData.code = ''; formData.type = 1
  formData.province = ''; formData.city = ''; formData.area = ''
  formData.areaSort = 0; formData.isMunicipality = 0; formData.countryCode = 'CHN'; formData.bigAreaCode = 'HUADONG'
  formData.parentCode = ''; formData.remark = ''
}

// 切换类型时清空其它类型的名称输入
function onTypeChange(type) {
  const t = Number(type)
  if (t === 1) { formData.city = ''; formData.area = '' }
  else if (t === 2) { formData.province = ''; formData.area = '' }
  else if (t === 3) { formData.province = ''; formData.city = '' }
}

async function loadParentAreaRoots() {
  try {
    const res = await queryAreaTreeTable({ pid: -1 })
    parentAreaTree.value = (res.data || []).map(item => ({ ...item, leaf: false }))
  } catch { }
}

async function loadParentAreaTree(node, resolve) {
  try { const res = await queryAreaTreeTable({ pid: node?.id || -1 }); resolve((res.data || []).map(item => ({ ...item, leaf: false }))) } catch { resolve([]) }
}

async function openDialog(title, row) {
  dialogVisible.value = true
  dialogLoading.value = true
  parentAreaTree.value = []
  if (row) {
    isEdit.value = true
    formData.id = row.id; formData.pid = row.pid != null && Number(row.pid) !== -1 ? row.pid : null; formData.name = row.name || ''; formData.code = row.code || ''
    formData.type = row.type != null ? row.type : 1; formData.province = row.province || ''; formData.city = row.city || ''
    formData.area = row.area || ''; formData.areaSort = row.areaSort || 0
    formData.isMunicipality = row.isMunicipality || 0; formData.countryCode = row.countryCode || 'CHN'
    formData.bigAreaCode = row.bigAreaCode || 'HUADONG'; formData.parentCode = row.parentCode || ''
    formData.remark = row.remark || ''
  } else {
    isEdit.value = false
    resetForm()
  }
  await loadParentAreaRoots()
  dialogLoading.value = false
}

function handleAdd() { openDialog() }
function handleEdit(row) { openDialog(null, row) }

// ========== 查看详情 ==========
const detailVisible = ref(false)
const detailInfo = ref(null)
const detailLoading = ref(false)

async function handleView(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  try {
    const res = await detailArea({ id: row.id })
    if (res.code === 1 && res.data) {
      detailInfo.value = res.data.basicInfo || res.data
    }
  } catch { } finally { detailLoading.value = false }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false); if (!valid) return
  const params = { ...formData }
  if (params.pid == null || params.pid === '') params.pid = -1
  if (isEdit.value && params.pid != null && String(params.pid) === String(params.id)) {
    ElMessage.warning('不能选择自己作为上级地区')
    return
  }
  submitLoading.value = true
  try {
    if (isEdit.value) { await updateArea(params) } else { await saveArea(params) }
    ElMessage.success(isEdit.value ? '修改成功' : '添加成功'); dialogVisible.value = false; loadRootData()
  } catch { } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该地区吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteArea({ id: row.id }); ElMessage.success('删除成功'); loadRootData() } catch { } }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个地区吗？`, '批量删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteAreas(selectedRows.value); ElMessage.success('批量删除成功'); loadRootData() } catch { } }).catch(() => {})
}

function handleFlushCache() {
  ElMessageBox.confirm('确定刷新全部数据缓存?', '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { const res = await flushAllAreaCache(); ElMessage.success(res.msg || '刷新成功') } catch { } }).catch(() => {})
}

loadRootData()
</script>

<style lang="scss" scoped>
.area-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
