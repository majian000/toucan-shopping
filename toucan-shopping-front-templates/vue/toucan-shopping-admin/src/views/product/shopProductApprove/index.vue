<template>
  <div class="shop-product-approve">
    <h2 class="page-title">商品审核</h2>
    <div class="layout-split">
      <div class="left-tree">
        <el-card shadow="never" class="tree-card">
          <template #header><span>分类树</span></template>
          <el-tree
            ref="categoryTreeRef"
            :data="treeData"
            :props="{ children: 'children', label: 'name' }"
            node-key="id"
            lazy
            :load="loadTreeNodes"
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
            <el-form-item label="店铺ID">
              <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:140px" />
            </el-form-item>
            <el-form-item label="审核状态">
              <el-select v-model="searchForm.approveStatus" placeholder="全部" clearable style="width:130px">
                <el-option label="审核中" value="1" />
                <el-option label="审核通过" value="2" />
                <el-option label="审核驳回" value="3" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
              <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <el-table :data="tableData" border stripe v-loading="loading" row-key="id">
            <el-table-column prop="approveStatus" label="审核状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="approveTagType(row.approveStatus)" size="small">{{ approveText(row.approveStatus) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="httpMainPhotoFilePath" label="商品主图" width="120" align="center">
              <template #default="{ row }">
                <el-image v-if="row.httpMainPhotoFilePath" :src="row.httpMainPhotoFilePath" fit="cover" style="width:80px;height:80px" :preview-src-list="[row.httpMainPhotoFilePath]" preview-teleported />
              </template>
            </el-table-column>
            <el-table-column prop="name" label="商品名称" min-width="280" show-overflow-tooltip />
            <el-table-column prop="categoryPath" label="分类路径" min-width="250" show-overflow-tooltip />
            <el-table-column prop="createDate" label="发布时间" width="170" />
            <el-table-column label="操作" width="160" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="handleApprove(row)">审核</el-button>
                <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
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

    <!-- 审核弹窗 -->
    <el-dialog v-model="approveDialogVisible" title="审核商品" width="900px" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="90px">
        <el-form-item label="商品名称">
          <span>{{ currentRow?.name }}</span>
        </el-form-item>
        <el-form-item label="SKU列表">
          <el-table :data="skuList" border stripe v-loading="skuLoading" row-key="id" style="width:100%">
            <el-table-column prop="httpMainPhoto" label="主图" width="90" align="center">
              <template #default="{ row }">
                <el-image v-if="row.httpMainPhoto" :src="row.httpMainPhoto" fit="cover" style="width:56px;height:56px" :preview-src-list="[row.httpMainPhoto]" preview-teleported />
              </template>
            </el-table-column>
            <el-table-column prop="name" label="SKU名称" min-width="240" show-overflow-tooltip />
            <el-table-column prop="price" label="价格" width="100" align="center" />
            <el-table-column prop="stockNum" label="库存" width="90" align="center" />
          </el-table>
        </el-form-item>
        <el-form-item v-if="showReject" label="驳回原因">
          <el-input v-model="approveTextValue" type="textarea" :rows="3" placeholder="请输入驳回原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveDialogVisible = false">关闭</el-button>
        <el-button type="danger" @click="handleReject">审核驳回</el-button>
        <el-button type="primary" @click="handlePass">审核通过</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight } from '@element-plus/icons-vue'
import {
  listShopProductApprove, queryCategoryTreeByPid, queryShopProductApproveSkuList,
  deleteShopProductApprove, rejectShopProductApprove, passShopProductApprove
} from '@/api/product/shopProductApprove'

const searchForm = reactive({ categoryId: null, id: '', uuid: '', name: '', shopId: '', approveStatus: '' })

function approveText(v) {
  if (v === 1 || v === '1') return '审核中'
  if (v === 2 || v === '2') return '审核通过'
  if (v === 3 || v === '3') return '审核驳回'
  return '-'
}
function approveTagType(v) {
  if (v === 2 || v === '2') return 'success'
  if (v === 3 || v === '3') return 'danger'
  return 'info'
}

// ===== 左侧分类树 =====
const categoryTreeRef = ref(null)
const treeData = ref([])

function resolveTreeNodes(children) {
  return (children || []).map(item => ({ ...item, leaf: !item.haveChild }))
}

async function loadTreeNodes(node, resolve) {
  const parentId = node && node.data ? node.data.id : -1
  try {
    const res = await queryCategoryTreeByPid({ parentId })
    resolve(resolveTreeNodes(res.data))
  } catch {
    resolve([])
  }
}

function onNodeClick(data) {
  searchForm.categoryId = data.id
  handleSearch()
}

// ===== 表格 =====
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listShopProductApprove(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.id = ''; searchForm.uuid = ''; searchForm.name = ''; searchForm.shopId = ''; searchForm.approveStatus = ''; searchForm.categoryId = null
  handleSearch()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该商品?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteShopProductApprove({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

// ===== 审核弹窗 =====
const approveDialogVisible = ref(false)
const skuLoading = ref(false)
const skuList = ref([])
const currentRow = ref(null)
const showReject = ref(false)
const approveTextValue = ref('')

async function handleApprove(row) {
  currentRow.value = row
  showReject.value = false
  approveTextValue.value = ''
  approveDialogVisible.value = true
  skuLoading.value = true
  skuList.value = []
  try {
    const res = await queryShopProductApproveSkuList({ productApproveId: row.id, page: 1, limit: 100 })
    skuList.value = res.data || []
  } catch { } finally { skuLoading.value = false }
}

async function handlePass() {
  if (!currentRow.value) return
  ElMessageBox.confirm('确定审核通过该商品吗？', '审核确认', {
    confirmButtonText: '确定通过', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await passShopProductApprove({ id: currentRow.value.id }); ElMessage.success('审核通过'); approveDialogVisible.value = false; loadTableData() } catch { }
  }).catch(() => {})
}

function handleReject() {
  showReject.value = true
  if (!approveTextValue.value) {
    ElMessage.warning('请输入驳回原因')
    return
  }
  const row = currentRow.value
  ElMessageBox.confirm('确定审核驳回该商品吗？', '驳回确认', {
    confirmButtonText: '确定驳回', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await rejectShopProductApprove({ approveId: row.id, shopId: row.shopId, approveText: approveTextValue.value })
      ElMessage.success('审核驳回成功'); approveDialogVisible.value = false; loadTableData()
    } catch { }
  }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.shop-product-approve {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
  }
  .right-table { flex: 1; overflow: auto; }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
