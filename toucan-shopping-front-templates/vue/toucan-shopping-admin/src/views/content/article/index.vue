<template>
  <div class="article-management">
    <h2 class="page-title">文章管理</h2>
    <div class="layout-split">
      <!-- 左侧栏目树 -->
      <div class="left-tree">
        <el-card shadow="never" class="tree-card">
          <template #header>
            <div class="tree-header">
              <span>所属栏目</span>
              <el-icon class="tree-refresh" @click="refreshColumnTree"><RefreshRight /></el-icon>
            </div>
          </template>
          <div class="tree-body" v-loading="columnTreeLoading">
            <el-tree
              ref="columnTreeRef"
              :key="treeKey"
              :data="columnTreeData"
              :props="{ children: 'children', label: 'name' }"
              node-key="id"
              highlight-current
              :expand-on-click-node="false"
              :load="loadColumnTreeNode"
              lazy
              @node-click="onColumnNodeClick"
            >
              <template #default="{ data }">
                <span>{{ data.name }}</span>
              </template>
            </el-tree>
          </div>
        </el-card>
      </div>
      <!-- 右侧表格区 -->
      <div class="right-table">
        <el-card shadow="never" class="search-card">
          <el-form :model="searchForm" :inline="true">
            <el-form-item label="标题">
              <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="作者">
              <el-input v-model="searchForm.author" placeholder="请输入作者" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="SEO标题">
              <el-input v-model="searchForm.seoTitle" placeholder="请输入SEO标题" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="SEO关键字">
              <el-input v-model="searchForm.seoKeywords" placeholder="请输入SEO关键字" clearable style="width:160px" />
            </el-form-item>
            <el-form-item label="开始时间">
              <el-date-picker v-model="searchForm.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
            </el-form-item>
            <el-form-item label="结束时间">
              <el-date-picker v-model="searchForm.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="searchForm.perpetualStatus" :true-value="1" :false-value="0">永远</el-checkbox>
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
            <el-button type="primary" :icon="Plus" v-permission="'toucan:content:article:add'" @click="handleAdd">添加文章</el-button>
            <el-button type="danger" :icon="Delete" v-permission="'toucan:content:article:deletes'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
          </div>
          <el-table
            ref="tableRef"
            :data="tableData"
            border stripe v-loading="loading"
            @selection-change="onSelectionChange"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="封面预览" width="100" align="center">
              <template #default="{ row }">
                <a v-if="row.httpCoverImgUrl" :href="row.httpCoverImgUrl" target="_blank">
                  <img :src="row.httpCoverImgUrl" style="width:30px;height:30px" />
                </a>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
            <el-table-column prop="startShowDate" label="开始展示时间" width="170" />
            <el-table-column prop="endShowDate" label="结束展示时间" width="170">
              <template #default="{ row }">
                {{ row.perpetualStatus === '1' || row.perpetualStatus === 1 ? '永远' : row.endShowDate }}
              </template>
            </el-table-column>
            <el-table-column prop="author" label="作者" width="100" />
            <el-table-column prop="publishDate" label="发布时间" width="120" />
            <el-table-column prop="abstractContent" label="摘要内容" width="150" show-overflow-tooltip />
            <el-table-column prop="seoTitle" label="SEO标题" width="120" show-overflow-tooltip />
            <el-table-column prop="seoKeywords" label="SEO关键字" width="120" show-overflow-tooltip />
            <el-table-column prop="seoDescription" label="SEO简介" width="150" show-overflow-tooltip />
            <el-table-column prop="showStatus" label="显示状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="row.showStatus === '1' || row.showStatus === 1 ? 'success' : 'info'" size="small">
                  {{ row.showStatus === '1' || row.showStatus === 1 ? '显示' : '隐藏' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="articleSort" label="排序" width="80" align="center" />
            <el-table-column prop="createAdminName" label="创建人" width="120" />
            <el-table-column prop="createDate" label="创建时间" width="170" />
            <el-table-column prop="updateAdminName" label="修改人" width="120" />
            <el-table-column prop="updateDate" label="修改时间" width="170" />
            <el-table-column label="操作" width="200" fixed="right" align="center">
              <template #default="{ row }">
                <el-button type="info" link size="small" :icon="View" v-permission="'toucan:content:article:detail'" @click="handleView(row)">查看</el-button>
                <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:content:article:update'" @click="handleEdit(row)">编辑</el-button>
                <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:content:article:delete'" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-wrap">
            <el-pagination
              v-model:current-page="pagination.pageNum"
              v-model:page-size="pagination.pageSize"
              :page-sizes="[15, 30, 100, 200]"
              layout="total, sizes, prev, pager, next"
              :total="pagination.total"
              @size-change="handleSearch"
              @current-change="handleSearch"
            />
          </div>
        </el-card>
      </div>
    </div>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="900px" :close-on-click-modal="false" destroy-on-close top="5vh">
      <div class="dialog-scroll" v-loading="dialogLoading">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-form-item label="所属栏目">
          <el-input :model-value="currentColumnName" disabled />
        </el-form-item>
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入文章标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload :auto-upload="false" :show-file-list="false" :on-change="onFileChange" :before-upload="beforeUpload" accept="image/*">
            <el-button type="primary" :icon="Upload">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="预览封面">
          <img v-if="previewCoverUrl" :src="previewCoverUrl" style="width:240px;height:220px;object-fit:contain;border:1px solid #dcdfe6;border-radius:4px" />
        </el-form-item>
        <el-form-item label="开始展示时间" prop="startShowDate">
          <el-date-picker v-model="formData.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束展示时间">
          <div style="display:flex;align-items:center;gap:12px">
            <el-date-picker v-model="formData.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="flex:1" :disabled="formData.perpetualStatus === 1" />
            <el-checkbox v-model="formData.perpetualStatus" :true-value="1" :false-value="0" @change="onPerpetualChange">永远</el-checkbox>
          </div>
        </el-form-item>
        <el-form-item label="显示状态" prop="showStatus">
          <el-select v-model="formData.showStatus" style="width:100%">
            <el-option label="显示" value="1" />
            <el-option label="隐藏" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.articleSort" :min="0" style="width:100%" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="摘要内容">
          <el-input v-model="formData.abstractContent" type="textarea" :rows="2" maxlength="200" placeholder="请输入摘要内容" />
        </el-form-item>
        <el-form-item label="SEO标题">
          <el-input v-model="formData.seoTitle" maxlength="255" placeholder="请输入SEO标题" />
        </el-form-item>
        <el-form-item label="SEO关键字">
          <el-input v-model="formData.seoKeywords" type="textarea" :rows="2" maxlength="255" placeholder="请输入SEO关键字" />
        </el-form-item>
        <el-form-item label="SEO简介">
          <el-input v-model="formData.seoDescription" type="textarea" :rows="2" maxlength="255" placeholder="请输入SEO简介" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="formData.author" maxlength="80" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="发布时间" prop="publishDate">
          <el-input v-model="formData.publishDate" maxlength="80" placeholder="请输入发布时间" />
        </el-form-item>
        <el-form-item label="文章内容">
          <Editor v-model="formData.content" :height="400" placeholder="请输入文章内容..." />
        </el-form-item>
      </el-form>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="文章详情" width="900px" :close-on-click-modal="false" top="5vh">
      <div class="dialog-scroll" v-loading="detailLoading">
        <template v-if="detail">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="文章标题" :span="2">{{ detail.title || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属栏目">{{ detail.columnName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="作者">{{ detail.author || '-' }}</el-descriptions-item>
            <el-descriptions-item label="发布时间">{{ detail.publishDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="显示状态">
              <el-tag :type="detail.showStatus === '1' || detail.showStatus === 1 ? 'success' : 'info'" size="small">
                {{ detail.showStatus === '1' || detail.showStatus === 1 ? '显示' : '隐藏' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="排序">{{ detail.articleSort ?? '-' }}</el-descriptions-item>
            <el-descriptions-item label="开始展示时间">{{ detail.startShowDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="结束展示时间">
              {{ detail.perpetualStatus === '1' || detail.perpetualStatus === 1 ? '永远' : (detail.endShowDate || '-') }}
            </el-descriptions-item>
            <el-descriptions-item label="摘要内容" :span="2">{{ detail.abstractContent || '-' }}</el-descriptions-item>
            <el-descriptions-item label="SEO标题">{{ detail.seoTitle || '-' }}</el-descriptions-item>
            <el-descriptions-item label="SEO关键字">{{ detail.seoKeywords || '-' }}</el-descriptions-item>
            <el-descriptions-item label="SEO简介" :span="2">{{ detail.seoDescription || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建人">{{ detail.createAdminName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ detail.createDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="修改人">{{ detail.updateAdminName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="修改时间">{{ detail.updateDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="封面图片" :span="2">
              <el-image v-if="detail.httpCoverImgUrl" :src="detail.httpCoverImgUrl" fit="contain" style="width:240px;height:220px" :preview-src-list="[detail.httpCoverImgUrl]" preview-teleported />
              <span v-else>-</span>
            </el-descriptions-item>
          </el-descriptions>
          <div class="detail-section">
            <div class="detail-section__label">文章内容</div>
            <div class="detail-content" v-html="detail.content"></div>
          </div>
        </template>
        <el-empty v-else-if="!detailLoading" description="暂无数据" />
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
import { Plus, Delete, Edit, Search, RefreshRight, Upload, View } from '@element-plus/icons-vue'
import { listArticle, queryColumnTreeByPid, saveArticle, updateArticle, deleteArticle, deleteArticles, queryArticleById, queryArticleDetail } from '@/api/content/article'
import Editor from '@/components/Editor/index.vue'

// ========== 左侧栏目树 ==========
const columnTreeRef = ref(null)
const columnTreeData = ref([])
const selectedColumnId = ref(-1)
const treeKey = ref(0)
const columnTreeLoading = ref(false)

async function loadColumnTreeNode(node, resolve) {
  const isRoot = !node || node.level === 0
  if (isRoot) columnTreeLoading.value = true
  try {
    const pid = node?.data?.id != null ? node.data.id : -1
    const res = await queryColumnTreeByPid(pid)
    const nodes = (res?.data || []).map(item => ({ ...item, leaf: false }))
    resolve(nodes)
  } catch { resolve([]) } finally {
    if (isRoot) columnTreeLoading.value = false
  }
}

function onColumnNodeClick(data) {
  selectedColumnId.value = data.id
  searchForm.columnId = data.id
  handleSearch()
}

function refreshColumnTree() {
  treeKey.value++
  selectedColumnId.value = -1
  searchForm.columnId = -1
  handleSearch()
}

// ========== 搜索 ==========
const searchForm = reactive({
  columnId: -1, title: '', author: '', seoTitle: '', seoKeywords: '',
  startShowDate: '', endShowDate: '', perpetualStatus: 1, showStatus: ''
})

function buildSearchParams() {
  const params = { ...searchForm }
  // 清理空值
  Object.keys(params).forEach(k => {
    if (params[k] === '' || params[k] === undefined) delete params[k]
  })
  return params
}

function handleSearch() {
  pagination.pageNum = 1
  searchForm.columnId = selectedColumnId.value != null ? selectedColumnId.value : -1
  loadTableData()
}

function handleReset() {
  searchForm.title = ''
  searchForm.author = ''
  searchForm.seoTitle = ''
  searchForm.seoKeywords = ''
  searchForm.startShowDate = ''
  searchForm.endShowDate = ''
  searchForm.perpetualStatus = 1
  searchForm.showStatus = ''
}

// ========== 表格 ==========
const tableRef = ref(null)
const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])
const pagination = reactive({ pageNum: 1, pageSize: 15, total: 0 })

function onSelectionChange(rows) { selectedRows.value = rows }

async function loadTableData() {
  loading.value = true
  try {
    const params = { ...buildSearchParams(), pageNum: pagination.pageNum, pageSize: pagination.pageSize }
    const res = await listArticle(params)
    if (res.code === 1 || res.code === 0) {
      // TableVO: { code: 0/1, data: [...], count: N }
      tableData.value = res.data || []
      pagination.total = res.count || 0
    }
  } finally { loading.value = false }
}

// ========== 添加/编辑弹窗 ==========
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const editingId = ref(null)
const currentColumnName = ref('')
const previewCoverUrl = ref('')

const dialogTitle = computed(() => isEdit.value ? '编辑文章' : '添加文章')

const formData = reactive({
  id: null, columnId: null, title: '', coverImgUrl: '', imgBase64: '', startShowDate: '',
  endShowDate: '', perpetualStatus: 1, showStatus: '1', articleSort: 0,
  abstractContent: '', seoTitle: '', seoKeywords: '', seoDescription: '',
  author: '', publishDate: '', content: ''
})

const formRules = {
  title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
  startShowDate: [{ required: true, message: '请选择开始展示时间', trigger: 'change' }],
  showStatus: [{ required: true, message: '请选择显示状态', trigger: 'change' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  publishDate: [{ required: true, message: '请输入发布时间', trigger: 'blur' }]
}

function resetForm() {
  formData.id = null; formData.columnId = selectedColumnId.value != null ? selectedColumnId.value : null
  formData.title = ''; formData.coverImgUrl = ''; formData.imgBase64 = ''; formData.startShowDate = ''
  formData.endShowDate = ''; formData.perpetualStatus = 1; formData.showStatus = '1'
  formData.articleSort = 0; formData.abstractContent = ''; formData.seoTitle = ''
  formData.seoKeywords = ''; formData.seoDescription = ''; formData.author = ''
  formData.publishDate = ''; formData.content = ''
  previewCoverUrl.value = ''
}

function onPerpetualChange(val) {
  if (val === 1) formData.endShowDate = ''
}

function onFileChange(file) {
  const reader = new FileReader()
  reader.onload = (e) => {
    previewCoverUrl.value = e.target.result
    formData.imgBase64 = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

function beforeUpload(file) {
  const isImage = file.type.startsWith('image/')
  if (!isImage) { ElMessage.error('只能上传图片文件'); return false }
  return true
}

function handleAdd() {
  if (selectedColumnId.value == null || selectedColumnId.value === -1) {
    ElMessage.warning('请先在左侧选择所属栏目')
    return
  }
  currentColumnName.value = columnTreeRef.value?.getCurrentNode()?.name || ''
  isEdit.value = false; editingId.value = null
  resetForm()
  dialogVisible.value = true
}

async function handleEdit(row) {
  currentColumnName.value = row.columnName || ''
  isEdit.value = true; editingId.value = row.id
  formData.id = row.id
  formData.columnId = row.columnId
  formData.title = row.title || ''
  formData.coverImgUrl = row.coverImgUrl || ''
  formData.imgBase64 = ''
  previewCoverUrl.value = row.httpCoverImgUrl || ''
  formData.startShowDate = row.startShowDate || ''
  formData.endShowDate = row.endShowDate || ''
  formData.perpetualStatus = row.perpetualStatus === '1' || row.perpetualStatus === 1 ? 1 : 0
  formData.showStatus = row.showStatus != null ? String(row.showStatus) : '1'
  formData.articleSort = row.articleSort || 0
  formData.abstractContent = row.abstractContent || ''
  formData.seoTitle = row.seoTitle || ''
  formData.seoKeywords = row.seoKeywords || ''
  formData.seoDescription = row.seoDescription || ''
  formData.author = row.author || ''
  formData.publishDate = row.publishDate || ''
  formData.content = row.content || ''
  dialogVisible.value = true
  dialogLoading.value = true
  try {
    const res = await queryArticleById({ id: row.id })
    if (res.code === 1 && res.data) {
      formData.imgBase64 = res.data.imgBase64 || ''
      formData.coverImgUrl = res.data.coverImgUrl || ''
      previewCoverUrl.value = res.data.imgBase64 || res.data.httpCoverImgUrl || ''
      if (res.data.content) formData.content = res.data.content
    }
  } catch { } finally { dialogLoading.value = false }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const data = { ...formData }
    if (isEdit.value) {
      await updateArticle(data)
      ElMessage.success('修改成功')
    } else {
      await saveArticle(data)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadTableData()
  } catch { } finally { submitLoading.value = false }
}

// ========== 删除 ==========
function handleDelete(row) {
  ElMessageBox.confirm(`确定删除文章"${row.title}"吗？`, '删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteArticle({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 篇文章吗？`, '批量删除确认', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
  }).then(async () => {
    try { await deleteArticles(selectedRows.value); ElMessage.success('批量删除成功'); loadTableData() } catch { }
  }).catch(() => {})
}

// ========== 查看详情 ==========
const detailVisible = ref(false)
const detailLoading = ref(false)
const detail = ref(null)

async function handleView(row) {
  detailVisible.value = true
  detail.value = null
  detailLoading.value = true
  try {
    const res = await queryArticleDetail({ id: row.id })
    if (res.code === 1 && res.data && res.data.basicInfo) {
      detail.value = res.data.basicInfo
    }
  } catch { } finally { detailLoading.value = false }
}

// 初始加载
loadTableData()
</script>

<style lang="scss" scoped>
.article-management {
  .layout-split { display: flex; gap: $gap-md; }
  .left-tree { width: 260px; flex-shrink: 0;
    .tree-header { display: flex; align-items: center; justify-content: space-between;
      .tree-refresh { cursor: pointer; color: $text-secondary; &:hover { color: $primary; } }
    }
    .tree-card { height: calc(100vh - 130px); :deep(.el-card__body) { overflow-y: auto; height: calc(100% - 50px); } }
    .tree-body { height: 100%; }
  }
  .right-table { flex: 1; overflow: auto; }
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card {
    .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  .form-tip { color: $text-secondary; font-size: 12px; margin-top: 4px; }
  .dialog-scroll { max-height: 70vh; overflow-y: auto; padding-right: 4px; min-height: 120px; }
  .detail-section { margin-top: 16px;
    .detail-section__label { font-weight: 600; color: $text-primary; margin-bottom: 8px; }
  }
  .detail-content {
    border: 1px solid #dcdfe6; border-radius: 4px; padding: 12px;
    line-height: 1.7; word-break: break-word; overflow-wrap: break-word;
    :deep(img) { max-width: 100%; height: auto; }
    :deep(table) { border-collapse: collapse; max-width: 100%; }
    :deep(td), :deep(th) { border: 1px solid #dcdfe6; padding: 4px 8px; }
    :deep(video) { max-width: 100%; }
  }
}
</style>
