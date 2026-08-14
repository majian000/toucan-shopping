<template>
  <div class="index-recommend-column-management">
    <h2 class="page-title">首页推荐栏目</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-select v-model="searchForm.showStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="显示" value="1" />
            <el-option label="隐藏" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="searchForm.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="searchForm.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">添加首页推荐栏目</el-button>
      </div>
      <el-table ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id">
        <el-table-column prop="id" label="ID" width="90" align="center" />
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
        <el-table-column prop="startShowDate" label="开始展示时间" width="170" />
        <el-table-column prop="endShowDate" label="结束展示时间" width="170" />
        <el-table-column prop="type" label="类型" width="90" align="center">
          <template #default="{ row }">{{ row.type === '1' || row.type === 1 ? 'PC端' : row.type }}</template>
        </el-table-column>
        <el-table-column prop="columnSort" label="排序" width="80" align="center" />
        <el-table-column prop="showStatus" label="显示状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.showStatus === '1' || row.showStatus === 1 ? 'success' : 'info'" size="small">
              {{ row.showStatus === '1' || row.showStatus === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createAdminName" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateAdminName" label="修改人" width="120" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column prop="remark" label="备注" width="180" show-overflow-tooltip />
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
          v-model:current-page="pagination.pageNum" v-model:page-size="pagination.pageSize"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSearch" @current-change="handleSearch"
        />
      </div>
    </el-card>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="920px" :close-on-click-modal="false" destroy-on-close top="4vh">
      <div class="dialog-scroll" v-loading="dialogLoading">
        <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-form-item label="栏目标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入栏目标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="开始展示时间" prop="startShowDate">
          <el-date-picker v-model="formData.startShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束展示时间" prop="endShowDate">
          <el-date-picker v-model="formData.endShowDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择" style="width:100%" />
        </el-form-item>
        <el-form-item label="关联城市" prop="areaCodeList">
          <el-input :model-value="areaNames" readonly placeholder="请选择关联城市" style="width:100%" @click="openAreaDialog" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-radio-group v-model="formData.showStatus">
            <el-radio value="1">显示</el-radio>
            <el-radio value="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="栏目类型">
          <el-radio-group v-model="formData.type">
            <el-radio v-for="t in columnTypeDictList" :key="t.code" :value="t.code">{{ t.name }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.columnSort" :min="0" style="width:100%" placeholder="值越大越靠前" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="formData.remark" type="textarea" :rows="2" maxlength="255" placeholder="请输入备注信息" />
        </el-form-item>

        <el-divider content-position="left">顶部推荐标签</el-divider>
        <el-form-item label="顶部标签">
          <div style="width:100%">
            <div v-for="(item, index) in formData.topLabels" :key="index" class="row-item">
              <el-input v-model="item.labelName" placeholder="标签名称" style="width:280px" />
              <el-input v-model="item.clickPath" placeholder="点击跳转" style="width:280px" />
              <el-button type="danger" link :icon="Delete" @click="formData.topLabels.splice(index, 1)">删除</el-button>
            </div>
            <el-button type="primary" link :icon="Plus" @click="addTopLabel">添加一行</el-button>
          </div>
        </el-form-item>

        <el-divider content-position="left">左侧推荐标签</el-divider>
        <el-form-item label="左侧标签">
          <div style="width:100%">
            <div v-for="(item, index) in formData.leftLabels" :key="index" class="row-item">
              <el-input v-model="item.labelName" placeholder="标签名称" style="width:280px" />
              <el-input v-model="item.clickPath" placeholder="点击跳转" style="width:280px" />
              <el-button type="danger" link :icon="Delete" @click="formData.leftLabels.splice(index, 1)">删除</el-button>
            </div>
            <el-button type="primary" link :icon="Plus" @click="addLeftLabel">添加一行</el-button>
          </div>
        </el-form-item>

        <el-divider content-position="left">顶部图片</el-divider>
        <el-form-item label="顶部图片">
          <div class="banner-upload-inner">
            <img v-if="formData.topBanner.imgBase64 || formData.topBanner.httpImgPath" :src="formData.topBanner.imgBase64 || formData.topBanner.httpImgPath" class="banner-upload-preview" />
            <el-upload :auto-upload="false" :show-file-list="false" :before-upload="beforeImgUpload" :on-change="makeImgChangeHandler(formData.topBanner)" accept="image/*">
              <el-button size="small" :icon="Upload">上传图片</el-button>
            </el-upload>
            <el-input v-model="formData.topBanner.title" placeholder="标题" style="width:180px" />
            <el-input v-model="formData.topBanner.clickPath" placeholder="点击跳转" style="width:240px" />
          </div>
        </el-form-item>

        <el-divider content-position="left">左侧轮播图</el-divider>
        <el-form-item label="左侧轮播图">
          <div style="width:100%">
            <div v-for="(item, index) in formData.columnLeftBannerVOS" :key="index" class="banner-upload-inner banner-row">
              <img v-if="item.imgBase64 || item.httpImgPath" :src="item.imgBase64 || item.httpImgPath" class="banner-upload-preview" />
              <el-upload :auto-upload="false" :show-file-list="false" :before-upload="beforeImgUpload" :on-change="makeImgChangeHandler(item)" accept="image/*">
                <el-button size="small" :icon="Upload">上传图片</el-button>
              </el-upload>
              <el-input v-model="item.title" placeholder="标题" style="width:180px" />
              <el-input v-model="item.clickPath" placeholder="点击跳转" style="width:240px" />
              <el-button type="danger" link :icon="Delete" @click="formData.columnLeftBannerVOS.splice(index, 1)">删除</el-button>
            </div>
            <el-button type="primary" link :icon="Plus" @click="addLeftBanner">添加一行</el-button>
          </div>
        </el-form-item>

        <el-divider content-position="left">右侧顶部图片</el-divider>
        <el-form-item label="右侧顶部图片">
          <div class="banner-upload-inner">
            <img v-if="formData.rightTopBanner.imgBase64 || formData.rightTopBanner.httpImgPath" :src="formData.rightTopBanner.imgBase64 || formData.rightTopBanner.httpImgPath" class="banner-upload-preview" />
            <el-upload :auto-upload="false" :show-file-list="false" :before-upload="beforeImgUpload" :on-change="makeImgChangeHandler(formData.rightTopBanner)" accept="image/*">
              <el-button size="small" :icon="Upload">上传图片</el-button>
            </el-upload>
            <el-input v-model="formData.rightTopBanner.title" placeholder="标题" style="width:180px" />
            <el-input v-model="formData.rightTopBanner.clickPath" placeholder="点击跳转" style="width:240px" />
          </div>
        </el-form-item>

        <el-divider content-position="left">右侧底部图片</el-divider>
        <el-form-item label="右侧底部图片">
          <div class="banner-upload-inner">
            <img v-if="formData.rightBottomBanner.imgBase64 || formData.rightBottomBanner.httpImgPath" :src="formData.rightBottomBanner.imgBase64 || formData.rightBottomBanner.httpImgPath" class="banner-upload-preview" />
            <el-upload :auto-upload="false" :show-file-list="false" :before-upload="beforeImgUpload" :on-change="makeImgChangeHandler(formData.rightBottomBanner)" accept="image/*">
              <el-button size="small" :icon="Upload">上传图片</el-button>
            </el-upload>
            <el-input v-model="formData.rightBottomBanner.title" placeholder="标题" style="width:180px" />
            <el-input v-model="formData.rightBottomBanner.clickPath" placeholder="点击跳转" style="width:240px" />
          </div>
        </el-form-item>

        <el-divider content-position="left">底部图片</el-divider>
        <el-form-item label="底部图片">
          <div class="banner-upload-inner">
            <img v-if="formData.bottomBanner.imgBase64 || formData.bottomBanner.httpImgPath" :src="formData.bottomBanner.imgBase64 || formData.bottomBanner.httpImgPath" class="banner-upload-preview" />
            <el-upload :auto-upload="false" :show-file-list="false" :before-upload="beforeImgUpload" :on-change="makeImgChangeHandler(formData.bottomBanner)" accept="image/*">
              <el-button size="small" :icon="Upload">上传图片</el-button>
            </el-upload>
            <el-input v-model="formData.bottomBanner.title" placeholder="标题" style="width:180px" />
            <el-input v-model="formData.bottomBanner.clickPath" placeholder="点击跳转" style="width:240px" />
          </div>
        </el-form-item>

        <el-divider content-position="left">商品推荐（需配置6个）</el-divider>
        <div v-for="(item, index) in formData.columnRecommendProducts" :key="index" class="product-row">
          <el-image :src="item.imgBase64 || item.httpImgPath" fit="cover" class="product-img" />
          <el-upload :auto-upload="false" :show-file-list="false" :before-upload="beforeImgUpload" :on-change="makeImgChangeHandler(item)" accept="image/*">
            <el-button size="small" :icon="Upload">上传图片</el-button>
          </el-upload>
          <div class="product-field">
            <el-input v-model="item.productName" placeholder="商品名称" style="width:220px" />
            <span class="required-star">*</span>
          </div>
          <div class="product-field">
            <el-input v-model="item.productPrice" placeholder="商品价格" style="width:120px" />
            <span class="required-star">*</span>
          </div>
          <div class="product-field">
            <el-input v-model="item.clickPath" placeholder="点击跳转" style="width:220px" />
            <span class="required-star">*</span>
          </div>
        </div>
      </el-form>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 选择关联城市弹窗 -->
    <el-dialog v-model="areaDialogVisible" title="选择关联城市" width="500px" :close-on-click-modal="false" append-to-body>
      <div v-loading="areaTreeLoading" class="area-tree-wrap">
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
    <el-dialog v-model="detailVisible" title="查看首页推荐栏目" width="860px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detailInfo" :column="2" border label-width="120px">
          <el-descriptions-item label="栏目标题" :span="2">{{ detailInfo.title }}</el-descriptions-item>
          <el-descriptions-item label="开始展示时间">{{ detailInfo.startShowDate }}</el-descriptions-item>
          <el-descriptions-item label="结束展示时间">{{ detailInfo.endShowDate }}</el-descriptions-item>
          <el-descriptions-item label="显示状态">
            <el-tag :type="detailInfo.showStatus === 1 || detailInfo.showStatus === '1' ? 'success' : 'info'" size="small">
              {{ detailInfo.showStatus === 1 || detailInfo.showStatus === '1' ? '显示' : '隐藏' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="排序">{{ detailInfo.columnSort }}</el-descriptions-item>
          <el-descriptions-item label="关联城市" :span="2">{{ detailAreaNames }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detailInfo.remark }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="detailInfo" class="detail-tables">
          <div class="detail-table-block" v-if="detailInfo.topLabels && detailInfo.topLabels.length">
            <div class="detail-block-title">顶部推荐标签</div>
            <el-table :data="detailInfo.topLabels" border size="small">
              <el-table-column prop="labelName" label="标签名称" min-width="220" show-overflow-tooltip />
              <el-table-column prop="clickPath" label="点击跳转" min-width="260" show-overflow-tooltip />
            </el-table>
          </div>
          <div class="detail-table-block" v-if="detailInfo.leftLabels && detailInfo.leftLabels.length">
            <div class="detail-block-title">左侧推荐标签</div>
            <el-table :data="detailInfo.leftLabels" border size="small">
              <el-table-column prop="labelName" label="标签名称" min-width="220" show-overflow-tooltip />
              <el-table-column prop="clickPath" label="点击跳转" min-width="260" show-overflow-tooltip />
            </el-table>
          </div>
          <div v-for="sec in bannerSections" :key="sec.label" class="detail-table-block">
            <div class="detail-block-title">{{ sec.label }}</div>
            <el-table :data="sec.data" border size="small">
              <el-table-column label="图片预览" width="200" align="center">
                <template #default="{ row }">
                  <el-image v-if="row.httpImgPath" :src="row.httpImgPath" fit="cover" style="width:120px;height:100px" :preview-src-list="[row.httpImgPath]" preview-teleported />
                </template>
              </el-table-column>
              <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
              <el-table-column prop="clickPath" label="点击跳转" min-width="220" show-overflow-tooltip />
            </el-table>
          </div>
          <div class="detail-table-block" v-if="detailInfo.columnRecommendProducts && detailInfo.columnRecommendProducts.length">
            <div class="detail-block-title">推荐商品</div>
            <el-table :data="detailInfo.columnRecommendProducts" border size="small">
              <el-table-column label="商品图片" width="200" align="center">
                <template #default="{ row }">
                  <el-image v-if="row.httpImgPath" :src="row.httpImgPath" fit="cover" style="width:120px;height:100px" :preview-src-list="[row.httpImgPath]" preview-teleported />
                </template>
              </el-table-column>
              <el-table-column prop="productName" label="商品名称" min-width="180" show-overflow-tooltip />
              <el-table-column prop="productPrice" label="商品价格" width="120" />
              <el-table-column prop="clickPath" label="点击跳转" min-width="220" show-overflow-tooltip />
            </el-table>
          </div>
        </div>
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
import { listIndexRecommendColumn, saveIndexRecommendColumn, updateIndexRecommendColumn, findIndexRecommendColumn, deleteIndexRecommendColumn, queryAreaTree } from '@/api/content/indexRecommendColumn'
import { queryColumnDict } from '@/api/content/column'

// ========== 列表 ==========
const searchForm = reactive({ title: '', showStatus: '', startShowDate: '', endShowDate: '' })

function handleSearch() { pagination.pageNum = 1; loadTableData() }
function handleReset() { searchForm.title = ''; searchForm.showStatus = ''; searchForm.startShowDate = ''; searchForm.endShowDate = '' }

const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ pageNum: 1, pageSize: 15, total: 0 })

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.pageNum, limit: pagination.pageSize }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listIndexRecommendColumn(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

// ========== 栏目类型字典 ==========
const columnTypeDictList = ref([])
async function loadColumnDict() {
  try {
    const res = await queryColumnDict()
    if (res.code === 1 && res.data) {
      columnTypeDictList.value = res.data.columnTypeList || []
    }
  } catch { }
}

// ========== 图片上传（base64，后端统一处理） ==========
function beforeImgUpload(file) {
  if (!file.type.startsWith('image/')) { ElMessage.error('只能上传图片文件'); return false }
  return true
}

function makeImgChangeHandler(target) {
  return (file) => {
    const raw = file.raw
    if (!raw) return
    if (!raw.type.startsWith('image/')) { ElMessage.error('只能上传图片文件'); return }
    const reader = new FileReader()
    reader.onload = (e) => { target.imgBase64 = e.target.result }
    reader.readAsDataURL(raw)
  }
}

// ========== 表单 ==========
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const dialogTitle = computed(() => isEdit.value ? '编辑首页推荐栏目' : '添加首页推荐栏目')

function emptyBanner() { return { title: '', imgPath: '', httpImgPath: '', imgBase64: '', clickPath: '' } }
function emptyProduct() { return { productName: '', productPrice: '', clickPath: '', imgPath: '', httpImgPath: '', imgBase64: '' } }

const formData = reactive({
  id: null, title: '', startShowDate: '', endShowDate: '', showStatus: '1', type: '1', columnSort: 0, remark: '',
  areaCodeList: [], areaNameList: [],
  topLabels: [], leftLabels: [],
  topBanner: emptyBanner(), columnLeftBannerVOS: [], rightTopBanner: emptyBanner(), rightBottomBanner: emptyBanner(), bottomBanner: emptyBanner(),
  columnRecommendProducts: []
})

const formRules = {
  title: [{ required: true, message: '请输入栏目标题', trigger: 'blur' }],
  startShowDate: [{ required: true, message: '请选择开始展示时间', trigger: 'change' }],
  endShowDate: [{ required: true, message: '请选择结束展示时间', trigger: 'change' }],
  areaCodeList: [{ required: true, validator: (rule, value, cb) => { if (!value || value.length === 0) cb(new Error('请选择关联城市')); else cb() }, trigger: 'change' }]
}

function resetForm() {
  formData.id = null; formData.title = ''; formData.startShowDate = ''; formData.endShowDate = ''
  formData.showStatus = '1'; formData.type = '1'; formData.columnSort = 0; formData.remark = ''
  formData.areaCodeList = []; formData.areaNameList = []
  formData.topLabels = []; formData.leftLabels = []
  formData.topBanner = emptyBanner(); formData.columnLeftBannerVOS = []
  formData.rightTopBanner = emptyBanner(); formData.rightBottomBanner = emptyBanner(); formData.bottomBanner = emptyBanner()
  formData.columnRecommendProducts = Array.from({ length: 6 }, () => emptyProduct())
  areaNames.value = ''
}

function addTopLabel() { formData.topLabels.push({ labelName: '', clickPath: '' }) }
function addLeftLabel() { formData.leftLabels.push({ labelName: '', clickPath: '' }) }
function addLeftBanner() { formData.columnLeftBannerVOS.push(emptyBanner()) }

function handleAdd() {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  resetForm()
  formData.id = row.id
  dialogVisible.value = true
  dialogLoading.value = true
  findIndexRecommendColumn({ id: row.id })
    .then(res => {
      if (res.code === 1 && res.data) fillForm(res.data)
    }).catch(() => {}).finally(() => { dialogLoading.value = false })
}

function fillForm(entity) {
  formData.title = entity.title || ''
  formData.startShowDate = entity.startShowDate || ''
  formData.endShowDate = entity.endShowDate || ''
  formData.showStatus = entity.showStatus != null ? String(entity.showStatus) : '1'
  formData.type = entity.type || '1'
  formData.columnSort = entity.columnSort || 0
  formData.remark = entity.remark || ''
  formData.topLabels = (entity.topLabels || []).map(l => ({ labelName: l.labelName || '', clickPath: l.clickPath || '' }))
  formData.leftLabels = (entity.leftLabels || []).map(l => ({ labelName: l.labelName || '', clickPath: l.clickPath || '' }))
  formData.topBanner = entity.topBanner || emptyBanner()
  formData.columnLeftBannerVOS = entity.columnLeftBannerVOS || []
  formData.rightTopBanner = entity.rightTopBanner || emptyBanner()
  formData.rightBottomBanner = entity.rightBottomBanner || emptyBanner()
  formData.bottomBanner = entity.bottomBanner || emptyBanner()
  // 商品推荐固定 6 行，不足补空行
  const products = (entity.columnRecommendProducts || []).slice(0, 6)
  while (products.length < 6) products.push(emptyProduct())
  formData.columnRecommendProducts = products
  // 关联城市
  formData.areaCodeList = (entity.columnAreas || []).map(a => a.areaCode)
  formData.areaNameList = (entity.columnAreas || []).map(a => a.areaName)
  areaNames.value = formData.areaNameList.join(' ')
}

function hasImg(item) { return !!(item && (item.imgBase64 || item.imgPath)) }

function validateBeforeSubmit() {
  if (!formData.columnLeftBannerVOS.length || formData.columnLeftBannerVOS.some(b => !hasImg(b))) {
    ElMessage.warning('请上传左侧轮播图')
    return false
  }
  if (!hasImg(formData.rightTopBanner)) { ElMessage.warning('请上传右侧顶部图片'); return false }
  if (!hasImg(formData.rightBottomBanner)) { ElMessage.warning('请上传右侧底部图片'); return false }
  const products = formData.columnRecommendProducts
  if (!products.length || products.length < 6) { ElMessage.warning('请配置商品推荐,数量至少6个'); return false }
  for (let i = 0; i < products.length; i++) {
    const p = products[i]
    if (!hasImg(p)) { ElMessage.warning('请上传商品图片'); return false }
    if (!p.productName) { ElMessage.warning('商品名称不能为空'); return false }
    if (!p.productPrice) { ElMessage.warning('商品价格不能为空'); return false }
    if (!p.clickPath) { ElMessage.warning('商品点击跳转不能为空'); return false }
  }
  return true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (!validateBeforeSubmit()) return
  submitLoading.value = true
  try {
    const payload = {
      id: formData.id,
      title: formData.title,
      startShowDate: formData.startShowDate,
      endShowDate: formData.endShowDate,
      showStatus: Number(formData.showStatus),
      type: formData.type,
      columnSort: Number(formData.columnSort),
      remark: formData.remark,
      areaCodeList: formData.areaCodeList,
      areaNameList: formData.areaNameList,
      topLabels: formData.topLabels,
      leftLabels: formData.leftLabels,
      topBanner: formData.topBanner,
      columnLeftBannerVOS: formData.columnLeftBannerVOS,
      rightTopBanner: formData.rightTopBanner,
      rightBottomBanner: formData.rightBottomBanner,
      bottomBanner: formData.bottomBanner,
      columnRecommendProducts: formData.columnRecommendProducts
    }
    if (isEdit.value) {
      await updateIndexRecommendColumn(payload)
      ElMessage.success('修改成功')
    } else {
      await saveIndexRecommendColumn(payload)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadTableData()
  } catch { } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除该首页推荐栏目吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try { await deleteIndexRecommendColumn({ id: row.id }); ElMessage.success('删除成功'); loadTableData() } catch { }
    }).catch(() => {})
}

// ========== 关联城市 ==========
const areaDialogVisible = ref(false)
const areaTreeLoading = ref(false)
const areaTreeRef = ref(null)
const areaTreeData = ref([])
const areaNames = ref('')

async function loadAreaTreeData() {
  try {
    const res = await queryAreaTree(formData.id || '')
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
    if (areaTreeRef.value) areaTreeRef.value.setCheckedKeys(formData.areaCodeList || [])
  } finally {
    areaTreeLoading.value = false
  }
}

function handleAreaConfirm() {
  if (!areaTreeRef.value) return
  const checkedKeys = areaTreeRef.value.getCheckedKeys()
  if (!checkedKeys.length) { ElMessage.warning('请选择地区'); return }
  formData.areaCodeList = checkedKeys
  formData.areaNameList = areaTreeRef.value.getCheckedNodes().map(n => n.text).filter(Boolean)
  areaNames.value = formData.areaNameList.join(' ')
  areaDialogVisible.value = false
}

// ========== 查看 ==========
const detailVisible = ref(false)
const detailInfo = ref(null)
const detailLoading = ref(false)
const detailAreaNames = computed(() => (detailInfo.value?.columnAreas || []).map(a => a.areaName).join(' '))

// 各位置图片分区（参考 layui 查看界面：每个位置独立表格展示 图片/标题/点击跳转）
const bannerSections = computed(() => {
  const d = detailInfo.value
  if (!d) return []
  const sections = []
  const push = (label, item) => {
    if (item && (item.httpImgPath || item.title || item.clickPath)) sections.push({ label, data: [item] })
  }
  push('顶部图片', d.topBanner)
  if (d.columnLeftBannerVOS && d.columnLeftBannerVOS.length) sections.push({ label: '左侧轮播图', data: d.columnLeftBannerVOS })
  push('右侧顶部图片', d.rightTopBanner)
  push('右侧底部图片', d.rightBottomBanner)
  push('底部图片', d.bottomBanner)
  return sections
})

function handleView(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  findIndexRecommendColumn({ id: row.id })
    .then(res => { if (res.code === 1 && res.data) detailInfo.value = res.data })
    .catch(() => {}).finally(() => { detailLoading.value = false })
}

loadTableData()
loadColumnDict()
</script>

<style lang="scss" scoped>
.index-recommend-column-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  .dialog-scroll { max-height: 70vh; overflow-y: auto; padding-right: 4px; }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }

  // 分隔标题
  :deep(.el-divider) { margin: 22px 0 16px; }
  :deep(.el-divider__text) { color: $primary; font-weight: 600; font-size: 14px; }

  // 可增删行（顶部/左侧标签）
  .row-item { display: flex; align-items: center; gap: 8px; margin-bottom: 8px;
    padding: 8px 10px; background: $bg-page; border-radius: 6px;
  }

  // 图片上传卡片
  .banner-upload-inner { display: flex; align-items: center; gap: 10px; flex-wrap: wrap;
    padding: 12px; background: $bg-page; border: 1px dashed $border-color; border-radius: 8px;
  }
  .banner-upload-preview { width: 120px; height: 100px; object-fit: cover; border: 1px solid $border-light; border-radius: 6px; background: #fff; }
  .banner-row { margin-bottom: 8px; }

  // 商品推荐行
  .product-row { display: flex; align-items: center; gap: 10px; margin-bottom: 8px;
    padding: 10px 12px; background: $bg-page; border-radius: 8px;
    .product-img { width: 120px; height: 100px; object-fit: cover; border: 1px solid $border-light; border-radius: 6px; background: #fff; }
  }
  .product-field { display: inline-flex; align-items: center;
    .required-star { color: #f56c6c; margin-left: 4px; font-weight: 600; }
  }
  :deep(.el-descriptions__table) { width: 100%; }
  :deep(.el-descriptions__label) { word-break: keep-all; }
  :deep(.el-descriptions__content) { word-break: break-all; overflow-wrap: anywhere; min-width: 0; }
  .detail-tables {
    margin-top: 16px;
    .detail-table-block { margin-bottom: 16px; }
    .detail-block-title { font-weight: 600; margin-bottom: 8px; color: $text-primary; }
  }
}

// 关联城市弹窗使用 append-to-body，需独立作用域规则才能生效
.area-tree-wrap { max-height: 400px; overflow-y: auto; }
</style>
