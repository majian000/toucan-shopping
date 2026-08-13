<template>
  <div class="product-sku">
    <h2 class="page-title">店铺SKU列表</h2>
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
            <el-button type="primary" :icon="Refresh" :disabled="selectedRows.length === 0" @click="handleFlushSearch">同步搜索缓存</el-button>
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
            <el-table-column label="操作" width="120" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="handleShelves(row)">
                  {{ row.status === 1 || row.status === '1' ? '下架' : '上架' }}
                </el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Refresh } from '@element-plus/icons-vue'
import { listProductSku, queryCategoryTreeByPid, shelves, flushSearch } from '@/api/product/productSku'

const searchForm = reactive({
  categoryId: null, id: '', uuid: '', shopId: '', status: '', shopProductId: '', shopProductUuid: '', name: ''
})

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

loadTableData()
</script>

<style lang="scss" scoped>
.product-sku {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
  }
  .right-table { flex: 1; overflow: auto; }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
