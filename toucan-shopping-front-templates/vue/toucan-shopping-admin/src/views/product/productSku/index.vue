<template>
  <div class="product-sku">
    <h2 class="page-title">店铺SKU列表</h2>
    <div class="layout-split">
      <div class="left-tree">
        <el-card shadow="never" class="tree-card">
          <template #header>
            <div class="tree-header">
              <span>分类树</span>
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
            <el-form-item label="SKU ID">
              <el-input v-model="searchForm.id" placeholder="请输入SKU ID" clearable style="width:140px" />
            </el-form-item>
            <el-form-item label="SKU UUID">
              <el-input v-model="searchForm.uuid" placeholder="请输入SKU UUID" clearable style="width:180px" />
            </el-form-item>
            <el-form-item label="店铺ID">
              <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:140px" />
            </el-form-item>
            <el-form-item label="上架状态">
              <el-select v-model="searchForm.status" placeholder="全部" clearable style="width:120px">
                <el-option label="未上架" value="0" />
                <el-option label="已上架" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item label="店铺商品ID">
              <el-input v-model="searchForm.shopProductId" placeholder="请输入店铺商品ID" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="店铺商品UUID">
              <el-input v-model="searchForm.shopProductUuid" placeholder="请输入店铺商品UUID" clearable style="width:180px" />
            </el-form-item>
            <el-form-item label="商品名称">
              <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable style="width:160px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
              <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <el-button type="primary" :icon="Refresh" v-permission="'toucan:product:sku:toolbar:flushSearch'" :disabled="selectedRows.length === 0" @click="handleFlushSearch">同步搜索缓存</el-button>
          </div>
          <el-table :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="httpMainPhoto" label="商品主图" width="130" align="center">
              <template #default="{ row }">
                <el-image v-if="row.httpMainPhoto" :src="row.httpMainPhoto" fit="cover" style="width:80px;height:80px" :preview-src-list="[row.httpMainPhoto]" preview-teleported />
              </template>
            </el-table-column>
            <el-table-column prop="name" label="商品名称" min-width="300" show-overflow-tooltip />
            <el-table-column prop="price" label="价格" width="110" align="center" />
            <el-table-column prop="stockNum" label="库存数量" width="110" align="center" />
            <el-table-column prop="status" label="上架状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 || row.status === '1' ? 'success' : 'danger'" size="small">
                  {{ row.status === 1 || row.status === '1' ? '已上架' : '未上架' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createDate" label="发布时间" width="170" />
            <el-table-column label="操作" width="220" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" v-permission="'toucan:product:sku:row:shelves'" @click="handleShelves(row)">
                  {{ row.status === 1 || row.status === '1' ? '下架' : '上架' }}
                </el-button>
                <el-button type="primary" link size="small" v-permission="'toucan:product:shopProduct:sku:btn:previewPc'" @click="handlePreviewPc(row)">PC预览</el-button>
                <el-button type="info" link size="small" v-permission="'toucan:product:sku:btn:detail'" @click="handleDetail(row)">商品详情</el-button>
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
    <el-dialog v-model="detailVisible" title="商品详情" width="820px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detailInfo" :column="2" border label-width="110px">
          <el-descriptions-item label="上架状态">
            <el-tag :type="detailInfo.status === 1 || detailInfo.status === '1' ? 'success' : 'danger'" size="small">
              {{ detailInfo.status === 1 || detailInfo.status === '1' ? '已上架' : '未上架' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="SKU ID">{{ detailInfo.id }}</el-descriptions-item>
          <el-descriptions-item label="SKU UUID">{{ detailInfo.uuid || '-' }}</el-descriptions-item>
          <el-descriptions-item label="店铺商品ID">{{ detailInfo.shopProductId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="店铺商品UUID">{{ detailInfo.shopProductUuid || '-' }}</el-descriptions-item>
          <el-descriptions-item label="商品名称">{{ detailInfo.name || '-' }}</el-descriptions-item>
          <el-descriptions-item label="商品主图" :span="2">
            <el-image v-if="detailInfo.httpMainPhotoFilePath" :src="detailInfo.httpMainPhotoFilePath" :preview-src-list="[detailInfo.httpMainPhotoFilePath]" preview-teleported fit="contain" style="width:200px;height:200px" />
          </el-descriptions-item>
          <el-descriptions-item label="分类名称">{{ detailInfo.categoryName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="分类路径">{{ detailInfo.categoryPath || '-' }}</el-descriptions-item>
          <el-descriptions-item label="单价">{{ detailInfo.price }}</el-descriptions-item>
          <el-descriptions-item label="库存数量">{{ detailInfo.stockNum }}</el-descriptions-item>
          <el-descriptions-item label="商品编号">{{ detailInfo.productNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="属性">{{ detailInfo.attributes || '-' }}</el-descriptions-item>
          <el-descriptions-item label="毛重(kg)">{{ detailInfo.roughWeight }}</el-descriptions-item>
          <el-descriptions-item label="净重(kg)">{{ detailInfo.suttle }}</el-descriptions-item>
          <el-descriptions-item label="品牌中文名称">{{ detailInfo.brandChineseName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="品牌英文名称">{{ detailInfo.brandEnglishName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="店铺内分类名称">{{ detailInfo.shopCategoryName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="店铺内分类路径">{{ detailInfo.shopCategoryPath || '-' }}</el-descriptions-item>
          <el-descriptions-item label="店铺ID">{{ detailInfo.shopId }}</el-descriptions-item>
          <el-descriptions-item label="店铺名称">{{ detailInfo.shopName || '-' }}</el-descriptions-item>
        </el-descriptions>
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
import { Search, RefreshRight, Refresh } from '@element-plus/icons-vue'
import { listProductSku, queryCategoryTreeByPid, shelves, flushSearch, detailProductSku } from '@/api/product/productSku'

const searchForm = reactive({
  categoryId: null, id: '', uuid: '', shopId: '', status: '', shopProductId: '', shopProductUuid: '', name: ''
})

// ===== 左侧分类树 =====
const categoryTreeRef = ref(null)
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
    const res = await listProductSku(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.id = ''; searchForm.uuid = ''; searchForm.shopId = ''; searchForm.status = ''
  searchForm.shopProductId = ''; searchForm.shopProductUuid = ''; searchForm.name = ''; searchForm.categoryId = null
  handleSearch()
}

function handleShelves(row) {
  const action = row.status === 1 || row.status === '1' ? '下架' : '上架'
  ElMessageBox.confirm(`确定要【${action}】"${row.name}"?`, '操作确认', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await shelves({ id: row.id, shopId: row.shopId }); ElMessage.success('操作成功'); loadTableData() } catch { }
  }).catch(() => {})
}

function handleFlushSearch() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要操作的记录'); return }
  ElMessageBox.confirm('确定同步到搜索?', '操作确认', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      const productSkuVOList = selectedRows.value.map(r => ({ id: r.id }))
      await flushSearch({ productSkuVOList })
      ElMessage.success('同步成功')
      loadTableData()
    } catch { }
  }).catch(() => {})
}

// ===== PC预览 / 商品详情 =====
const pcBasePath = import.meta.env.VITE_PC_BASE_PATH || ''

function handlePreviewPc(row) {
  window.open(pcBasePath + '/page/product/preview/' + row.id)
}

const detailVisible = ref(false)
const detailInfo = ref(null)
const detailLoading = ref(false)

async function handleDetail(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  try {
    const res = await detailProductSku({ id: row.id })
    detailInfo.value = (res && res.data) || null
  } catch { } finally {
    detailLoading.value = false
  }
}

loadTableData()
</script>

<style lang="scss" scoped>
.product-sku {
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
