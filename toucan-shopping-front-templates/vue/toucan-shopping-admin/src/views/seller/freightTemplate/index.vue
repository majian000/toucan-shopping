<template>
  <div class="freight-template-management">
    <h2 class="page-title">运费模板列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="模板名称">
          <el-input v-model="searchForm.name" placeholder="请输入模板名称" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="店铺ID">
          <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="运费状态">
          <el-select v-model="searchForm.freightStatus" placeholder="全部" clearable style="width:140px">
            <el-option label="自定义运费" value="1" />
            <el-option label="包邮" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="计价方式">
          <el-select v-model="searchForm.valuationMethod" placeholder="全部" clearable style="width:140px">
            <el-option label="按件数" value="1" />
            <el-option label="按重量" value="2" />
            <el-option label="按体积" value="3" />
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
        <el-table-column prop="id" label="ID" width="230" />
        <el-table-column prop="name" label="模板名称" width="200" show-overflow-tooltip />
        <el-table-column prop="shopId" label="店铺ID" width="230" />
        <el-table-column prop="freightStatus" label="运费状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="row.freightStatus === 1 ? 'primary' : 'success'" size="small">
              {{ row.freightStatus === 1 ? '自定义运费' : row.freightStatus === 2 ? '包邮' : row.freightStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="valuationMethod" label="计价方式" width="110" align="center">
          <template #default="{ row }">{{ formatValuationMethod(row.valuationMethod) }}</template>
        </el-table-column>
        <el-table-column label="发货地" width="220" show-overflow-tooltip>
          <template #default="{ row }">{{ [row.deliverProvinceName, row.deliverCityName, row.deliverAreaName].filter(Boolean).join(' ') }}</template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="140" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" v-permission="'toucan:seller:freightTemplate:detail'" @click="handleView(row)">查看</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:seller:freightTemplate:delete'" @click="handleDelete(row)">删除</el-button>
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

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="运费模板详情" width="650px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detailInfo" :column="2" border label-width="110px">
          <el-descriptions-item label="模板名称" :span="2">{{ detailInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="店铺ID">{{ detailInfo.shopId }}</el-descriptions-item>
          <el-descriptions-item label="运费状态">
            <el-tag :type="detailInfo.freightStatus === 1 ? 'primary' : 'success'" size="small">
              {{ detailInfo.freightStatus === 1 ? '自定义运费' : detailInfo.freightStatus === 2 ? '包邮' : detailInfo.freightStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="计价方式">
            <el-tag type="warning" size="small">{{ formatValuationMethod(detailInfo.valuationMethod) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="运送方式">
            <template v-if="detailInfo.transportModel">
              <el-tag v-for="(item, idx) in transportModelList(detailInfo.transportModel)" :key="idx" type="success" size="small" style="margin-right:4px">{{ item }}</el-tag>
            </template>
          </el-descriptions-item>
          <el-descriptions-item label="发货地" :span="2">{{ [detailInfo.deliverProvinceName, detailInfo.deliverCityName, detailInfo.deliverAreaName].filter(Boolean).join(' ') }}</el-descriptions-item>
          <el-descriptions-item label="模板排序">{{ detailInfo.templateSort }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detailInfo.remark }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detailInfo.createDate }}</el-descriptions-item>
          <el-descriptions-item label="修改时间">{{ detailInfo.updateDate }}</el-descriptions-item>
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
import { Search, RefreshRight, View, Delete } from '@element-plus/icons-vue'
import { listFreightTemplate, detailFreightTemplate, deleteFreightTemplate } from '@/api/seller/freightTemplate'

const searchForm = reactive({ name: '', shopId: '', freightStatus: '', valuationMethod: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.name = ''; searchForm.shopId = ''; searchForm.freightStatus = ''; searchForm.valuationMethod = ''; handleSearch() }

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
    const res = await listFreightTemplate(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function formatValuationMethod(v) {
  if (v === 1) return '按件数'
  if (v === 2) return '按重量'
  if (v === 3) return '按体积'
  return v
}
function transportModelList(v) {
  if (!v) return []
  const map = { 1: '快递', 2: 'EMS', 3: '平邮' }
  return String(v).split(',').map(i => map[i] || i).filter(Boolean)
}

const detailVisible = ref(false)
const detailLoading = ref(false)
const detailInfo = ref(null)

async function handleView(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  try {
    const res = await detailFreightTemplate({ id: row.id })
    if (res.code === 1 && res.data) {
      detailInfo.value = res.data
    }
  } catch { } finally { detailLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该运费模板吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteFreightTemplate({ id: row.id })
        if (res.code === 1) { ElMessage.success('删除成功'); loadTableData() } else { ElMessage.error(res.msg || '删除失败'); loadTableData() }
      } catch { }
    }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.freight-template-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
