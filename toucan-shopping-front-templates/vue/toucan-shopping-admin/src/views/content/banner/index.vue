<template>
  <div class="banner-management">
    <h2 class="page-title">轮播图列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="searchForm.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="searchForm.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-select v-model="searchForm.showStatus" placeholder="全部" clearable style="width:120px">
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
        <el-button type="primary" :icon="Plus" @click="handleAdd">添加轮播图</el-button>
        <el-button type="danger" :icon="Delete" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="warning" :icon="Refresh" :disabled="selectedRows.length === 0" @click="handleFlushCache">刷新首页缓存</el-button>
        <el-button :icon="Delete" @click="handleClearCache">清空首页缓存</el-button>
      </div>
      <el-table
        ref="tableRef" :data="tableData" border stripe v-loading="loading"
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="预览" width="80" align="center">
          <template #default="{ row }">
            <a v-if="row.httpImgPath" :href="row.httpImgPath" target="_blank">
              <img :src="row.httpImgPath" style="width:30px;height:30px" />
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" width="180" show-overflow-tooltip />
        <el-table-column prop="startShowDate" label="开始展示时间" width="170" />
        <el-table-column prop="endShowDate" label="结束展示时间" width="170" />
        <el-table-column prop="clickPath" label="点击路径" width="200" show-overflow-tooltip />
        <el-table-column prop="showStatus" label="显示状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.showStatus === '1' || row.showStatus === 1 ? 'success' : 'info'" size="small">
              {{ row.showStatus === '1' || row.showStatus === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="position" label="位置" width="120" align="center">
          <template #default="{ row }">{{ row.position === '0' ? '首页顶部' : row.position }}</template>
        </el-table-column>
        <el-table-column prop="bannerSort" label="排序" width="80" align="center" />
        <el-table-column prop="createAdminName" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminName" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="210" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" v-permission="'toucan:content:banner:detail'" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
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

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="750px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px" v-loading="dialogLoading">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="点击跳转" prop="clickPath">
          <el-input v-model="formData.clickPath" placeholder="请输入点击跳转地址" maxlength="255" />
        </el-form-item>
        <el-form-item label="预览图">
          <el-upload :auto-upload="false" :show-file-list="false" :on-change="onFileChange" :before-upload="beforeUpload" accept="image/*">
            <el-button type="primary" :icon="Upload">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="预览">
          <img v-if="previewImgUrl" :src="previewImgUrl" style="width:240px;height:220px;object-fit:contain;border:1px solid #dcdfe6;border-radius:4px" />
        </el-form-item>
        <el-form-item label="开始展示时间" prop="startShowDate">
          <el-date-picker v-model="formData.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束展示时间" prop="endShowDate">
          <el-date-picker v-model="formData.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="width:100%" />
        </el-form-item>
        <el-form-item label="显示状态" prop="showStatus">
          <el-select v-model="formData.showStatus" style="width:100%">
            <el-option label="显示" value="1" />
            <el-option label="隐藏" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置">
          <el-radio-group v-model="formData.position">
            <el-radio value="0">首页顶部</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关联城市" prop="areaCodeArray">
          <el-input v-model="areaNames" readonly placeholder="请选择关联城市" style="width:100%" @click="openAreaDialog" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.bannerSort" :min="0" style="width:100%" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="formData.remark" type="textarea" :rows="2" maxlength="255" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 选择关联城市弹窗 -->
    <el-dialog v-model="areaDialogVisible" title="选择关联城市" width="500px" :close-on-click-modal="false" append-to-body>
      <div v-loading="areaTreeLoading" class="area-tree-wrap" style="max-height: 400px; overflow-y: auto;">
        <el-tree
          ref="areaTreeRef"
          :data="areaTreeData"
          :props="{ label: 'text', children: 'children' }"
          node-key="code"
          show-checkbox
          check-strictly
        />
      </div>
      <template #footer>
        <el-button @click="areaDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAreaConfirm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="查看轮播图" width="650px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detailInfo" :column="2" border label-width="120px">
          <el-descriptions-item label="标题" :span="2">{{ detailInfo.title }}</el-descriptions-item>
          <el-descriptions-item label="点击路径" :span="2">{{ detailInfo.clickPath }}</el-descriptions-item>
          <el-descriptions-item label="预览图" :span="2">
            <img v-if="detailInfo.httpImgPath" :src="detailInfo.httpImgPath" style="width:180px;height:160px;object-fit:contain;border:1px solid #dcdfe6;border-radius:4px" />
          </el-descriptions-item>
          <el-descriptions-item label="显示状态">
            <el-tag :type="detailInfo.showStatus === 1 || detailInfo.showStatus === '1' ? 'success' : 'info'" size="small">
              {{ detailInfo.showStatus === 1 || detailInfo.showStatus === '1' ? '显示' : '隐藏' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="位置">{{ detailInfo.position === 0 || detailInfo.position === '0' ? '首页顶部' : detailInfo.position }}</el-descriptions-item>
          <el-descriptions-item label="排序" :span="2">{{ detailInfo.bannerSort }}</el-descriptions-item>
          <el-descriptions-item label="开始展示时间">{{ detailInfo.startShowDate }}</el-descriptions-item>
          <el-descriptions-item label="结束展示时间">{{ detailInfo.endShowDate }}</el-descriptions-item>
          <el-descriptions-item label="关联城市" :span="2">{{ detailInfo.areaNames }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detailInfo.remark }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ detailInfo.createAdminName }}</el-descriptions-item>
          <el-descriptions-item label="修改人">{{ detailInfo.updateAdminName }}</el-descriptions-item>
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
import { ref, reactive, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Edit, Search, RefreshRight, Upload, Refresh, View } from '@element-plus/icons-vue'
import { listBanner, saveBanner, updateBanner, deleteBanner, deleteBanners, queryBannerById, queryAreaTree, detailBanner, flushIndexCache, clearIndexCache } from '@/api/content/banner'

const searchForm = reactive({ title: '', startShowDate: '', endShowDate: '', showStatus: '' })

function handleSearch() { pagination.pageNum = 1; loadTableData() }
function handleReset() { searchForm.title = ''; searchForm.startShowDate = ''; searchForm.endShowDate = ''; searchForm.showStatus = '' }

const tableData = ref([]); const loading = ref(false); const selectedRows = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 15, total: 0 })
function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, pageNum: pagination.pageNum, pageSize: pagination.pageSize }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try { const res = await listBanner(buildSearchParams()); tableData.value = res.data || []; pagination.total = res.count || 0 } catch { } finally { loading.value = false }
}

