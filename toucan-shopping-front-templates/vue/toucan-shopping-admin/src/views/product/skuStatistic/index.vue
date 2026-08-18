<template>
  <div class="sku-statistic">
    <h2 class="page-title">商品SKU统计</h2>
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
              <el-input v-model="searchForm.productSkuId" placeholder="请输入SKU ID" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="店铺ID">
              <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="上架状态">
              <el-select v-model="searchForm.status" placeholder="全部" clearable style="width:120px">
                <el-option label="未上架" value="0" />
                <el-option label="已上架" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
              <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="table-card">
          <el-table :data="tableData" border stripe v-loading="loading" row-key="categoryId">
            <el-table-column type="index" label="序号" width="80" align="center" />
            <el-table-column prop="categoryName" label="分类" min-width="160" />
            <el-table-column prop="count" label="商品数量" min-width="150" align="center" sortable />
          </el-table>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, RefreshRight, Refresh } from '@element-plus/icons-vue'
import { queryProductSkuStatistic, queryCategoryTreeByPid } from '@/api/product/skuStatistic'

const searchForm = reactive({ productSkuId: '', shopId: '', status: '', categoryId: null })

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
  const pid = node && node.data ? node.data.id : -1
  treeLoading.value = true
  try {
    const res = await queryCategoryTreeByPid({ parentId: pid })
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

// ===== 统计表格 =====
const tableData = ref([])
const loading = ref(false)

function buildSearchParams() {
  const p = { ...searchForm }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function handleSearch() {
  if (searchForm.categoryId == null || searchForm.categoryId === '' || searchForm.categoryId === -1) {
    ElMessage.warning('请先在左侧选择商品分类')
    return
  }
  loading.value = true
  try {
    const res = await queryProductSkuStatistic(buildSearchParams())
    tableData.value = res.data || []
  } catch { } finally { loading.value = false }
}

function handleReset() {
  searchForm.productSkuId = ''; searchForm.shopId = ''; searchForm.status = ''
  if (searchForm.categoryId != null) handleSearch()
}
</script>

<style lang="scss" scoped>
.sku-statistic {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
  }
  .right-table { flex: 1; overflow: auto; }
  .tree-header { display: flex; justify-content: space-between; align-items: center; }
  .tree-refresh { cursor: pointer; color: $text-secondary; &:hover { color: $primary; } }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
