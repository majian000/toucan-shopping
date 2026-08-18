<template>
  <div class="product-spu">
    <h2 class="page-title">平台SPU管理</h2>
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
            :key="treeKey"
            ref="categoryTreeRef"
            :data="treeData"
            :props="{ children: 'children', label: 'name', isLeaf: 'isLeaf' }"
            node-key="id"
            lazy
            :load="loadTreeNodes"
            :expand-on-click-node="false"
            highlight-current
            @node-click="onNodeClick"
          />
        </el-card>
      </div>
      <div class="right-table">
        <el-card shadow="never" class="search-card">
          <el-form :model="searchForm" :inline="true">
            <el-form-item label="商品ID">
              <el-input v-model="searchForm.id" placeholder="请输入商品ID" clearable style="width:150px" />
            </el-form-item>
            <el-form-item label="商品UUID">
              <el-input v-model="searchForm.uuid" placeholder="请输入商品UUID" clearable style="width:180px" />
            </el-form-item>
            <el-form-item label="商品名称">
              <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="上架状态">
              <el-select v-model="searchForm.status" placeholder="全部" clearable style="width:120px">
                <el-option label="已上架" value="1" />
                <el-option label="未上架" value="0" />
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
            <el-button type="primary" :icon="Plus" v-permission="'toucan:product:spu:btn:add'" @click="handleAdd">添加</el-button>
            <el-button type="danger" :icon="Delete" v-permission="'toucan:product:spu:btn:delete'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
          </div>
          <el-table :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="name" label="商品名称" min-width="300" show-overflow-tooltip />
            <el-table-column prop="categoryPath" label="分类路径" min-width="250" show-overflow-tooltip />
            <el-table-column prop="status" label="上架状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 || row.status === '1' ? 'success' : 'danger'" size="small">
                  {{ row.status === 1 || row.status === '1' ? '已上架' : '未上架' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createDate" label="创建时间" width="170" />
            <el-table-column prop="updateDate" label="修改时间" width="170" />
            <el-table-column label="操作" width="130" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" v-permission="'toucan:product:spu:btn:edit'" @click="handleEdit(row)">编辑</el-button>
                <el-button type="danger" link size="small" v-permission="'toucan:product:spu:btn:delete'" @click="handleDelete(row)">删除</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogMode === 'add' ? '添加平台商品' : '编辑平台商品'" width="680px" :close-on-click-modal="false" destroy-on-close>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="商品信息" name="info">
          <el-form :model="form" label-width="90px">
            <el-form-item label="商品名称" required>
              <el-input v-model="form.name" placeholder="请输入商品名称" maxlength="100" />
            </el-form-item>
            <el-form-item label="商品分类" required>
              <el-input v-model="form.categoryName" placeholder="请选择商品分类" readonly @click="categoryPickerVisible = true" />
            </el-form-item>
            <el-form-item label="所属品牌" required>
              <el-input v-model="form.brandName" placeholder="请选择所属品牌" readonly @click="openBrandPicker" />
            </el-form-item>
            <el-form-item label="上架状态">
              <el-radio-group v-model="form.status">
                <el-radio :value="1">已上架</el-radio>
                <el-radio :value="0">未上架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="公共属性" name="attr">
          <div v-if="form.categoryId == null || form.categoryId === -1" style="min-height:200px">
            <el-empty description="请先在「商品信息」中选择商品分类" />
          </div>
          <div v-else v-loading="attributeLoading" class="attr-panel" style="max-height: 420px; overflow-y: auto;">
            <AttributeCheckboxTree
              v-if="attributeTree.length"
              :nodes="attributeTree"
              :selected-value-ids="checkedValueIds"
              @toggle="handleToggleAttribute"
            />
            <el-empty v-else-if="!attributeLoading" description="该分类下暂无公共属性" />

            <div v-if="attributePreviewTree.length" class="attr-preview-panel">
              <div class="attr-preview-header">属性预览</div>
              <AttributePreview :nodes="attributePreviewTree" />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确认保存</el-button>
      </template>
    </el-dialog>

    <!-- 分类选择弹窗 -->
    <el-dialog v-model="categoryPickerVisible" title="选择商品分类" width="360px" append-to-body>
      <div style="max-height: 420px; overflow-y: auto;">
        <el-tree
          v-loading="treeLoading"
          ref="pickerTreeRef"
          :data="treeData"
          :props="{ children: 'children', label: 'name', isLeaf: 'isLeaf' }"
          node-key="id"
          lazy
          :load="loadTreeNodes"
          @node-click="onPickerNodeClick"
        />
      </div>
    </el-dialog>

    <!-- 品牌选择弹窗 -->
    <el-dialog v-model="brandPickerVisible" title="选择品牌" width="1000px" append-to-body>
      <div class="brand-search">
        <el-form :model="brandSearchForm" :inline="true">
          <el-form-item label="品牌ID">
            <el-input v-model="brandSearchForm.id" placeholder="请输入品牌ID" clearable style="width:160px" />
          </el-form-item>
          <el-form-item label="品牌名">
            <el-input v-model="brandSearchForm.name" placeholder="请输入品牌名" clearable style="width:160px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleBrandSearch">搜索</el-button>
            <el-button :icon="RefreshRight" @click="handleBrandReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="brandList" border stripe v-loading="brandLoading" row-key="id" height="400">
        <el-table-column prop="id" label="主键" width="150" />
        <el-table-column label="所属类目" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">{{ (row.categoryNamePathList || []).join('、') }}</template>
        </el-table-column>
        <el-table-column label="商标注册地区" width="170" show-overflow-tooltip>
          <template #default="{ row }">{{ row.trademarkAreaType === 1 || row.trademarkAreaType === '1' ? '中国大陆地区' : (row.trademarkAreaType === 2 || row.trademarkAreaType === '2' ? '香港、澳门特别行政区，台湾省和境外国家' : '') }}</template>
        </el-table-column>
        <el-table-column prop="chineseName" label="品牌名(中文)" width="140" show-overflow-tooltip />
        <el-table-column prop="englishName" label="品牌名(英文)" width="140" show-overflow-tooltip />
        <el-table-column label="LOGO" width="80" align="center">
          <template #default="{ row }">
            <el-image v-if="row.httpLogoPath" :src="row.httpLogoPath" fit="cover" style="width:40px;height:40px" :preview-src-list="[row.httpLogoPath]" preview-teleported />
          </template>
        </el-table-column>
        <el-table-column prop="registNumber1" label="商标注册号" width="130" show-overflow-tooltip />
        <el-table-column prop="ownerName" label="品牌所有人" width="120" show-overflow-tooltip />
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enabledStatus === 1 || row.enabledStatus === '1' ? 'success' : 'info'" size="small">
              {{ row.enabledStatus === 1 || row.enabledStatus === '1' ? '有效' : '无效' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="80" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="onSelectBrand(row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap" style="margin-top: 12px; display: flex; justify-content: flex-end;">
        <el-pagination
          v-model:current-page="brandPagination.page"
          v-model:page-size="brandPagination.limit"
          :page-sizes="[15, 30, 100, 200]"
          layout="total, sizes, prev, pager, next"
          :total="brandPagination.total"
          @size-change="handleBrandSizeChange"
          @current-change="loadBrandList"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Plus, Delete, Refresh } from '@element-plus/icons-vue'
import AttributeCheckboxTree from './AttributeCheckboxTree.vue'
import AttributePreview from './AttributePreview.vue'
import {
  listProductSpu, queryCategoryTreeByPid, listBrand,
  saveProductSpu, updateProductSpu, deleteProductSpu, deleteProductSpuByIds,
  queryAttributeTreePage, findByIdProductSpu
} from '@/api/product/productSpu'

const searchForm = reactive({ categoryId: null, id: '', uuid: '', name: '', status: '' })

// ===== 左侧分类树 =====
const categoryTreeRef = ref(null)
const pickerTreeRef = ref(null)
const treeData = ref([])
const treeKey = ref(0)
const treeLoading = ref(false)

function handleRefreshTree() {
  treeKey.value++
}

function resolveTreeNodes(children) {
  return (children || []).map(item => ({ ...item, isLeaf: !(item.isParent === true || item.isParent === 'true') }))
}

async function loadTreeNodes(node, resolve) {
  const parentId = node && node.data && node.data.id != null ? node.data.id : -1
  treeLoading.value = true
  try {
    const res = await queryCategoryTreeByPid({ parentId })
    resolve(resolveTreeNodes(res.data))
  } catch {
    resolve([])
  } finally {
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
    const res = await listProductSpu(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.id = ''; searchForm.uuid = ''; searchForm.name = ''; searchForm.status = ''; searchForm.categoryId = null
  handleSearch()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该平台商品?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteProductSpu({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要操作的记录'); return }
  ElMessageBox.confirm('确定删除选中的平台商品?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await deleteProductSpuByIds(selectedRows.value.map(r => ({ id: r.id })))
      ElMessage.success('删除成功')
      loadTableData()
    } catch { }
  }).catch(() => {})
}

// ===== 添加/编辑 =====
const dialogVisible = ref(false)
const dialogMode = ref('add')
const saving = ref(false)
const form = reactive({ id: null, name: '', categoryId: null, categoryName: '', brandId: null, brandName: '', status: 1 })

function handleAdd() {
  dialogMode.value = 'add'
  resetAttributeState()
  Object.assign(form, { id: null, name: '', categoryId: searchForm.categoryId, categoryName: '', brandId: null, brandName: '', status: 1 })
  dialogVisible.value = true
  if (form.categoryId != null && form.categoryId !== -1) loadAttributeTree()
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  resetAttributeState()
  Object.assign(form, {
    id: row.id, name: row.name, categoryId: row.categoryId, categoryName: row.categoryName || '',
    brandId: row.brandId, brandName: row.brandChineseName || '', status: row.status == null ? 1 : row.status
  })
  dialogVisible.value = true
  if (form.categoryId != null && form.categoryId !== -1) loadAttributeTree()
  if (row.id != null) loadExistingAttributes(row.id)
}

function onPickerNodeClick(data) {
  if (form.categoryId !== data.id) {
    form.brandId = null
    form.brandName = ''
    selectAttributeArray.value = []
  }
  form.categoryId = data.id
  form.categoryName = data.name
  categoryPickerVisible.value = false
  loadAttributeTree()
}

const brandPickerVisible = ref(false)
const brandLoading = ref(false)
const brandList = ref([])
const brandSearchForm = reactive({ id: '', name: '' })
const brandPagination = reactive({ page: 1, limit: 15, total: 0 })

function buildBrandParams() {
  const p = { categoryId: form.categoryId, ...brandSearchForm, page: brandPagination.page, limit: brandPagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function loadBrandList() {
  brandLoading.value = true
  try {
    const res = await listBrand(buildBrandParams())
    brandList.value = res.data || []
    brandPagination.total = res.count || 0
  } catch { } finally { brandLoading.value = false }
}

function handleBrandSearch() { brandPagination.page = 1; loadBrandList() }
function handleBrandSizeChange() { brandPagination.page = 1; loadBrandList() }
function handleBrandReset() {
  brandSearchForm.id = ''
  brandSearchForm.name = ''
  handleBrandSearch()
}

async function openBrandPicker() {
  if (form.categoryId == null || form.categoryId === -1) {
    ElMessage.warning('请先选择商品分类')
    return
  }
  brandPickerVisible.value = true
  brandSearchForm.id = ''
  brandSearchForm.name = ''
  brandPagination.page = 1
  brandPagination.limit = 15
  brandPagination.total = 0
  loadBrandList()
}

function onSelectBrand(row) {
  form.brandId = row.id
  form.brandName = row.chineseName || row.englishName || ''
  brandPickerVisible.value = false
}

// ===== 公共属性 =====
const activeTab = ref('info')
const attributeTree = ref([])
const attributeLoading = ref(false)
const selectAttributeArray = ref([])
const keyNodeMap = new Map()

const checkedValueIds = computed(() =>
  selectAttributeArray.value.filter(a => a.type === 2).map(a => String(a.attributeValueId))
)

const attributePreviewTree = computed(() => {
  const keys = selectAttributeArray.value.filter(a => a.type === 1)
  const values = selectAttributeArray.value.filter(a => a.type === 2)
  const bySort = (a, b) => (Number(b.attributeSort) || 0) - (Number(a.attributeSort) || 0)

  function buildNode(key) {
    return {
      attributeKeyId: key.attributeKeyId,
      attributeName: key.attributeName,
      values: values
        .filter(v => String(v.attributeKeyId) === String(key.attributeKeyId))
        .sort(bySort)
        .map(v => v.attributeValue),
      children: keys
        .filter(k => String(k.parentAttributeKeyId) === String(key.attributeKeyId))
        .sort(bySort)
        .map(buildNode)
    }
  }

  return keys
    .filter(k => k.parentAttributeKeyId == null || String(k.parentAttributeKeyId) === '-1')
    .sort(bySort)
    .map(buildNode)
})

function indexKeyNodes(nodes) {
  for (const n of nodes) {
    keyNodeMap.set(String(n.id), n)
    if (n.children && n.children.length) indexKeyNodes(n.children)
  }
}

async function loadAttributeTree() {
  if (form.categoryId == null || form.categoryId === -1) return
  attributeLoading.value = true
  try {
    const res = await queryAttributeTreePage({ categoryId: form.categoryId, page: 1, limit: 1000 })
    if (res.code !== 1) { ElMessage.error(res.msg || '加载公共属性失败'); return }
    const raw = res.data
    const page = typeof raw === 'string' ? JSON.parse(raw) : raw
    attributeTree.value = page.list || []
    keyNodeMap.clear()
    indexKeyNodes(attributeTree.value)
  } catch { } finally { attributeLoading.value = false }
}

function resetAttributeState() {
  activeTab.value = 'info'
  attributeTree.value = []
  selectAttributeArray.value = []
  keyNodeMap.clear()
}

async function loadExistingAttributes(id) {
  try {
    const res = await findByIdProductSpu({ id })
    if (res.code !== 1) { ElMessage.error(res.msg || '加载商品属性失败'); return }
    const keys = res.data && res.data.attributeKeyValues
    if (Array.isArray(keys)) selectAttributeArray.value = keys.map(k => ({ ...k }))
  } catch { }
}

function addKeyObject(node) {
  if (!node) return
  const keyId = String(node.id)
  if (!selectAttributeArray.value.some(a => a.type === 1 && String(a.attributeKeyId) === keyId)) {
    selectAttributeArray.value.push({
      type: 1,
      attributeName: node.attributeName,
      attributeKeyId: node.id,
      parentAttributeKeyId: node.parentId,
      categoryId: node.categoryId,
      attributeSort: node.attributeSort,
      queryStatus: node.queryStatus,
      showStatus: node.showStatus
    })
  }
}

function pushAncestorKeys(node) {
  if (!node) return
  addKeyObject(node)
  if (node.parentId != null && String(node.parentId) !== '-1') {
    pushAncestorKeys(keyNodeMap.get(String(node.parentId)))
  }
}

function cleanEmptyKeys(node) {
  if (!node) return
  const keyId = String(node.id)
  const hasValues = selectAttributeArray.value.some(a => a.type === 2 && String(a.attributeKeyId) === keyId)
  const hasChildKeys = selectAttributeArray.value.some(a => a.type === 1 && String(a.parentAttributeKeyId) === keyId)
  if (!hasValues && !hasChildKeys) {
    const ki = selectAttributeArray.value.findIndex(a => a.type === 1 && String(a.attributeKeyId) === keyId)
    if (ki !== -1) selectAttributeArray.value.splice(ki, 1)
    if (node.parentId != null && String(node.parentId) !== '-1') {
      cleanEmptyKeys(keyNodeMap.get(String(node.parentId)))
    }
  }
}

function handleToggleAttribute(node, value, checked) {
  if (checked) {
    const valueId = String(value.id)
    if (!selectAttributeArray.value.some(a => a.type === 2 && String(a.attributeValueId) === valueId)) {
      selectAttributeArray.value.push({
        type: 2,
        attributeValue: value.attributeValue,
        attributeKeyId: node.id,
        attributeValueId: value.id,
        attributeSort: value.attributeSort,
        queryStatus: value.queryStatus,
        showStatus: value.showStatus
      })
    }
    pushAncestorKeys(node)
  } else {
    const valueId = String(value.id)
    const idx = selectAttributeArray.value.findIndex(a => a.type === 2 && String(a.attributeValueId) === valueId)
    if (idx !== -1) selectAttributeArray.value.splice(idx, 1)
    cleanEmptyKeys(node)
  }
}

async function handleSave() {
  if (!form.name) { ElMessage.warning('请输入商品名称'); return }
  if (form.categoryId == null || form.categoryId === -1) { ElMessage.warning('请选择商品分类'); return }
  if (form.brandId == null) { ElMessage.warning('请选择所属品牌'); return }
  const attributeKeys = selectAttributeArray.value.filter(a => a.type === 1)
  const attributeValues = selectAttributeArray.value.filter(a => a.type === 2)
  if (attributeKeys.length <= 0 || attributeValues.length <= 0) { ElMessage.warning('请选择商品属性'); return }
  const payload = {
    name: form.name, categoryId: form.categoryId, brandId: form.brandId, status: form.status,
    attributeKeys, attributeValues
  }
  if (dialogMode.value === 'edit') payload.id = form.id
  saving.value = true
  try {
    if (dialogMode.value === 'add') await saveProductSpu(payload)
    else await updateProductSpu(payload)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadTableData()
  } catch { } finally { saving.value = false }
}

const categoryPickerVisible = ref(false)

loadTableData()
</script>

<style lang="scss" scoped>
.product-spu {
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
  .attr-preview-panel {
    margin-top: $gap-md;
    padding: 12px;
    border: 1px solid $border-light;
    border-radius: 6px;
    background: #fafafa;
    .attr-preview-header {
      font-weight: 600;
      color: $text-primary;
      margin-bottom: 8px;
      font-size: 14px;
    }
  }
  .brand-search { margin-bottom: $gap-sm; }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