const dialogVisible = ref(false); const dialogLoading = ref(false); const isEdit = ref(false)
const submitLoading = ref(false); const formRef = ref(null)
const previewImgUrl = ref('')
const dialogTitle = computed(() => isEdit.value ? '编辑轮播图' : '添加轮播图')
const areaDialogVisible = ref(false); const areaTreeLoading = ref(false); const areaTreeRef = ref(null)
const areaTreeData = ref([]); const areaNames = ref('')
const detailVisible = ref(false); const detailInfo = ref(null); const detailLoading = ref(false)

const formData = reactive({ id: null, title: '', clickPath: '', imgPath: '', imgBase64: '', startShowDate: '', endShowDate: '', showStatus: '1', position: '0', bannerSort: 0, areaCodeArray: [], remark: '' })
const formRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  clickPath: [{ required: true, message: '请输入点击跳转地址', trigger: 'blur' }],
  startShowDate: [{ required: true, message: '请选择开始展示时间', trigger: 'change' }],
  endShowDate: [{ required: true, message: '请选择结束展示时间', trigger: 'change' }],
  showStatus: [{ required: true, message: '请选择显示状态', trigger: 'change' }],
  areaCodeArray: [{ required: true, validator: (rule, value, cb) => { if (!value || value.length === 0) cb(new Error('请选择关联城市')); else cb() }, trigger: 'change' }]
}

function resetForm() {
  formData.id = null; formData.title = ''; formData.clickPath = ''; formData.imgPath = ''; formData.imgBase64 = ''
  formData.startShowDate = ''; formData.endShowDate = ''; formData.showStatus = '1'; formData.position = '0'; formData.bannerSort = 0
  formData.areaCodeArray = []; formData.remark = ''
  areaNames.value = ''
  previewImgUrl.value = ''
}

function onFileChange(file) {
  const reader = new FileReader()
  reader.onload = (e) => {
    previewImgUrl.value = e.target.result
    formData.imgBase64 = e.target.result
  }
  reader.readAsDataURL(file.raw)
}
function beforeUpload(file) { if (!file.type.startsWith('image/')) { ElMessage.error('只能上传图片文件'); return false }; return true }

