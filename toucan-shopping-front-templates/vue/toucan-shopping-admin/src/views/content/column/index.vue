<template>
  <div class="column-management">
    <h2 class="page-title">栏目列表</h2>
    <div class="layout-split">
      <!-- 左侧栏目类型树 -->
      <div class="left-tree">
        <el-card shadow="never" class="tree-card">
          <template #header><span>栏目类型</span></template>
          <el-tree
            ref="typeTreeRef"
            :data="typeTreeData"
            :props="{ children: 'children', label: 'name' }"
            node-key="code"
            highlight-current
            @node-click="onTypeNodeClick"
          >
            <template #default="{ data }">
              <span>{{ data.name }}</span>
            </template>
          </el-tree>
        </el-card>
      </div>
      <!-- 右侧表格区 -->
      <div class="right-table">
        <el-card shadow="never" class="search-card">
          <el-form :model="searchForm" :inline="true">
            <el-form-item label="标题">
              <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="编码">
              <el-input v-model="searchForm.code" placeholder="请输入编码" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="显示状态">
              <el-select v-model="searchForm.showStatus" placeholder="全部" clearable style="width:120px">
                <el-option label="显示" value="1" />
                <el-option label="隐藏" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item label="栏目类型">
              <el-select v-model="searchForm.type" placeholder="全部" clearable style="width:140px">
                <el-option v-for="t in columnTypeDictList" :key="t.code" :label="t.name" :value="t.code" />
              </el-select>
            </el-form-item>
            <el-form-item label="栏目位置">
              <el-select v-model="searchForm.position" placeholder="全部" clearable style="width:140px">
                <el-option v-for="p in columnPositionDictList" :key="p.code" :label="p.name" :value="p.code" />
              </el-select>
            </el-form-item>
            <el-form-item label="开始时间">
              <el-date-picker v-model="searchForm.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
            </el-form-item>
            <el-form-item label="结束时间">
              <el-date-picker v-model="searchForm.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" v-permission="'toucan:content:column:list'" @click="handleSearch">搜索</el-button>
              <el-button :icon="RefreshRight" v-permission="'toucan:content:column:list'" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <el-button type="primary" :icon="Plus"  v-permission="'toucan:content:column:toolbar:save'"  @click="handleAdd">添加栏目</el-button>
            <el-button type="danger" :icon="Delete" :disabled="selectedRows.length === 0"  v-permission="'toucan:content:column:toolbar:delete'"   @click="handleBatchDelete">批量删除</el-button>
          </div>
          <el-table
            ref="tableRef"
            :key="searchMode ? 'search' : 'tree'"
            :data="tableData"
            border stripe v-loading="loading"
            row-key="id"
            lazy
            :load="loadChildren"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            @selection-change="onSelectionChange"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
            <el-table-column prop="code" label="编码" width="180" />
            <el-table-column prop="clickPath" label="跳转地址" width="200" show-overflow-tooltip />
            <el-table-column prop="parentTitle" label="上级栏目" width="160" />
            <el-table-column prop="typeNames" label="栏目类型" width="180">
              <template #default="{ row }">
                <el-tag v-for="(t, i) in (row.typeDictVos || [])" :key="i" size="small" type="info" style="margin-right: 4px">{{ t.name }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="positionNames" label="栏目位置" width="180">
              <template #default="{ row }">
                <el-tag v-for="(p, i) in (row.positionDictVos || [])" :key="i" size="small" type="warning" style="margin-right: 4px">{{ p.name }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="columnSort" label="排序" width="80" align="center" />
            <el-table-column prop="startShowDate" label="开始展示时间" width="170" />
            <el-table-column prop="endShowDate" label="结束展示时间" width="170" />
            <el-table-column prop="extendProperty" label="扩展属性" width="150" show-overflow-tooltip />
            <el-table-column prop="createAdminName" label="创建人" width="120" />
            <el-table-column prop="createDate" label="创建时间" width="170" />
            <el-table-column prop="updateAdminName" label="修改人" width="120" />
            <el-table-column prop="updateDate" label="修改时间" width="170" />
            <el-table-column label="操作" width="220" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="info" link size="small" :icon="View" v-permission="'toucan:content:column:detail'" @click="handleDetail(row)">查看</el-button>
                <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:content:column:btn:edit'"  @click="handleEdit(row)">编辑</el-button>
                <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:content:column:row:delete'"  @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px" v-loading="dialogLoading">
        <el-form-item label="栏目类型">
          <el-input :model-value="currentColumnTypeName" disabled />
        </el-form-item>
        <el-form-item label="上级栏目" prop="pid">
          <el-tree-select
            v-model="formData.pid"
            :data="parentColumnTree"
            :props="{ children: 'children', label: 'title', value: 'id' }"
            check-strictly
            clearable
            placeholder="请选择上级栏目（不选则为根节点）"
            style="width:100%"
            :load="loadParentColumnTree"
            lazy
          />
        </el-form-item>
        <el-form-item label="栏目标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入栏目标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="栏目编码" prop="code">
          <el-input v-model="formData.code" placeholder="字母、数字、下划线" maxlength="100" />
        </el-form-item>
        <el-form-item label="跳转地址">
          <el-input v-model="formData.clickPath" placeholder="请输入点击跳转地址" maxlength="255" />
        </el-form-item>
        <el-form-item label="开始展示时间" prop="startShowDate">
          <el-date-picker v-model="formData.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束展示时间" prop="endShowDate">
          <el-date-picker v-model="formData.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="width:100%" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-radio-group v-model="formData.showStatus">
            <el-radio value="1">显示</el-radio>
            <el-radio value="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="栏目类型" prop="type">
          <el-checkbox-group v-model="formData.type">
            <el-checkbox v-for="t in columnTypeDictList" :key="t.code" :value="t.code">{{ t.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="栏目位置" prop="position">
          <el-radio-group v-model="formData.position">
            <el-radio v-for="p in columnPositionDictList" :key="p.code" :value="p.code">{{ p.name }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.columnSort" :min="0" placeholder="请输入排序" style="width:100%" />
        </el-form-item>
        <el-form-item label="扩展属性">
          <el-input v-model="formData.extendProperty" type="textarea" :rows="2" maxlength="255" placeholder="请输入扩展属性" />
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
    <el-dialog v-model="detailVisible" title="栏目详情" width="720px" :close-on-click-modal="false" destroy-on-close>
      <el-descriptions :column="2" border v-loading="detailLoading">
        <el-descriptions-item label="栏目类型">{{ detailData?.columnTypeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="上级栏目">{{ detailData?.parentTitle || '-' }}</el-descriptions-item>
        <el-descriptions-item label="栏目标题">{{ detailData?.title || '-' }}</el-descriptions-item>
        <el-descriptions-item label="栏目编码">{{ detailData?.code || '-' }}</el-descriptions-item>
        <el-descriptions-item label="跳转地址">{{ detailData?.clickPath || '-' }}</el-descriptions-item>
        <el-descriptions-item label="展示类型">{{ detailData?.typeNames || '-' }}</el-descriptions-item>
        <el-descriptions-item label="栏目位置">{{ detailData?.positionNames || '-' }}</el-descriptions-item>
        <el-descriptions-item label="显示状态">{{ detailData?.showStatus == 1 ? '显示' : '隐藏' }}</el-descriptions-item>
        <el-descriptions-item label="排序">{{ detailData?.columnSort ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="开始展示时间">{{ detailData?.startShowDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="结束展示时间">{{ detailData?.endShowDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="扩展属性">{{ detailData?.extendProperty || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailData?.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData?.createAdminName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData?.createDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="修改人">{{ detailData?.updateAdminName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ detailData?.updateDate || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Search, RefreshRight, View } from '@element-plus/icons-vue'
import { queryTreeTable, queryColumnTypeList, queryColumnDict, queryColumnTree, queryColumnById, queryColumnDetail, saveColumn, updateColumn, deleteColumn, deleteColumns } from '@/api/content/column'

// ========== 左侧栏目类型树 ==========
const typeTreeRef = ref(null)
const typeTreeData = ref([])
const selectedColumnTypeCode = ref('')
const columnTypeDictList = ref([])
const columnPositionDictList = ref([])

async function loadColumnTypes() {
  try {
    const res = await queryColumnTypeList()
    if (res.code === 1 && res.data) {
      typeTreeData.value = res.data || []
    }
  } catch { }
}

async function loadColumnDict() {
  dialogLoading.value = true
  try {
    const res = await queryColumnDict()
    if (res.code === 1 && res.data) {
      columnTypeDictList.value = res.data.columnTypeList || []
      columnPositionDictList.value = res.data.columnPositionList || []
    }
  } catch { } finally {
    dialogLoading.value = false
  }
}

function onTypeNodeClick(data) {
  selectedColumnTypeCode.value = data.code
  searchForm.columnTypeCode = data.code
  handleSearch()
}

// ========== 搜索 ==========
const searchForm = reactive({
  columnTypeCode: '-1', title: '', code: '', showStatus: '',
  type: '', startShowDate: '', endShowDate: '', position: ''
})

// 是否为搜索模式（有查询条件时结果为平铺列表，不展示树形结构）
const searchMode = ref(false)

function hasSearchCriteria() {
  return !!(searchForm.title || searchForm.code || searchForm.showStatus ||
    searchForm.type || searchForm.position ||
    searchForm.startShowDate || searchForm.endShowDate)
}

function handleSearch() {
  if (selectedColumnTypeCode.value) {
    searchForm.columnTypeCode = selectedColumnTypeCode.value
  }
  searchMode.value = hasSearchCriteria()
  loadRootData()
}

function handleReset() {
  searchForm.title = ''
  searchForm.code = ''
  searchForm.showStatus = ''
  searchForm.type = ''
  searchForm.startShowDate = ''
  searchForm.endShowDate = ''
  searchForm.position = ''
  searchMode.value = false
  loadRootData()
}

// ========== 树表格 ==========
const tableRef = ref(null)
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])

function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  return {
    columnTypeCode: searchForm.columnTypeCode || selectedColumnTypeCode.value || '-1',
    title: searchForm.title || undefined,
    code: searchForm.code || undefined,
    showStatus: searchForm.showStatus || undefined,
    type: searchForm.type || undefined,
    startShowDate: searchForm.startShowDate || undefined,
    endShowDate: searchForm.endShowDate || undefined,
    position: searchForm.position || undefined,
    pid: -1
  }
}

async function loadRootData() {
  loading.value = true
  try {
    const res = await queryTreeTable(buildSearchParams())
    tableData.value = (res.data || []).map(item => ({
      ...item,
      hasChildren: !searchMode.value
    }))
  } finally {
    loading.value = false
  }
}

async function loadChildren(row, treeNode, resolve) {
  try {
    const params = buildSearchParams()
    params.pid = row.id
    const res = await queryTreeTable(params)
    const children = (res.data || []).map(item => ({
      ...item,
      hasChildren: true
    }))
    resolve(children)
  } catch {
    resolve([])
  }
}

// ========== 添加/编辑弹窗 ==========
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const editingId = ref(null)
const currentColumnTypeName = ref('')
const parentColumnTree = ref([])
const detailVisible = ref(false)
const detailLoading = ref(false)
const detailData = ref(null)

const dialogTitle = computed(() => isEdit.value ? '编辑栏目' : '添加栏目')

const formData = reactive({
  id: null, columnTypeCode: '', pid: null, title: '', code: '', clickPath: '',
  startShowDate: '', endShowDate: '', showStatus: '1', type: [], position: '1',
  columnSort: 0, extendProperty: '', remark: ''
})

const formRules = {
  title: [{ required: true, message: '请输入栏目标题', trigger: 'blur' }],
  code: [{ required: true, message: '请输入栏目编码', trigger: 'blur' }],
  type: [{ type: 'array', required: true, message: '请选择栏目类型', trigger: 'change' }],
  position: [{ required: true, message: '请选择栏目位置', trigger: 'change' }],
  startShowDate: [{ required: true, message: '请选择开始展示时间', trigger: 'change' }],
  endShowDate: [{ required: true, message: '请选择结束展示时间', trigger: 'change' }]
}

function resetForm() {
  formData.id = null; formData.columnTypeCode = selectedColumnTypeCode.value
  formData.pid = null; formData.title = ''; formData.code = ''; formData.clickPath = ''
  formData.startShowDate = ''; formData.endShowDate = ''; formData.showStatus = '1'
  formData.type = []; formData.position = '1'; formData.columnSort = 0
  formData.extendProperty = ''; formData.remark = ''
}

async function loadParentColumnTree(node, resolve) {
  try {
    const params = { columnTypeCode: formData.columnTypeCode }
    // 首次加载不传 id 让后端返回「根节点」；展开时传当前节点 id
    const id = node?.data?.id
    if (id != null) params.id = id
    const res = await queryColumnTree(params)
    const nodes = (res.data || []).map(item => ({ ...item, isLeaf: !item.isParent }))
    resolve(nodes)
  } catch {
    resolve([])
  }
}

function handleAdd() {
  if (!selectedColumnTypeCode.value || selectedColumnTypeCode.value === '-1') {
    ElMessage.warning('请先在左侧选择栏目类型')
    return
  }
  currentColumnTypeName.value = typeTreeRef.value?.getCurrentNode()?.name || ''
  isEdit.value = false; editingId.value = null
  resetForm()
  dialogVisible.value = true
  loadColumnDict()
}

async function handleEdit(row) {
  try {
    const res = await queryColumnById({ id: row.id })
    if (res.code === 1 && res.data) {
      const d = res.data
      isEdit.value = true; editingId.value = row.id
      currentColumnTypeName.value = d.columnTypeName || ''
      formData.id = d.id
      formData.columnTypeCode = d.columnTypeCode || selectedColumnTypeCode.value
      formData.pid = d.pid != null && Number(d.pid) !== -1 ? d.pid : null
      formData.title = d.title || ''
      formData.code = d.code || ''
      formData.clickPath = d.clickPath || ''
      formData.startShowDate = d.startShowDate || ''
      formData.endShowDate = d.endShowDate || ''
      formData.showStatus = d.showStatus != null ? String(d.showStatus) : '1'
      formData.type = d.type ? String(d.type).split(',').filter(Boolean) : []
      formData.position = d.position || '1'
      formData.columnSort = d.columnSort || 0
      formData.extendProperty = d.extendProperty || ''
      formData.remark = d.remark || ''
      dialogVisible.value = true
      loadColumnDict()
    } else {
      ElMessage.error(res.msg || '查询栏目详情失败')
    }
  } catch {
    // 拦截器已提示错误
  }
}

async function handleDetail(row) {
  detailVisible.value = true
  detailLoading.value = true
  detailData.value = null
  try {
    const res = await queryColumnDetail({ id: row.id })
    if (res.code === 1 && res.data) {
      detailData.value = res.data.basicInfo || res.data
    }
  } catch { } finally {
    detailLoading.value = false
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const data = {
      ...formData,
      type: Array.isArray(formData.type) ? formData.type.join(',') : formData.type,
      columnSort: Number(formData.columnSort)
    }
    // 上级栏目ID：不传则为-1（根节点）；传参以字符串形式避免Long精度丢失
    if (data.pid === null || data.pid === undefined || data.pid === '') {
      data.pid = -1
    } else {
      data.pid = String(data.pid)
    }
    if (isEdit.value) {
      await updateColumn(data)
      ElMessage.success('修改成功')
    } else {
      await saveColumn(data)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadRootData()
  } catch { } finally { submitLoading.value = false }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm('确定删除该栏目吗？', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteColumn({ id: row.id }); ElMessage.success('删除成功'); loadRootData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个栏目吗？`, '批量删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteColumns(selectedRows.value.map(r => ({ id: r.id }))); ElMessage.success('批量删除成功'); loadRootData() } catch { }
  }).catch(() => {})
}

loadColumnTypes()
loadColumnDict()
</script>

<style lang="scss" scoped>
.column-management {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
  }
  .right-table { flex: 1; overflow: auto; }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
