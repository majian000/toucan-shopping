<template>
  <div class="attribute-key">
    <h2 class="page-title">属性管理</h2>
    <div class="layout-split">
      <div class="left-tree">
        <el-card shadow="never" class="tree-card">
          <template #header><span>商品分类</span></template>
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
            <el-form-item label="属性范围">
              <el-select v-model="searchForm.attributeType" placeholder="全部" clearable style="width:120px">
                <el-option label="全局属性" value="1" />
                <el-option label="销售属性" value="2" />
              </el-select>
            </el-form-item>
            <el-form-item label="搜索状态">
              <el-select v-model="searchForm.queryStatus" placeholder="全部" clearable style="width:120px">
                <el-option label="可被搜索" value="1" />
                <el-option label="不可搜索" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item label="属性名">
              <el-input v-model="searchForm.attributeName" placeholder="请输入属性名" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="显示状态">
              <el-select v-model="searchForm.showStatus" placeholder="全部" clearable style="width:110px">
                <el-option label="显示" value="1" />
                <el-option label="隐藏" value="0" />
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
            <el-button type="primary" :icon="Plus" v-permission="'toucan:product:attributeKey:save'" @click="handleAdd">添加</el-button>
            <el-button type="danger" :icon="Delete" v-permission="'toucan:product:attributeKey:deletes'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
          </div>
          <el-table :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="attributeName" label="属性名" min-width="180" show-overflow-tooltip />
            <el-table-column prop="categoryName" label="所属分类" width="140" show-overflow-tooltip />
            <el-table-column prop="categoryPath" label="所属分类路径" min-width="240" show-overflow-tooltip />
            <el-table-column prop="attributeType" label="属性类型" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="row.attributeType === 1 || row.attributeType === '1' ? '' : 'warning'" size="small">
                  {{ row.attributeType === 1 || row.attributeType === '1' ? '全局属性' : '销售属性' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="queryStatus" label="搜索状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="row.queryStatus === 1 || row.queryStatus === '1' ? 'success' : 'info'" size="small">
                  {{ row.queryStatus === 1 || row.queryStatus === '1' ? '可被搜索' : '不可搜索' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="attributeSort" label="排序" width="80" align="center" />
            <el-table-column prop="showStatus" label="显示状态" width="110" align="center">
              <template #default="{ row }">
                <el-tag :type="row.showStatus === 1 || row.showStatus === '1' ? 'success' : 'info'" size="small">
                  {{ row.showStatus === 1 || row.showStatus === '1' ? '显示' : '隐藏' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createAdminName" label="创建人" width="110" show-overflow-tooltip />
            <el-table-column prop="createDate" label="创建时间" width="170" />
            <el-table-column prop="updateAdminName" label="修改人" width="110" show-overflow-tooltip />
            <el-table-column prop="updateDate" label="修改时间" width="170" />
            <el-table-column label="操作" width="220" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="primary" link size="small" v-permission="'toucan:product:attributeKey:attributeValue:list'" @click="openValueList(row)">属性值管理</el-button>
                <el-button type="primary" link size="small" v-permission="'toucan:product:attributeKey:update'" @click="handleEdit(row)">编辑</el-button>
                <el-button type="danger" link size="small" v-permission="'toucan:product:attributeKey:delete'" @click="handleDelete(row)">删除</el-button>
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

    <!-- 属性名添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogMode === 'add' ? '添加属性名' : '编辑属性名'" width="540px" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="属性类型" required>
          <el-select v-model="form.attributeType" style="width:100%">
            <el-option label="全局属性" :value="1" />
            <el-option label="销售属性" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="搜索状态" required>
          <el-select v-model="form.queryStatus" style="width:100%">
            <el-option label="不可搜索" :value="0" />
            <el-option label="可被搜索" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="属性名" required>
          <el-input v-model="form.attributeName" placeholder="请输入属性名" maxlength="100" />
        </el-form-item>
        <el-form-item label="所属分类" required>
          <el-input v-model="form.categoryName" placeholder="请选择所属分类" readonly @click="categoryPickerVisible = true" />
        </el-form-item>
        <el-form-item label="上级节点">
          <el-input v-model="form.parentName" placeholder="请选择所属上级" readonly @click="openParentPicker" />
        </el-form-item>
        <el-form-item label="排序" required>
          <el-input-number v-model="form.attributeSort" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-radio-group v-model="form.showStatus">
            <el-radio :value="1">显示</el-radio>
            <el-radio :value="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="form.remark" type="textarea" :rows="3" maxlength="255" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确认保存</el-button>
      </template>
    </el-dialog>

    <!-- 分类选择弹窗 -->
    <el-dialog v-model="categoryPickerVisible" title="选择分类" width="360px" append-to-body>
      <el-tree
        ref="pickerTreeRef"
        :data="treeData"
        :props="{ children: 'children', label: 'name' }"
        node-key="id"
        lazy
        :load="loadTreeNodes"
        @node-click="onCategoryPicked"
      />
    </el-dialog>

    <!-- 上级节点选择弹窗 -->
    <el-dialog v-model="parentPickerVisible" title="选择上级节点" width="360px" append-to-body>
      <el-tree
        :data="parentTreeData"
        :props="{ children: 'children', label: 'title' }"
        node-key="id"
        default-expand-all
        @node-click="onParentPicked"
      />
    </el-dialog>

    <!-- 属性值管理弹窗 -->
    <el-dialog v-model="valueDialogVisible" title="属性值管理" width="1000px" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="valueSearchForm" :inline="true" class="value-search">
        <el-form-item label="搜索状态">
          <el-select v-model="valueSearchForm.queryStatus" placeholder="全部" clearable style="width:110px">
            <el-option label="可被搜索" value="1" />
            <el-option label="不可搜索" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="属性值">
          <el-input v-model="valueSearchForm.attributeValue" placeholder="请输入属性值" clearable style="width:140px" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-select v-model="valueSearchForm.showStatus" placeholder="全部" clearable style="width:100px">
            <el-option label="显示" value="1" />
            <el-option label="隐藏" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleValueSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleValueReset">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" v-permission="'toucan:product:attributeValue:save'" @click="handleValueAdd">添加</el-button>
        <el-button type="danger" :icon="Delete" v-permission="'toucan:product:attributeValue:deletes'" :disabled="selectedValues.length === 0" @click="handleValueBatchDelete">批量删除</el-button>
      </div>
      <el-table :data="valueTableData" border stripe v-loading="valueLoading" row-key="id" @selection-change="onValueSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="attributeValue" label="属性值" min-width="140" />
        <el-table-column prop="queryStatus" label="搜索状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="row.queryStatus === 1 || row.queryStatus === '1' ? 'success' : 'info'" size="small">
              {{ row.queryStatus === 1 || row.queryStatus === '1' ? '可被搜索' : '不可搜索' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="attributeValueExtend1" label="属性值扩展1" width="130" show-overflow-tooltip />
        <el-table-column prop="attributeValueExtend2" label="属性值扩展2" width="130" show-overflow-tooltip />
        <el-table-column prop="attributeValueExtend3" label="属性值扩展3" width="130" show-overflow-tooltip />
        <el-table-column prop="showStatus" label="显示状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.showStatus === 1 || row.showStatus === '1' ? 'success' : 'info'" size="small">
              {{ row.showStatus === 1 || row.showStatus === '1' ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="attributeSort" label="排序" width="80" align="center" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="130" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" v-permission="'toucan:product:attributeValue:update'" @click="handleValueEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" v-permission="'toucan:product:attributeValue:delete'" @click="handleValueDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="valuePagination.page" v-model:page-size="valuePagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="valuePagination.total" @size-change="handleValueSizeChange" @current-change="loadValueData"
        />
      </div>
      <template #footer>
        <el-button @click="valueDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 属性值添加/编辑弹窗 -->
    <el-dialog v-model="valueFormVisible" :title="valueFormMode === 'add' ? '添加属性值' : '编辑属性值'" width="520px" :close-on-click-modal="false" append-to-body>
      <el-form :model="valueForm" label-width="110px">
        <el-form-item label="搜索状态" required>
          <el-select v-model="valueForm.queryStatus" style="width:100%">
            <el-option label="可被搜索" :value="1" />
            <el-option label="不可搜索" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="属性值" required>
          <el-input v-model="valueForm.attributeValue" placeholder="请输入属性值" maxlength="100" />
        </el-form-item>
        <el-form-item label="属性值扩展1">
          <el-input v-model="valueForm.attributeValueExtend1" maxlength="100" />
        </el-form-item>
        <el-form-item label="属性值扩展2">
          <el-input v-model="valueForm.attributeValueExtend2" maxlength="100" />
        </el-form-item>
        <el-form-item label="属性值扩展3">
          <el-input v-model="valueForm.attributeValueExtend3" maxlength="100" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-radio-group v-model="valueForm.showStatus">
            <el-radio :value="1">显示</el-radio>
            <el-radio :value="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" required>
          <el-input-number v-model="valueForm.attributeSort" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="valueForm.remark" type="textarea" :rows="2" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="valueFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="valueSaving" @click="handleValueSave">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, Plus, Delete } from '@element-plus/icons-vue'
import {
  listAttributeKey, queryCategoryTreeByPid, queryTreeByCategoryId,
  saveAttributeKey, updateAttributeKey, deleteAttributeKey, deleteAttributeKeyByIds
} from '@/api/product/attributeKey'
import {
  listAttributeValue, saveAttributeValue, updateAttributeValue, deleteAttributeValue, deleteAttributeValueByIds
} from '@/api/product/attributeValue'

const searchForm = reactive({ categoryId: null, attributeType: '', queryStatus: '', attributeName: '', showStatus: '' })

// ===== 左侧分类树 =====
const categoryTreeRef = ref(null)
const pickerTreeRef = ref(null)
const treeData = ref([])

function resolveTreeNodes(children) {
  return (children || []).map(item => ({ ...item, leaf: !item.haveChild }))
}

async function loadTreeNodes(node, resolve) {
  const id = node && node.data ? node.data.id : -1
  try {
    const res = await queryCategoryTreeByPid({ id })
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
    const res = await listAttributeKey(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadTableData() }
function handleSizeChange() { pagination.page = 1; loadTableData() }
function handleReset() {
  searchForm.attributeType = ''; searchForm.queryStatus = ''; searchForm.attributeName = ''; searchForm.showStatus = ''; searchForm.categoryId = null
  handleSearch()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该属性名?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteAttributeKey(row.id); ElMessage.success('删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要操作的记录'); return }
  ElMessageBox.confirm('确定删除选中的属性名?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await deleteAttributeKeyByIds(selectedRows.value.map(r => ({ id: r.id })))
      ElMessage.success('删除成功')
      loadTableData()
    } catch { }
  }).catch(() => {})
}

// ===== 属性名添加/编辑 =====
const dialogVisible = ref(false)
const dialogMode = ref('add')
const saving = ref(false)
const form = reactive({ id: null, attributeType: 1, queryStatus: 0, attributeName: '', categoryId: null, categoryName: '', parentId: -1, parentName: '', attributeSort: 0, showStatus: 1, remark: '' })

const categoryPickerVisible = ref(false)

function onCategoryPicked(data) {
  form.categoryId = data.id
  form.categoryName = data.name
  categoryPickerVisible.value = false
}

const parentPickerVisible = ref(false)
const parentTreeData = ref([])

async function openParentPicker() {
  if (form.categoryId == null || form.categoryId === -1) {
    ElMessage.warning('请先选择分类')
    return
  }
  parentPickerVisible.value = true
  parentTreeData.value = []
  try {
    const res = await queryTreeByCategoryId({ categoryId: form.categoryId, attributeType: form.attributeType })
    parentTreeData.value = res.data || []
  } catch { }
}

function onParentPicked(data) {
  form.parentId = data.id
  form.parentName = data.title || data.attributeName || ''
  parentPickerVisible.value = false
}

function handleAdd() {
  dialogMode.value = 'add'
  Object.assign(form, {
    id: null, attributeType: 1, queryStatus: 0, attributeName: '', categoryId: searchForm.categoryId,
    categoryName: '', parentId: -1, parentName: '', attributeSort: 0, showStatus: 1, remark: ''
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogMode.value = 'edit'
  Object.assign(form, {
    id: row.id, attributeType: row.attributeType, queryStatus: row.queryStatus, attributeName: row.attributeName,
    categoryId: row.categoryId, categoryName: row.categoryName || '', parentId: row.parentId == null ? -1 : row.parentId,
    parentName: row.parentName || '', attributeSort: row.attributeSort, showStatus: row.showStatus, remark: row.remark || ''
  })
  dialogVisible.value = true
}

async function handleSave() {
  if (!form.attributeName) { ElMessage.warning('请输入属性名'); return }
  if (form.categoryId == null || form.categoryId === -1) { ElMessage.warning('请选择所属分类'); return }
  const payload = {
    attributeType: form.attributeType, queryStatus: form.queryStatus, attributeName: form.attributeName,
    categoryId: form.categoryId, parentId: form.parentId, attributeSort: form.attributeSort,
    showStatus: form.showStatus, remark: form.remark
  }
  if (dialogMode.value === 'edit') payload.id = form.id
  saving.value = true
  try {
    if (dialogMode.value === 'add') await saveAttributeKey(payload)
    else await updateAttributeKey(payload)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadTableData()
  } catch { } finally { saving.value = false }
}

// ===== 属性值管理 =====
const valueDialogVisible = ref(false)
const valueLoading = ref(false)
const valueTableData = ref([])
const selectedValues = ref([])
const valuePagination = reactive({ page: 1, limit: 15, total: 0 })
const valueSearchForm = reactive({ attributeKeyId: null, queryStatus: '', attributeValue: '', showStatus: '' })

function onValueSelectionChange(rows) { selectedValues.value = rows }

function buildValueParams() {
  const p = { ...valueSearchForm, page: valuePagination.page, limit: valuePagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function loadValueData() {
  valueLoading.value = true
  try {
    const res = await listAttributeValue(buildValueParams())
    valueTableData.value = res.data || []
    valuePagination.total = res.count || 0
  } catch { } finally { valueLoading.value = false }
}

function handleValueSearch() { valuePagination.page = 1; loadValueData() }
function handleValueSizeChange() { valuePagination.page = 1; loadValueData() }
function handleValueReset() {
  valueSearchForm.queryStatus = ''; valueSearchForm.attributeValue = ''; valueSearchForm.showStatus = ''
  handleValueSearch()
}

function openValueList(row) {
  valueSearchForm.attributeKeyId = row.id
  valueDialogVisible.value = true
  handleValueSearch()
}

function handleValueDelete(row) {
  ElMessageBox.confirm('确定删除该属性值?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteAttributeValue(row.id); ElMessage.success('删除成功'); loadValueData() } catch { }
  }).catch(() => {})
}

function handleValueBatchDelete() {
  if (selectedValues.value.length === 0) { ElMessage.warning('请选择要操作的记录'); return }
  ElMessageBox.confirm('确定删除选中的属性值?', '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try {
      await deleteAttributeValueByIds(selectedValues.value.map(r => ({ id: r.id })))
      ElMessage.success('删除成功')
      loadValueData()
    } catch { }
  }).catch(() => {})
}

const valueFormVisible = ref(false)
const valueFormMode = ref('add')
const valueSaving = ref(false)
const valueForm = reactive({ id: null, queryStatus: 1, attributeValue: '', attributeValueExtend1: '', attributeValueExtend2: '', attributeValueExtend3: '', showStatus: 1, attributeSort: 0, remark: '' })

function handleValueAdd() {
  valueFormMode.value = 'add'
  Object.assign(valueForm, {
    id: null, queryStatus: 1, attributeValue: '', attributeValueExtend1: '', attributeValueExtend2: '', attributeValueExtend3: '', showStatus: 1, attributeSort: 0, remark: ''
  })
  valueFormVisible.value = true
}

function handleValueEdit(row) {
  valueFormMode.value = 'edit'
  Object.assign(valueForm, {
    id: row.id, queryStatus: row.queryStatus, attributeValue: row.attributeValue,
    attributeValueExtend1: row.attributeValueExtend1 || '', attributeValueExtend2: row.attributeValueExtend2 || '',
    attributeValueExtend3: row.attributeValueExtend3 || '', showStatus: row.showStatus, attributeSort: row.attributeSort, remark: row.remark || ''
  })
  valueFormVisible.value = true
}

async function handleValueSave() {
  if (!valueForm.attributeValue) { ElMessage.warning('请输入属性值'); return }
  const payload = {
    attributeKeyId: valueSearchForm.attributeKeyId, queryStatus: valueForm.queryStatus, attributeValue: valueForm.attributeValue,
    attributeValueExtend1: valueForm.attributeValueExtend1, attributeValueExtend2: valueForm.attributeValueExtend2,
    attributeValueExtend3: valueForm.attributeValueExtend3, showStatus: valueForm.showStatus,
    attributeSort: valueForm.attributeSort, remark: valueForm.remark
  }
  if (valueFormMode.value === 'edit') payload.id = valueForm.id
  valueSaving.value = true
  try {
    if (valueFormMode.value === 'add') await saveAttributeValue(payload)
    else await updateAttributeValue(payload)
    ElMessage.success('保存成功')
    valueFormVisible.value = false
    loadValueData()
  } catch { } finally { valueSaving.value = false }
}

loadTableData()
</script>

<style lang="scss" scoped>
.attribute-key {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
  }
  .right-table { flex: 1; overflow: auto; }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  .value-search { margin-bottom: $gap-md; }
  .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
  .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