function handleAdd() { isEdit.value = false; resetForm(); dialogVisible.value = true }
async function handleEdit(row) {
  isEdit.value = true
  formData.id = row.id; formData.title = row.title || ''; formData.clickPath = row.clickPath || ''
  formData.imgPath = row.imgPath || ''; formData.imgBase64 = ''; previewImgUrl.value = ''
  formData.startShowDate = row.startShowDate || ''; formData.endShowDate = row.endShowDate || ''
  formData.showStatus = row.showStatus != null ? String(row.showStatus) : '1'
  formData.position = row.position || '0'; formData.bannerSort = row.bannerSort || 0
  formData.remark = row.remark || ''; formData.areaCodeArray = []; areaNames.value = ''
  dialogVisible.value = true
  dialogLoading.value = true
  try {
    const res = await queryBannerById({ id: row.id })
    if (res.code === 1 && res.data) {
      formData.imgBase64 = res.data.imgBase64 || ''
      formData.imgPath = res.data.imgPath || ''
      previewImgUrl.value = res.data.imgBase64 || res.data.httpImgPath || ''
      formData.remark = res.data.remark || ''
      formData.areaCodeArray = (res.data.bannerAreas || []).map(a => a.areaCode)
    }
    if (!areaTreeData.value.length) await loadAreaTreeData()
    areaNames.value = computeAreaNames(formData.areaCodeArray)
  } catch { } finally { dialogLoading.value = false }
}

function computeAreaNames(codes) {
  const names = []
  const walk = (nodes) => {
    for (const n of nodes) {
      if (codes && codes.includes(n.code)) names.push(n.text)
      if (n.children && n.children.length) walk(n.children)
    }
  }
  walk(areaTreeData.value)
  return names.join(' ')
}

async function loadAreaTreeData() {
  try {
    const res = await queryAreaTree()
    if (res.code === 1 && res.data) {
      areaTreeData.value = res.data || []
    }
  } catch { }
}

async function openAreaDialog() {
  areaDialogVisible.value = true
  areaTreeLoading.value = true
  try {
    if (!areaTreeData.value.length) await loadAreaTreeData()
    await nextTick()
    await nextTick()
    if (areaTreeRef.value) areaTreeRef.value.setCheckedKeys(formData.areaCodeArray || [])
  } finally {
    areaTreeLoading.value = false
  }
}

function handleAreaConfirm() {
  if (!areaTreeRef.value) return
  const checkedKeys = areaTreeRef.value.getCheckedKeys()
  if (!checkedKeys.length) { ElMessage.warning('请选择地区'); return }
  formData.areaCodeArray = checkedKeys
  const names = areaTreeRef.value.getCheckedNodes().map(n => n.text).filter(Boolean)
  areaNames.value = names.join(' ')
  areaDialogVisible.value = false
}

async function handleView(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  try {
    const res = await detailBanner({ id: row.id })
    if (res.code === 1 && res.data) {
      detailInfo.value = res.data.basicInfo || res.data
    }
  } catch { } finally { detailLoading.value = false }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false); if (!valid) return
  submitLoading.value = true
  try { if (isEdit.value) { await updateBanner({ ...formData }) } else { await saveBanner({ ...formData }) }; ElMessage.success(isEdit.value ? '修改成功' : '添加成功'); dialogVisible.value = false; loadTableData() } catch { } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该轮播图吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteBanner({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { } }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个轮播图吗？`, '批量删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { await deleteBanners(selectedRows.value); ElMessage.success('批量删除成功'); loadTableData() } catch { } }).catch(() => {})
}

function handleFlushCache() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要操作的记录'); return }
  ElMessageBox.confirm('确定刷新PC首页缓存?', '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { const res = await flushIndexCache(selectedRows.value); ElMessage.success(res.msg || '刷新成功'); loadTableData() } catch { } }).catch(() => {})
}

function handleClearCache() {
  ElMessageBox.confirm('确定清空PC首页缓存?', '确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { try { const res = await clearIndexCache(); ElMessage.success(res.msg || '清空成功'); loadTableData() } catch { } }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.banner-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  .area-tree-wrap { max-height: 400px; overflow-y: auto; }
  :deep(.el-descriptions__table) { width: 100%; }
  :deep(.el-descriptions__label) { word-break: keep-all; }
  :deep(.el-descriptions__content) { word-break: break-all; overflow-wrap: anywhere; min-width: 0; }
}
</style>
