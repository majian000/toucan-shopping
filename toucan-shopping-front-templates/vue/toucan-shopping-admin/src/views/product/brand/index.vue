<template>
  <div class="brand">
    <h2 class="page-title">品牌管理</h2>
    <div class="layout-split">
      <div class="left-tree">
        <el-card shadow="never" class="tree-card">
          <template #header>
            <div class="tree-header">
              <span>商品分类</span>
              <el-icon class="tree-refresh" title="刷新分类树" @click="handleRefreshTree"><Refresh /></el-icon>
            </div>
          </template>
          <el-tree
            v-loading="treeLoading"
            ref="categoryTreeRef"
            :data="treeData"
            :props="{ children: 'children', label: 'name' }"
            node-key="id"
            highlight-current
            :expand-on-click-node="false"
            @node-click="onNodeClick"
          />
        </el-card>
      </div>
      <div class="right-table">
        <el-card shadow="never" class="search-card">
          <el-form :model="searchForm" :inline="true">
            <el-form-item label="品牌ID">
              <el-input v-model="searchForm.id" placeholder="请输入品牌ID" clearable style="width:150px" />
            </el-form-item>
            <el-form-item label="品牌名">
              <el-input v-model="searchForm.name" placeholder="请输入品牌名" clearable style="width:160px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
              <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <el-button type="primary" :icon="Plus" v-permission="'toucan:product:brand:btn:add'" @click="handleAdd">添加</el-button>
            <el-button type="danger" :icon="Delete" v-permission="'toucan:product:brand:btn:delete'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
          </div>
          <el-table :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="id" label="主键" width="120" />
            <el-table-column prop="categoryNamePathList" label="所属类目" min-width="260" show-overflow-tooltip>
              <template #default="{ row }">
                {{ (row.categoryNamePathList || []).join('、') }}
              </template>
            </el-table-column>
            <el-table-column prop="trademarkAreaType" label="商标注册地区" width="200" show-overflow-tooltip>
              <template #default="{ row }">
                {{ row.trademarkAreaType === 1 || row.trademarkAreaType === '1' ? '中国大陆地区' : (row.trademarkAreaType === 2 || row.trademarkAreaType === '2' ? '香港、澳门特别行政区，台湾省和境外国家' : '') }}
              </template>
            </el-table-column>
            <el-table-column prop="chineseName" label="品牌名(中文)" width="140" show-overflow-tooltip />
            <el-table-column prop="englishName" label="品牌名(英文)" width="140" show-overflow-tooltip />
            <el-table-column prop="httpLogoPath" label="LOGO" width="110" align="center">
              <template #default="{ row }">
                <el-image v-if="row.httpLogoPath" :src="row.httpLogoPath" fit="cover" style="width:60px;height:60px" :preview-src-list="[row.httpLogoPath]" preview-teleported />
              </template>
            </el-table-column>
            <el-table-column prop="registNumber1" label="商标注册号" width="130" show-overflow-tooltip />
            <el-table-column prop="seminary" label="发源地" width="110" show-overflow-tooltip />
            <el-table-column prop="ownerName" label="品牌所有人" width="120" show-overflow-tooltip />
            <el-table-column prop="enabledStatus" label="状态" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="row.enabledStatus === 1 || row.enabledStatus === '1' ? 'success' : 'info'" size="small">
                  {{ row.enabledStatus === 1 || row.enabledStatus === '1' ? '有效' : '无效' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createDate" label="创建时间" width="170" />
            <el-table-column label="操作" width="130" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" v-permission="'toucan:product:brand:btn:updateDetail'" @click="handleEdit(row)">编辑</el-button>
                <el-button type="danger" link size="small" v-permission="'toucan:product:brand:btn:delete'" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-wrap">
            <el-pagination
              v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
              :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
              :total="pagination.total" @size-change="handleSizeChange" @current-change="loadTableData"
            />
          </div>
        </el-card>
      </div>
    </div>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogMode === 'add' ? '添加品牌' : '编辑品牌'" width="620px" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="120px">
        <el-form-item label="商标注册地区" required>
          <el-select v-model="form.trademarkAreaType" style="width:100%">
            <el-option label="中国大陆地区" :value="1" />
            <el-option label="香港、澳门特别行政区，台湾省和境外国家" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="品牌名(中文)" required>
          <el-input v-model="form.chineseName" placeholder="请输入品牌名(中文)" maxlength="255" />
        </el-form-item>
        <el-form-item label="品牌名(英文)">
          <el-input v-model="form.englishName" placeholder="请输入品牌名(英文)" maxlength="255" />
        </el-form-item>
        <el-form-item label="LOGO">
          <el-upload
            :auto-upload="false"
            :show-file-list="false"
            :on-change="onLogoChange"
            :before-upload="beforeLogoUpload"
            accept="image/*"
          >
            <el-button :icon="Upload">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="LOGO预览">
          <el-image v-if="form.httpLogoPath" :src="form.httpLogoPath" fit="cover" style="width:120px;height:120px" />
        </el-form-item>
        <el-form-item label="商标注册号">
          <el-input v-model="form.registNumber1" maxlength="200" />
        </el-form-item>
        <el-form-item label="商标注册号">
          <el-input v-model="form.registNumber2" maxlength="200" />
        </el-form-item>
        <el-form-item label="发源地">
          <el-input v-model="form.seminary" maxlength="200" />
        </el-form-item>
        <el-form-item label="品牌所有人">
          <el-input v-model="form.ownerName" maxlength="100" />
        </el-form-item>
        <el-form-item label="所属分类" required>
          <el-input v-model="form.categoryNameDisplay" placeholder="请选择所属分类" readonly @click="openCategoryPicker" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.enabledStatus" style="width:100%">
            <el-option label="有效" :value="1" />
            <el-option label="无效" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确认保存</el-button>
      </template>
    </el-dialog>

    <!-- 分类多选弹窗 -->
    <el-dialog v-model="categoryPickerVisible" title="选择类别" width="420px" append-to-body>
      <el-tree
        ref="formCategoryTreeRef"
        :data="formCategoryTreeData"
        :props="{ children: 'children', label: 'name' }"
        node-key="nodeId"
        show-checkbox
        check-strictly
        default-expand-all
      />
      <template #footer>
        <el-button @click="categoryPickerVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCategoryPicker">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Plus, Delete, Upload, Refresh } from '@element-plus/icons-vue'
import {
  listBrand, queryCategoryTreeForListPage, queryCategoryTree,
  saveBrand, updateBrand, deleteBrand, deleteBrandByIds
} from '@/api/product/brand'

const searchForm = reactive({ categoryId: null, id: '', name: '' })

// ===== 左侧分类树 (列表过滤) =====
const categoryTreeRef = ref(null)
const treeData = ref([])
const treeLoading = ref(false)

function handleRefreshTree() {
  loadCategoryTree()
}

async function loadCategoryTree() {
  treeLoading.value = true
  try {
    const res = await queryCategoryTreeForListPage()
    treeData.value = res.data || []
  } catch { } finally {
    treeLoading.value = false
  }
}

function onNodeClick(data) {
  searchForm.categoryId = data.id
  handleSearch()
}

// ===== 表格 =====
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listBrand(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.id = ''; searchForm.name = ''; searchForm.categoryId = null
  handleSearch()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该品牌?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteBrand({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要操作的记录'); return }
  ElMessageBox.confirm('确定删除选中的品牌?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await deleteBrandByIds(selectedRows.value.map(r => ({ id: r.id })))
      ElMessage.success('删除成功')
      loadTableData()
    } catch { }
  }).catch(() => {})
}

// ===== 添加/编辑 =====
const dialogVisible = ref(false)
const dialogMode = ref('add')
const saving = ref(false)
const form = reactive({
  id: null, trademarkAreaType: 1, chineseName: '', englishName: '', logoPath: '', httpLogoPath: '', logoBase64: '',
  registNumber1: '', registNumber2: '', seminary: '', ownerName: '', categoryIdCache: '', categoryNameDisplay: '', enabledStatus: 1
})

function onLogoChange(file) {
  const raw = file.raw
  if (!raw) return
  const reader = new FileReader()
  reader.onload = (e) => {
    form.logoBase64 = e.target.result
    form.httpLogoPath = e.target.result
  }
  reader.readAsDataURL(raw)
}
function beforeLogoUpload(file) {
  if (!file.type.startsWith('image/')) { ElMessage.error('只能上传图片文件'); return false }
  if (file.size / 1024 / 1024 > 5) { ElMessage.error('图片大小不能超过5MB'); return false }
  return true
}

function handleAdd() {
  dialogMode.value = 'add'
  Object.assign(form, {
    id: null, trademarkAreaType: 1, chineseName: '', englishName: '', logoPath: '', httpLogoPath: '', logoBase64: '',
    registNumber1: '', registNumber2: '', seminary: '', ownerName: '', categoryIdCache: '', categoryNameDisplay: '', enabledStatus: 1
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  Object.assign(form, {
    id: row.id, trademarkAreaType: row.trademarkAreaType, chineseName: row.chineseName, englishName: row.englishName || '',
    logoPath: row.logoPath || '', httpLogoPath: row.httpLogoPath || '', logoBase64: '',
    registNumber1: row.registNumber1 || '', registNumber2: row.registNumber2 || '', seminary: row.seminary || '',
    ownerName: row.ownerName || '', categoryIdCache: (row.categoryIdCacheArray || []).join(','),
    categoryNameDisplay: (row.categoryNamePathList || []).join('、'), enabledStatus: row.enabledStatus
  })
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.chineseName && !form.englishName) { ElMessage.warning('品牌名不能为空'); return }
  if (!form.categoryIdCache) { ElMessage.warning('请选择所属分类'); return }
  const payload = {
    trademarkAreaType: form.trademarkAreaType, chineseName: form.chineseName, englishName: form.englishName,
    logoPath: form.logoPath, logoBase64: form.logoBase64, registNumber1: form.registNumber1, registNumber2: form.registNumber2,
    seminary: form.seminary, ownerName: form.ownerName, categoryIdCache: form.categoryIdCache, enabledStatus: form.enabledStatus
  }
  if (dialogMode.value === 'edit') payload.id = form.id
  saving.value = true
  try {
    if (dialogMode.value === 'add') await saveBrand(payload)
    else await updateBrand(payload)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadTableData()
  } catch { } finally { saving.value = false }
}

// ===== 分类多选 =====
const categoryPickerVisible = ref(false)
const formCategoryTreeRef = ref(null)
const formCategoryTreeData = ref([])

function collectCheckedNodeIds(nodes, arr) {
  ;(nodes || []).forEach(n => {
    if (n.state && n.state.checked) arr.push(n.nodeId)
    if (n.children && n.children.length) collectCheckedNodeIds(n.children, arr)
  })
}

async function openCategoryPicker() {
  categoryPickerVisible.value = true
  formCategoryTreeData.value = []
  try {
    const res = await queryCategoryTree({ brandId: form.id || -1 })
    formCategoryTreeData.value = res.data || []
    const checkedIds = []
    collectCheckedNodeIds(res.data || [], checkedIds)
    formCategoryTreeRef.value && formCategoryTreeRef.value.setCheckedKeys(checkedIds)
  } catch { }
}

function confirmCategoryPicker() {
  const checked = formCategoryTreeRef.value ? formCategoryTreeRef.value.getCheckedKeys() : []
  if (checked.length === 0) { ElMessage.warning('请选择类别'); return }
  // 通过已选 nodeId 反查名称路径
  const nameArr = []
  function findName(nodes, id) {
    for (const n of nodes || []) {
      if (String(n.nodeId) === String(id)) return n.name || n.title
      const r = findName(n.children, id)
      if (r) return r
    }
    return null
  }
  checked.forEach(id => {
    const nm = findName(formCategoryTreeData.value, id)
    if (nm) nameArr.push(nm)
  })
  form.categoryIdCache = checked.join(',')
  form.categoryNameDisplay = nameArr.join('、')
  categoryPickerVisible.value = false
}

loadCategoryTree()
loadTableData()
</script>

<style lang="scss" scoped>
.brand {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
  }
  .tree-header { display: flex; justify-content: space-between; align-items: center; }
  .tree-refresh { cursor: pointer; color: $text-secondary; &:hover { color: $primary; } }
  .right-table { flex: 1; overflow: auto; }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
