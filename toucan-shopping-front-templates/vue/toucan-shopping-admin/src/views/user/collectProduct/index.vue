<template>
  <div class="collect-product-management">
    <h2 class="page-title">收藏商品</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userMainId" placeholder="请输入用户ID" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="商品SKU ID">
          <el-input v-model="searchForm.productSkuId" placeholder="请输入商品SKU ID" clearable style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="danger" :icon="Delete" v-permission="'toucan:user:collectProduct:deletes'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="id" label="ID" width="200" show-overflow-tooltip />
        <el-table-column prop="userMainId" label="用户ID" width="180" show-overflow-tooltip />
        <el-table-column prop="productSkuId" label="商品SKU ID" width="180" show-overflow-tooltip />
        <el-table-column label="商品预览" width="90" align="center">
          <template #default="{ row }">
            <el-image v-if="row.httpProductImgPath" :src="row.httpProductImgPath" :preview-src-list="[row.httpProductImgPath]" preview-teleported fit="cover" style="width:40px;height:40px;cursor:zoom-in" />
          </template>
        </el-table-column>
        <el-table-column prop="productSkuName" label="商品名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="productPrice" label="商品价格" width="120" align="right">
          <template #default="{ row }">{{ row.productPrice != null ? `￥${row.productPrice}` : '-' }}</template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:user:collectProduct:delete'" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSearch" @current-change="handleSearch"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Delete } from '@element-plus/icons-vue'
import { listCollectProduct, deleteCollectProduct, deleteCollectProducts } from '@/api/user/collectProduct'

const searchForm = reactive({ userMainId: '', productSkuId: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.userMainId = ''; searchForm.productSkuId = '' }

const tableRef = ref(null)
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listCollectProduct(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该收藏商品吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try { await deleteCollectProduct(row.id); ElMessage.success('删除成功'); loadTableData() } catch { }
    }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 条收藏商品吗？`, '批量删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      const list = selectedRows.value.map(r => ({ id: r.id }))
      try { await deleteCollectProducts(list); ElMessage.success('批量删除成功'); loadTableData() } catch { }
    }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.collect-product-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
