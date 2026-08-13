<template>
  <div class="stock-lock-management">
    <h2 class="page-title">商品库存锁定</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="SKU ID">
          <el-input v-model="searchForm.productSkuId" placeholder="请输入SKU ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userMainId" placeholder="请输入用户ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="主订单编号">
          <el-input v-model="searchForm.mainOrderNo" placeholder="请输入主订单编号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="子订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入子订单编号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="锁定类型">
          <el-select v-model="searchForm.type" placeholder="全部" clearable style="width:160px">
            <el-option label="买家拍下减库存" value="1" />
            <el-option label="买家付款减库存" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="还原状态">
          <el-select v-model="searchForm.restoreStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="未还原" value="0" />
            <el-option label="已还原" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id">
        <el-table-column prop="id" label="ID" width="90" align="center" />
        <el-table-column prop="productSkuId" label="SKU ID" width="120" />
        <el-table-column prop="userMainId" label="用户ID" width="120" />
        <el-table-column prop="mainOrderNo" label="主订单编号" width="180" show-overflow-tooltip />
        <el-table-column prop="orderNo" label="子订单编号" width="180" show-overflow-tooltip />
        <el-table-column prop="stockNum" label="锁定数量" width="90" align="center" />
        <el-table-column prop="type" label="锁定类型" width="130" align="center">
          <template #default="{ row }">{{ row.type === 1 || row.type === '1' ? '买家拍下减库存' : row.type === 2 || row.type === '2' ? '买家付款减库存' : '-' }}</template>
        </el-table-column>
        <el-table-column prop="payStatus" label="支付状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.payStatus === 1 || row.payStatus === '1' ? 'success' : 'info'" size="small">
              {{ row.payStatus === 1 || row.payStatus === '1' ? '已支付' : '未支付' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="restoreStatus" label="还原状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.restoreStatus === 1 || row.restoreStatus === '1' ? 'success' : 'warning'" size="small">
              {{ row.restoreStatus === 1 || row.restoreStatus === '1' ? '已还原' : '未还原' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderCreateDate" label="订单创建时间" width="170" />
        <el-table-column prop="remark" label="备注" width="160" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="90" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleView(row)">查看</el-button>
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

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="库存锁定详情" width="650px" :close-on-click-modal="false" destroy-on-close>
      <el-descriptions v-if="detailInfo" :column="2" border label-width="120px">
        <el-descriptions-item label="ID">{{ detailInfo.id }}</el-descriptions-item>
        <el-descriptions-item label="SKU ID">{{ detailInfo.productSkuId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailInfo.userMainId }}</el-descriptions-item>
        <el-descriptions-item label="锁定数量">{{ detailInfo.stockNum }}</el-descriptions-item>
        <el-descriptions-item label="主订单编号" :span="2">{{ detailInfo.mainOrderNo }}</el-descriptions-item>
        <el-descriptions-item label="子订单编号" :span="2">{{ detailInfo.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="锁定类型">
          {{ detailInfo.type === 1 || detailInfo.type === '1' ? '买家拍下减库存' : detailInfo.type === 2 || detailInfo.type === '2' ? '买家付款减库存' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          {{ detailInfo.payStatus === 1 || detailInfo.payStatus === '1' ? '已支付' : '未支付' }}
        </el-descriptions-item>
        <el-descriptions-item label="还原状态">
          {{ detailInfo.restoreStatus === 1 || detailInfo.restoreStatus === '1' ? '已还原' : '未还原' }}
        </el-descriptions-item>
        <el-descriptions-item label="删除状态">
          {{ detailInfo.deleteStatus === 1 || detailInfo.deleteStatus === '1' ? '已删除' : '未删除' }}
        </el-descriptions-item>
        <el-descriptions-item label="订单创建时间">{{ detailInfo.orderCreateDate }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailInfo.createDate }}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ detailInfo.updateDate }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailInfo.remark }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Search, RefreshRight, View } from '@element-plus/icons-vue'
import { listProductSkuStockLock } from '@/api/stock/productSkuStockLock'

const searchForm = reactive({
  productSkuId: '', userMainId: '', mainOrderNo: '', orderNo: '', type: '', restoreStatus: ''
})

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.productSkuId = ''; searchForm.userMainId = ''; searchForm.mainOrderNo = ''
  searchForm.orderNo = ''; searchForm.type = ''; searchForm.restoreStatus = ''
  handleSearch()
}

const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listProductSkuStockLock(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSizeChange() { pagination.page = 1; loadTableData() }

const detailVisible = ref(false)
const detailInfo = ref(null)
function handleView(row) { detailInfo.value = row; detailVisible.value = true }

loadTableData()
</script>

<style lang="scss" scoped>
.stock-lock-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  :deep(.el-descriptions__table) { width: 100%; }
  :deep(.el-descriptions__label) { word-break: keep-all; }
  :deep(.el-descriptions__content) { word-break: break-all; overflow-wrap: anywhere; min-width: 0; }
}
</style>
