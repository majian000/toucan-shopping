<template>
  <div class="page-model-management">
    <h2 class="page-title">页面列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="店铺ID">
          <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="全部" clearable style="width:140px">
            <el-option label="预览页" value="1" />
            <el-option label="正式页" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置">
          <el-select v-model="searchForm.position" placeholder="全部" clearable style="width:140px">
            <el-option label="PC首页" value="1" />
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
        <el-table-column prop="id" label="ID" width="180" />
        <el-table-column prop="shopName" label="店铺名称" width="200" show-overflow-tooltip />
        <el-table-column prop="shopId" label="店铺ID" width="180" />
        <el-table-column prop="type" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 2 ? 'success' : 'info'" size="small">
              {{ row.type === 1 ? '预览页' : row.type === 2 ? '正式页' : row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="position" label="位置" width="100" align="center">
          <template #default="{ row }">{{ row.position === 1 ? 'PC首页' : row.position }}</template>
        </el-table-column>
        <el-table-column prop="designerVersion" label="设计器版本" width="130" />
        <el-table-column prop="enableStatus" label="启用状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'info'" size="small">
              {{ row.enableStatus === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pcIndexPage" label="PC首页正式页" min-width="240" show-overflow-tooltip />
        <el-table-column prop="createrName" label="创建人" width="120" />
        <el-table-column prop="updaterName" label="修改人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="140" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:seller:pageModel:delete'" @click="handleDelete(row)">删除</el-button>
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
    <el-dialog v-model="detailVisible" title="查看页面" width="650px" :close-on-click-modal="false" destroy-on-close>
      <el-descriptions v-if="detailInfo" :column="2" border label-width="110px">
        <el-descriptions-item label="店铺名称" :span="2">{{ detailInfo.shopName }}</el-descriptions-item>
        <el-descriptions-item label="店铺ID">{{ detailInfo.shopId }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="detailInfo.type === 2 ? 'success' : 'info'" size="small">
            {{ detailInfo.type === 1 ? '预览页' : detailInfo.type === 2 ? '正式页' : detailInfo.type }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="位置">{{ detailInfo.position === 1 ? 'PC首页' : detailInfo.position }}</el-descriptions-item>
        <el-descriptions-item label="设计器版本">{{ detailInfo.designerVersion }}</el-descriptions-item>
        <el-descriptions-item label="启用状态">
          <el-tag :type="detailInfo.enableStatus === 1 ? 'success' : 'info'" size="small">
            {{ detailInfo.enableStatus === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="PC首页正式页" :span="2">
          <a v-if="detailInfo.pcIndexPage" :href="detailInfo.pcIndexPage" target="_blank" rel="noopener">{{ detailInfo.pcIndexPage }}</a>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailInfo.createrName }}</el-descriptions-item>
        <el-descriptions-item label="修改人">{{ detailInfo.updaterName }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailInfo.createDate }}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ detailInfo.updateDate }}</el-descriptions-item>
      </el-descriptions>
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
import { listPageModel, deletePageModel } from '@/api/seller/pageModel'

const searchForm = reactive({ shopId: '', type: '', position: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.shopId = ''; searchForm.type = ''; searchForm.position = ''; handleSearch() }

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
    const res = await listPageModel(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

const detailVisible = ref(false)
const detailInfo = ref(null)
function handleView(row) {
  detailInfo.value = row
  detailVisible.value = true
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该页面吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deletePageModel({ id: row.id })
        if (res.code === 1) { ElMessage.success('删除成功'); loadTableData() } else { ElMessage.error(res.msg || '删除失败') }
      } catch { }
    }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.page-model-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
