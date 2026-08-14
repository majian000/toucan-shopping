<template>
  <div class="article-image-management">
    <h2 class="page-title">文章图片列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="文章ID">
          <el-input v-model="searchForm.articleId" placeholder="请输入文章ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="文件删除状态">
          <el-select v-model="searchForm.fileDeleteStatus" placeholder="全部" clearable style="width:140px">
            <el-option label="未删除" value="0" />
            <el-option label="已删除" value="1" />
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
        <el-button type="danger" :icon="Delete" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table
        ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id"
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="预览" width="80" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.httpImgPath"
              :src="row.httpImgPath"
              :preview-src-list="[row.httpImgPath]"
              preview-teleported
              fit="cover"
              style="width:30px;height:30px;cursor:zoom-in"
            />
          </template>
        </el-table-column>
        <el-table-column prop="id" label="ID" width="230" align="center" />
        <el-table-column prop="articleId" label="文章ID" width="230" align="center" />
        <el-table-column prop="fileName" label="文件名" min-width="180" show-overflow-tooltip />
        <el-table-column prop="fileExt" label="格式" width="90" align="center" />
        <el-table-column prop="fileSize" label="大小(MB)" width="120" align="center" :formatter="formatFileSize" />
        <el-table-column prop="createAdminName" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminName" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize"
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
import { Delete, Search, RefreshRight } from '@element-plus/icons-vue'
import { listArticleImage, deleteArticleImage, deleteArticleImages } from '@/api/content/articleImage'

const searchForm = reactive({ articleId: '', fileDeleteStatus: '' })

function handleSearch() { pagination.pageNum = 1; loadTableData() }
function handleReset() { searchForm.articleId = ''; searchForm.fileDeleteStatus = ''; handleSearch() }

const tableRef = ref(null)
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 15, total: 0 })

function onSelectionChange(rows) { selectedRows.value = rows }

function formatFileSize(row, column, cellValue) {
  if (cellValue == null || cellValue === '') return '-'
  return (Number(cellValue) / 1024 / 1024).toFixed(2)
}

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.pageNum, limit: pagination.pageSize }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listArticleImage(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该文章图片吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteArticleImage({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { } })
    .catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个文章图片吗？`, '批量删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteArticleImages(selectedRows.value); ElMessage.success('批量删除成功'); loadTableData() } catch { } })
    .catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.article-image-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
