<template>
  <div class="designer-image-management">
    <h2 class="page-title">装修图片</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="店铺ID">
          <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading" row-key="id">
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
        <el-table-column prop="id" label="ID" width="180" />
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
        <el-table-column prop="shopId" label="店铺ID" width="180" />
        <el-table-column prop="fileName" label="文件名" width="180" show-overflow-tooltip />
        <el-table-column prop="fileSize" label="文件大小" width="110" align="center" />
        <el-table-column prop="createrName" label="创建人" width="120" />
        <el-table-column prop="updaterName" label="修改人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
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
    <el-dialog v-model="detailVisible" title="查看装修图片" width="600px" :close-on-click-modal="false" destroy-on-close>
      <el-descriptions v-if="detailInfo" :column="2" border label-width="100px">
        <el-descriptions-item label="标题" :span="2">{{ detailInfo.title }}</el-descriptions-item>
        <el-descriptions-item label="店铺ID">{{ detailInfo.shopId }}</el-descriptions-item>
        <el-descriptions-item label="文件名">{{ detailInfo.fileName }}</el-descriptions-item>
        <el-descriptions-item label="文件大小">{{ detailInfo.fileSize }}</el-descriptions-item>
        <el-descriptions-item label="图片" :span="2">
          <el-image v-if="detailInfo.httpImgPath" :src="detailInfo.httpImgPath" :preview-src-list="[detailInfo.httpImgPath]" preview-teleported fit="contain" style="width:200px;height:180px" />
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

    <!-- 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" title="编辑装修图片" width="520px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="90px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="图片" prop="file">
          <el-upload :auto-upload="false" :show-file-list="false" :on-change="onFileChange" :before-upload="beforeUpload" accept="image/*">
            <el-button type="primary" :icon="Upload">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="预览">
          <img v-if="previewUrl" :src="previewUrl" style="width:240px;height:180px;object-fit:contain;border:1px solid #dcdfe6;border-radius:4px" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, View, Edit, Delete, Upload } from '@element-plus/icons-vue'
import { listDesignerImage, updateDesignerImage, deleteDesignerImage } from '@/api/seller/designerImage'

const searchForm = reactive({ title: '', shopId: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.title = ''; searchForm.shopId = ''; handleSearch() }

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
    const res = await listDesignerImage(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

// ============ 查看 ============
const detailVisible = ref(false)
const detailInfo = ref(null)
function handleView(row) {
  detailInfo.value = row
  detailVisible.value = true
}

// ============ 编辑 ============
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const previewUrl = ref('')
const formData = reactive({ id: null, title: '', file: null })
const formRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  file: [{ required: true, validator: (rule, value, cb) => { if (!value) cb(new Error('请上传图片')); else cb() }, trigger: 'change' }]
}

function onFileChange(file) {
  const raw = file.raw
  formData.file = raw
  previewUrl.value = URL.createObjectURL(raw)
}
function beforeUpload(file) {
  if (!file.type.startsWith('image/')) { ElMessage.error('只能上传图片文件'); return false }
  return true
}

function handleEdit(row) {
  formData.id = row.id
  formData.title = row.title || ''
  formData.file = null
  previewUrl.value = row.httpImgPath || ''
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const fd = new FormData()
    fd.append('bannerImgFile', formData.file)
    fd.append('id', formData.id)
    fd.append('title', formData.title)
    const res = await updateDesignerImage(fd)
    if (res.code === 1) {
      ElMessage.success('修改成功')
      dialogVisible.value = false
      loadTableData()
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  } catch { } finally { submitLoading.value = false }
}

// ============ 删除 ============
function handleDelete(row) {
  ElMessageBox.confirm('确定删除该装修图片吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteDesignerImage({ id: row.id })
        if (res.code === 1) { ElMessage.success('删除成功'); loadTableData() } else { ElMessage.error(res.msg || '删除失败') }
      } catch { }
    }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.designer-image-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
