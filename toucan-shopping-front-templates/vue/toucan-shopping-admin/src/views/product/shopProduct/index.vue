<template>
  <div class="shop-product">
    <h2 class="page-title">店铺商品列表</h2>
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
            <el-form-item label="店铺商品ID">
              <el-input v-model="searchForm.id" placeholder="请输入店铺商品ID" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="店铺商品UUID">
              <el-input v-model="searchForm.uuid" placeholder="请输入店铺商品UUID" clearable style="width:180px" />
            </el-form-item>
            <el-form-item label="商品名称">
              <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="上架状态">
              <el-select v-model="searchForm.status" placeholder="全部" clearable style="width:120px">
                <el-option label="未上架" value="0" />
                <el-option label="已上架" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item label="店铺ID">
              <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:140px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
              <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <div class="toolbar">
            <el-button type="primary" :icon="Refresh" v-permission="'toucan:product:shopProduct:toolbar:flushSearch'" :disabled="selectedRows.length === 0" @click="handleFlushSearch">同步搜索</el-button>
          </div>
          <el-table :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="httpMainPhotoFilePath" label="商品主图" width="130" align="center">
              <template #default="{ row }">
                <el-image v-if="row.httpMainPhotoFilePath" :src="row.httpMainPhotoFilePath" fit="cover" style="width:80px;height:80px" :preview-src-list="[row.httpMainPhotoFilePath]" preview-teleported />
              </template>
            </el-table-column>
            <el-table-column prop="name" label="商品名称" min-width="300" show-overflow-tooltip>
              <template #default="{ row }">
                <el-button type="primary" link v-permission="'toucan:product:shopProduct:query:sku:list'" @click="handleViewSku(row)">{{ row.name }}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="categoryPath" label="分类路径" min-width="250" show-overflow-tooltip />
            <el-table-column prop="status" label="上架状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 || row.status === '1' ? 'success' : 'danger'" size="small">
                  {{ row.status === 1 || row.status === '1' ? '已上架' : '未上架' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createDate" label="发布时间" width="170" />
            <el-table-column label="操作" width="240" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" :icon="View" v-permission="'toucan:product:shopProduct:btn:detail'" @click="handleDetail(row)">商品详情</el-button>
                <el-button type="primary" link size="small" v-permission="'toucan:product:row:shelves'" @click="handleShelves(row)">
                  {{ row.status === 1 || row.status === '1' ? '下架' : '上架' }}
                </el-button>
                <el-button type="primary" link size="small" v-permission="'toucan:product:shopProduct:btn:previewPc'" @click="handlePreviewPc(row)">PC预览</el-button>
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

    <!-- SKU列表弹窗 -->
    <el-dialog v-model="skuDialogVisible" title="店铺商品SKU列表" width="900px" :close-on-click-modal="false" destroy-on-close>
      <el-table :data="skuList" border stripe v-loading="skuLoading" row-key="id">
        <el-table-column prop="httpMainPhoto" label="主图" width="100" align="center">
          <template #default="{ row }">
            <el-image v-if="row.httpMainPhoto" :src="row.httpMainPhoto" fit="cover" style="width:60px;height:60px" :preview-src-list="[row.httpMainPhoto]" preview-teleported />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="SKU名称" min-width="260" show-overflow-tooltip />
        <el-table-column prop="price" label="价格" width="110" align="center" />
        <el-table-column prop="stockNum" label="库存" width="100" align="center" />
        <el-table-column prop="status" label="上架状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 || row.status === '1' ? 'success' : 'danger'" size="small">
              {{ row.status === 1 || row.status === '1' ? '已上架' : '未上架' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="skuDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 商品详情弹窗 -->
    <el-dialog v-model="detailVisible" title="商品详情" width="900px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-tabs v-if="detailInfo" v-model="detailActiveTab">
          <el-tab-pane label="商品信息" name="info">
            <el-descriptions :column="2" border label-width="110px">
              <el-descriptions-item label="上架状态">
                <el-tag :type="detailInfo.status === 1 || detailInfo.status === '1' ? 'success' : 'danger'" size="small">
                  {{ detailInfo.status === 1 || detailInfo.status === '1' ? '已上架' : '未上架' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="店铺商品ID">{{ detailInfo.id }}</el-descriptions-item>
              <el-descriptions-item label="商品名称">{{ detailInfo.name || '-' }}</el-descriptions-item>
              <el-descriptions-item label="分类名称">{{ detailInfo.categoryName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="商品主图" :span="2">
                <el-image v-if="detailInfo.httpMainPhotoFilePath" :src="detailInfo.httpMainPhotoFilePath" :preview-src-list="[detailInfo.httpMainPhotoFilePath]" preview-teleported fit="contain" style="width:200px;height:200px" />
              </el-descriptions-item>
              <el-descriptions-item label="分类路径">{{ detailInfo.categoryPath || '-' }}</el-descriptions-item>
              <el-descriptions-item label="品牌中文名称">{{ detailInfo.brandChineseName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="品牌英文名称">{{ detailInfo.brandEnglishName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="店铺内分类名称">{{ detailInfo.shopCategoryName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="店铺内分类路径">{{ detailInfo.shopCategoryPath || '-' }}</el-descriptions-item>
              <el-descriptions-item label="店铺ID">{{ detailInfo.shopId }}</el-descriptions-item>
              <el-descriptions-item label="店铺名称">{{ detailInfo.shopName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="商家编码">{{ detailInfo.sellerNo || '-' }}</el-descriptions-item>
              <el-descriptions-item label="关联平台商品">{{ detailInfo.productSpuName || '-' }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="商品介绍" name="desc">
            <div v-if="detailInfo.shopProductDescriptionVO && detailInfo.shopProductDescriptionVO.productDescriptionImgs && detailInfo.shopProductDescriptionVO.productDescriptionImgs.length" class="desc-imgs">
              <img v-for="(img, i) in detailInfo.shopProductDescriptionVO.productDescriptionImgs" :key="i" :src="img.httpFilePath" class="desc-img" />
            </div>
            <el-empty v-else description="暂无商品介绍" />
          </el-tab-pane>
          <el-tab-pane label="商品预览" name="preview">
            <div v-if="(detailInfo.httpPreviewPhotoPaths && detailInfo.httpPreviewPhotoPaths.length) || (detailInfo.httpSkuPreviewPhotoPaths && detailInfo.httpSkuPreviewPhotoPaths.length)">
              <div v-if="detailInfo.httpPreviewPhotoPaths && detailInfo.httpPreviewPhotoPaths.length" class="preview-group">
                <div class="preview-title">商品预览图</div>
                <div class="preview-imgs">
                  <img v-for="(p, i) in detailInfo.httpPreviewPhotoPaths" :key="i" :src="p" class="preview-img" />
                </div>
              </div>
              <div v-if="detailInfo.httpSkuPreviewPhotoPaths && detailInfo.httpSkuPreviewPhotoPaths.length" class="preview-group">
                <div class="preview-title">SKU预览图</div>
                <div class="preview-imgs">
                  <img v-for="(p, i) in detailInfo.httpSkuPreviewPhotoPaths" :key="i" :src="p" class="preview-img" />
                </div>
              </div>
            </div>
            <el-empty v-else description="暂无预览图" />
          </el-tab-pane>
          <el-tab-pane label="支付信息" name="pay">
            <el-descriptions :column="1" border label-width="110px">
              <el-descriptions-item label="付款方式">{{ detailInfo.payMethod === 1 || detailInfo.payMethod === '1' ? '一口价(普通交易模式)' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="库存计数">{{ detailInfo.buckleInventoryMethod === 1 || detailInfo.buckleInventoryMethod === '1' ? '买家拍下减库存' : detailInfo.buckleInventoryMethod === 2 || detailInfo.buckleInventoryMethod === '2' ? '买家付款减库存' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="售后服务">{{ (detailInfo.giveInvoice === 1 || detailInfo.giveInvoice === '1' ? '提供发票' : '') }}{{ (detailInfo.changeOrReturn === 1 || detailInfo.changeOrReturn === '1' ? '、退换货承诺' : '') }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="物流信息" name="logistics">
            <el-descriptions :column="1" border label-width="110px">
              <el-descriptions-item label="提取方式">{{ detailInfo.etractMethod && String(detailInfo.etractMethod).indexOf('1') !== -1 ? '使用物流配送' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="模板名称">{{ (freightInfo && freightInfo.name) || '-' }}</el-descriptions-item>
              <el-descriptions-item label="发货地">
                {{ freightInfo ? [freightInfo.deliverProvinceName, freightInfo.deliverCityName, freightInfo.deliverAreaName].filter(v => v).join('/') : '-' }}
              </el-descriptions-item>
              <el-descriptions-item label="是否包邮">{{ freightInfo && freightInfo.freightStatus === 2 ? '包邮' : freightInfo && freightInfo.freightStatus === 1 ? '自定义运费' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="计价方式">{{ freightInfo && freightInfo.valuationMethod === 1 ? '按件数' : freightInfo && freightInfo.valuationMethod === 2 ? '按重量' : freightInfo && freightInfo.valuationMethod === 3 ? '按体积' : '-' }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="商品规格" name="sku">
            <el-table v-if="detailInfo.productSkuVOList && detailInfo.productSkuVOList.length" :data="detailInfo.productSkuVOList" border size="small" max-height="360">
              <el-table-column prop="name" label="名称" min-width="260" show-overflow-tooltip />
              <el-table-column prop="price" label="单价" width="100" align="center" />
              <el-table-column prop="attributes" label="属性" min-width="140" show-overflow-tooltip />
              <el-table-column prop="stockNum" label="库存数量" width="100" align="center" />
              <el-table-column label="预览图" width="120" align="center">
                <template #default="{ row }">
                  <el-image v-if="row.httpMainPhoto" :src="row.httpMainPhoto" :preview-src-list="[row.httpMainPhoto]" preview-teleported fit="contain" style="width:80px;height:80px" />
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-else description="暂无规格" />
          </el-tab-pane>
        </el-tabs>
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
import { Search, RefreshRight, Refresh, View } from '@element-plus/icons-vue'
import { listShopProduct, queryCategoryTreeByPid, queryShopProductSkuList, shelves, flushSearch, detailShopProduct } from '@/api/product/shopProduct'
import { detailFreightTemplate } from '@/api/seller/freightTemplate'

const searchForm = reactive({ categoryId: null, id: '', uuid: '', name: '', status: '', shopId: '' })

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
    const res = await listShopProduct(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.id = ''; searchForm.uuid = ''; searchForm.name = ''; searchForm.status = ''; searchForm.shopId = ''; searchForm.categoryId = null
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
      await flushSearch({ ids: selectedRows.value.map(r => r.id) })
      ElMessage.success('同步成功')
      loadTableData()
    } catch { }
  }).catch(() => {})
}

// ===== SKU列表弹窗 =====
const skuDialogVisible = ref(false)
const skuLoading = ref(false)
const skuList = ref([])

async function handleViewSku(row) {
  skuDialogVisible.value = true
  skuLoading.value = true
  skuList.value = []
  try {
    const res = await queryShopProductSkuList({ shopProductId: row.id, page: 1, limit: 100 })
    skuList.value = res.data || []
  } catch { } finally { skuLoading.value = false }
}

// ===== PC预览 / 商品详情 =====
const pcBasePath = import.meta.env.VITE_PC_BASE_PATH || ''

function handlePreviewPc(row) {
  window.open(pcBasePath + '/page/product/preview/pid/' + row.id)
}

const detailVisible = ref(false)
const detailInfo = ref(null)
const detailLoading = ref(false)
const detailActiveTab = ref('info')
const freightInfo = ref(null)

async function handleDetail(row) {
  detailVisible.value = true
  detailInfo.value = null
  freightInfo.value = null
  detailLoading.value = true
  detailActiveTab.value = 'info'
  try {
    const res = await detailShopProduct({ id: row.id })
    detailInfo.value = (res && res.data) || null
    const info = detailInfo.value
    if (info && info.freightTemplateId) {
      try {
        const ftRes = await detailFreightTemplate({ id: info.freightTemplateId })
        freightInfo.value = (ftRes && ftRes.data) || null
      } catch { /* 运费模板加载失败不影响整体详情 */ }
    }
  } catch { } finally {
    detailLoading.value = false
  }
}

loadTableData()
</script>

<style lang="scss" scoped>
.shop-product {
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
  .desc-imgs { display: flex; flex-direction: column; gap: 12px; align-items: center;
    .desc-img { max-width: 100%; max-height: 420px; border: 1px solid $border-light; border-radius: 4px; }
  }
  .preview-group { margin-bottom: 16px;
    .preview-title { font-weight: 600; margin-bottom: 8px; color: $text-primary; }
    .preview-imgs { display: flex; flex-wrap: wrap; gap: 12px;
      .preview-img { width: 180px; height: 180px; object-fit: contain; border: 1px solid $border-light; border-radius: 4px; }
    }
  }
}
</style>
