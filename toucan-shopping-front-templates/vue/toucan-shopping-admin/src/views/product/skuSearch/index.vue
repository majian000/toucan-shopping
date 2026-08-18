<template>
  <div class="sku-search">
    <h2 class="page-title">商品搜索</h2>
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
            :props="{ children: 'children', label: 'name' }"
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
            <el-form-item label="关键字">
              <el-input v-model="searchForm.keyword" placeholder="请输入关键字" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="商品名称">
              <el-input v-model="searchForm.productName" placeholder="请输入商品名称" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="SKU ID">
              <el-input v-model="searchForm.skuId" placeholder="请输入SKU ID" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="店铺ID">
              <el-input v-model="searchForm.sid" placeholder="请输入店铺ID" clearable style="width:160px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
              <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <el-button type="danger" :icon="Delete" v-permission="'toucan:product:sku:search:toolbar:delete'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
            <el-button type="danger" :icon="Delete" v-permission="'toucan:product:sku:search:toolbar:clear'" @click="handleClear">清空</el-button>
          </div>
          <el-table :data="tableData" border stripe v-loading="loading" row-key="skuId" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="httpProductPreviewPath" label="图片" width="80" align="center">
              <template #default="{ row }">
                <el-image v-if="row.httpProductPreviewPath" :src="row.httpProductPreviewPath" fit="cover" style="width:48px;height:48px" :preview-src-list="[row.httpProductPreviewPath]" preview-teleported />
              </template>
            </el-table-column>
            <el-table-column prop="skuId" label="SKU ID" width="150" />
            <el-table-column prop="name" label="商品名称" min-width="220" show-overflow-tooltip />
            <el-table-column prop="price" label="价格" width="110" align="center" />
            <el-table-column prop="brandName" label="品牌" width="140" show-overflow-tooltip />
            <el-table-column prop="categoryName" label="分类" width="140" show-overflow-tooltip />
            <el-table-column prop="createDate" label="创建时间" width="170" />
            <el-table-column prop="updateDate" label="修改时间" width="170" />
            <el-table-column label="操作" width="160" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" :icon="View" v-permission="'toucan:product:sku:search:btn:detail'" @click="handleDetail(row)">商品详情</el-button>
                <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:product:sku:search:row:delete'" @click="handleDelete(row)">删除</el-button>
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

    <!-- 商品详情弹窗 -->
    <el-dialog v-model="detailVisible" title="商品详情" width="860px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detailInfo" :column="2" border label-width="110px">
          <el-descriptions-item label="SKU ID">{{ detailInfo.skuId || detailInfo.id }}</el-descriptions-item>
          <el-descriptions-item label="商品名称">{{ detailInfo.name || '-' }}</el-descriptions-item>
          <el-descriptions-item label="商品主图" :span="2">
            <el-image v-if="detailInfo.httpProductPreviewPath" :src="detailInfo.httpProductPreviewPath" :preview-src-list="[detailInfo.httpProductPreviewPath]" preview-teleported fit="contain" style="width:200px;height:200px" />
          </el-descriptions-item>
          <el-descriptions-item label="分类名称">{{ detailInfo.categoryName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="分类ID">{{ detailInfo.categoryId }}</el-descriptions-item>
          <el-descriptions-item label="分类路径" :span="2">{{ (detailInfo.categoryIds || []).join(' > ') || '-' }}</el-descriptions-item>
          <el-descriptions-item label="分类名称路径">{{ detailInfo.categoryPath || '-' }}</el-descriptions-item>
          <el-descriptions-item label="单价">{{ detailInfo.price }}</el-descriptions-item>
          <el-descriptions-item label="品牌ID">{{ detailInfo.brandId }}</el-descriptions-item>
          <el-descriptions-item label="品牌名称">{{ detailInfo.brandName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="品牌中文名称">{{ detailInfo.brandNameCN || '-' }}</el-descriptions-item>
          <el-descriptions-item label="品牌英文名称">{{ detailInfo.brandNameEN || '-' }}</el-descriptions-item>
          <el-descriptions-item label="店铺ID">{{ detailInfo.shopId }}</el-descriptions-item>
          <el-descriptions-item label="店铺分类ID">{{ detailInfo.shopCategoryId }}</el-descriptions-item>
          <el-descriptions-item label="属性值">{{ detailInfo.attributeValueGroup || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-table v-if="detailInfo && detailInfo.attributes && detailInfo.attributes.length" :data="detailInfo.attributes" border size="small" style="margin-top:12px" max-height="240">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="nameId" label="属性名ID" min-width="120" />
          <el-table-column prop="name" label="属性名" min-width="120" />
          <el-table-column prop="valueId" label="属性值ID" min-width="120" />
          <el-table-column prop="value" label="属性值" min-width="160" />
        </el-table>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Delete, Refresh, View } from '@element-plus/icons-vue'
import { searchList, queryCategoryTreeByPid, deleteSearchById, deleteSearchByIds, clearSearch, detailSearchProduct } from '@/api/product/skuSearch'

const searchForm = reactive({ keyword: '', productName: '', skuId: '', sid: '', cid: null })

// ===== 左侧分类树 =====
const categoryTreeRef = ref(null)
const treeData = ref([])
const treeKey = ref(0)
const treeLoading = ref(false)

function handleRefreshTree() {
  treeKey.value++
}

function resolveTreeNodes(children) {
  return (children || []).map(item => ({ ...item, leaf: !item.haveChild }))
}

async function loadTreeNodes(node, resolve) {
  const id = node && node.data ? node.data.id : -1
  treeLoading.value = true
  try {
    const res = await queryCategoryTreeByPid({ id })
    resolve(resolveTreeNodes(res.data))
  } catch {
    resolve([])
  } finally {
    treeLoading.value = false
  }
}

function onNodeClick(data) {
  searchForm.cid = data.id
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
    const res = await searchList(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.keyword = ''; searchForm.productName = ''; searchForm.skuId = ''; searchForm.sid = ''; searchForm.cid = null
  handleSearch()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定从缓存中删除该商品吗？', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteSearchById({ skuId: row.skuId }); ElMessage.success('删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm('确定从搜索中删除选中的商品吗？(不会影响商品，可再次同步搜索)', '批量删除', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      const list = selectedRows.value.map(r => ({ skuId: r.skuId }))
      await deleteSearchByIds(list)
      ElMessage.success('删除成功')
      loadTableData()
    } catch { }
  }).catch(() => {})
}

function handleClear() {
  ElMessageBox.confirm('确定清空所有商品吗？(不会影响商品，可再次同步搜索)', '清空确认', {
    confirmButtonText: '确定清空', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await clearSearch(); ElMessage.success('清空成功'); loadTableData() } catch { }
  }).catch(() => {})
}

// ===== 商品详情弹窗 =====
const detailVisible = ref(false)
const detailInfo = ref(null)
const detailLoading = ref(false)

async function handleDetail(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  try {
    const res = await detailSearchProduct({ skuId: row.skuId })
    detailInfo.value = (res && res.data) || null
  } catch { } finally {
    detailLoading.value = false
  }
}

loadTableData()
</script>

<style lang="scss" scoped>
.sku-search {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
  }
  .tree-header { display: flex; justify-content: space-between; align-items: center; }
  .tree-refresh { cursor: pointer; color: $text-secondary; &:hover { color: $primary; } }
  .right-table { flex: 1; overflow: auto; }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
