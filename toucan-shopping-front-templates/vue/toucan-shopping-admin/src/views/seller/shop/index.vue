<template>
  <div class="shop-management">
    <h2 class="page-title">店铺列表</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="店铺名称">
          <el-input v-model="searchForm.name" placeholder="请输入店铺名称" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="公开店铺ID">
          <el-input v-model="searchForm.publicShopId" placeholder="请输入公开店铺ID" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="searchForm.approveStatus" placeholder="全部" clearable style="width:130px">
            <el-option label="审核中" value="1" />
            <el-option label="审核通过" value="2" />
            <el-option label="审核驳回" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="店铺类型">
          <el-select v-model="searchForm.type" placeholder="全部" clearable style="width:130px">
            <el-option label="个人" value="1" />
            <el-option label="企业" value="2" />
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
        <el-button type="danger" :icon="Delete" v-permission="'toucan:seller:shop:deletes'" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="店铺图标" width="80" align="center">
          <template #default="{ row }">
            <el-image v-if="row.httpLogo" :src="row.httpLogo" :preview-src-list="[row.httpLogo]" preview-teleported fit="cover" style="width:30px;height:30px;cursor:zoom-in" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="店铺名称" width="200" show-overflow-tooltip />
        <el-table-column prop="publicShopId" label="公开店铺ID" width="160" show-overflow-tooltip />
        <el-table-column prop="type" label="店铺类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 2 ? 'warning' : 'info'" size="small">{{ row.type === 1 ? '个人' : row.type === 2 ? '企业' : row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approveStatus" label="审核状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="approveTagType(row.approveStatus)" size="small">{{ approveText(row.approveStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enableStatus" label="启用状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.enableStatus === 1 ? 'success' : 'info'" size="small">{{ row.enableStatus === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="所在地区" width="180" show-overflow-tooltip>
          <template #default="{ row }">{{ [row.province, row.city, row.area].filter(Boolean).join(' ') }}</template>
        </el-table-column>
        <el-table-column prop="shopRank" label="排序" width="90" align="center" />
        <el-table-column prop="createAdminName" label="创建人" width="110" />
        <el-table-column prop="updateAdminName" label="修改人" width="110" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="270" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:seller:shop:update'" @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link size="small" :icon="FolderOpened" @click="handleCategory(row)">分类</el-button>
            <el-button type="warning" link size="small" v-permission="'toucan:seller:shop:disabledEnabled'" @click="handleToggleEnable(row)">{{ row.enableStatus === 1 ? '禁用' : '启用' }}</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:seller:shop:delete'" @click="handleDelete(row)">删除</el-button>
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
    <el-dialog v-model="detailVisible" title="查看店铺" width="700px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading" style="min-height:120px">
      <el-descriptions v-if="detailInfo" :column="2" border label-width="110px">
        <el-descriptions-item label="店铺名称" :span="2">{{ detailInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="公开店铺ID">{{ detailInfo.publicShopId }}</el-descriptions-item>
        <el-descriptions-item label="店铺类型">{{ detailInfo.type === 1 ? '个人' : detailInfo.type === 2 ? '企业' : detailInfo.type }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">{{ approveText(detailInfo.approveStatus) }}</el-descriptions-item>
        <el-descriptions-item label="启用状态">{{ detailInfo.enableStatus === 1 ? '启用' : '禁用' }}</el-descriptions-item>
        <el-descriptions-item label="店铺图标" :span="2">
          <el-image v-if="detailInfo.httpLogo" :src="detailInfo.httpLogo" :preview-src-list="[detailInfo.httpLogo]" preview-teleported fit="contain" style="width:120px;height:100px" />
        </el-descriptions-item>
        <el-descriptions-item label="所在地区" :span="2">{{ [detailInfo.province, detailInfo.city, detailInfo.area].filter(Boolean).join(' ') }}</el-descriptions-item>
        <el-descriptions-item label="详细地址" :span="2">{{ detailInfo.detailAddress }}</el-descriptions-item>
        <el-descriptions-item label="店铺介绍" :span="2">{{ detailInfo.introduce }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailInfo.remark }}</el-descriptions-item>
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

    <!-- 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" title="编辑店铺" width="640px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" v-loading="editLoading">
        <el-form-item label="店铺名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入店铺名称" maxlength="50" />
        </el-form-item>
        <el-form-item label="店铺介绍">
          <el-input v-model="formData.introduce" type="textarea" :rows="2" maxlength="255" placeholder="请输入店铺介绍" />
        </el-form-item>
        <el-form-item label="店铺图标">
          <el-upload
            :auto-upload="false"
            :show-file-list="false"
            accept="image/*"
            :on-change="onLogoChange"
            :before-upload="beforeLogoUpload"
          >
            <img v-if="formData.httpLogo" :src="formData.httpLogo" style="width:80px;height:80px;object-fit:contain;border:1px solid #dcdfe6;border-radius:4px" />
            <el-button v-else type="primary" :icon="Upload">上传图标</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="所在地区">
          <div style="display:flex;gap:8px;width:100%">
            <el-select v-model="formData.provinceCode" placeholder="省份/直辖市" style="flex:1" :loading="provinceLoading" @change="onProvinceChange">
              <el-option v-for="p in provinceList" :key="p.code" :label="p.name" :value="p.code" />
            </el-select>
            <el-select v-if="!isMunicipality" v-model="formData.cityCode" placeholder="地市" style="flex:1" :loading="cityLoading" @change="onCityChange">
              <el-option v-for="c in cityList" :key="c.code" :label="c.name" :value="c.code" />
            </el-select>
            <el-select v-model="formData.areaCode" placeholder="区县" style="flex:1" :loading="areaLoading" @change="onAreaChange">
              <el-option v-for="a in areaList" :key="a.code" :label="a.name" :value="a.code" />
            </el-select>
          </div>
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="formData.detailAddress" placeholder="请输入详细地址" maxlength="100" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-radio-group v-model="formData.enableStatus">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="2" maxlength="255" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分类管理弹窗 -->
    <el-dialog v-model="categoryVisible" :title="'分类管理 - ' + categoryShopName" width="860px" :close-on-click-modal="false" destroy-on-close>
      <div class="toolbar" style="margin-bottom:8px">
        <el-button type="primary" :icon="Plus" v-permission="'toucan:seller:shopCategory:list:add:toolbarbtn'" @click="openCategoryAdd(-1)">添加根分类</el-button>
        <el-button :icon="RefreshRight"    v-permission="'toucan:seller:shopCategory:tree:table'"  @click="loadCategoryRoot">刷新</el-button>
      </div>
      <el-table ref="categoryTableRef" :key="categoryTableKey" :data="categoryData" border stripe v-loading="categoryLoading" row-key="id" :tree-props="{ children: 'children' }" max-height="500">
        <el-table-column prop="name" label="名称" width="240" />
        <el-table-column prop="categorySort" label="排序" width="90" align="center" />
        <el-table-column prop="href" label="跳转路径" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="updateDate" label="修改时间" width="170" />
        <el-table-column label="操作" width="300" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Plus" v-permission="'toucan:seller:shopCategory:list:addChildCategory:btn'" @click="openCategoryAdd(row.id)">添加子分类</el-button>
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:seller:shopCategory:list:btn:edit'" @click="openCategoryEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" v-permission="'toucan:seller:shopCategory:list:btn:delete'" @click="handleCategoryDelete(row)">删除</el-button>
            <el-button link size="small" v-permission="'toucan:seller:shopCategory:list:move:up:btn'" @click="handleCategoryMove('up', row)">上移</el-button>
            <el-button link size="small" v-permission="'toucan:seller:shopCategory:list:move:down:btn'" @click="handleCategoryMove('down', row)">下移</el-button>
            <el-button link size="small" v-permission="'toucan:seller:shopCategory:list:move:top:btn'" @click="handleCategoryMove('top', row)">置顶</el-button>
            <el-button link size="small" v-permission="'toucan:seller:shopCategory:list:move:bottom:btn'" @click="handleCategoryMove('bottom', row)">置底</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 分类添加/编辑弹窗 -->
    <el-dialog v-model="categoryDialogVisible" :title="categoryDialogTitle" width="460px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="categoryFormRef" :model="categoryForm" :rules="categoryFormRules" label-width="90px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" maxlength="25" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="categoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCategorySubmit" :loading="categorySubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, View, Edit, Delete, Plus, Upload, FolderOpened } from '@element-plus/icons-vue'
import {
  listShop, updateShop, deleteShop, deleteShops, disabledEnabledShop, listAreaByParentCode,
  queryShopById, detailShop,
  shopCategoryTreeTableByPid, saveShopCategory, updateShopCategory, deleteShopCategory,
  moveShopCategoryUp, moveShopCategoryDown, moveShopCategoryTop, moveShopCategoryBottom
} from '@/api/seller/shop'

const searchForm = reactive({ name: '', publicShopId: '', approveStatus: '', type: '' })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleReset() { searchForm.name = ''; searchForm.publicShopId = ''; searchForm.approveStatus = ''; searchForm.type = ''; handleSearch() }

const tableData = ref([])
const loading = ref(false)
const selectedRows = ref([])
const tableRef = ref(null)
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function onSelectionChange(rows) { selectedRows.value = rows }

function buildSearchParams() {
  const p = { ...searchForm, page: pagination.page, limit: pagination.limit }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listShop(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

function approveText(v) {
  if (v === 1) return '审核中'
  if (v === 2) return '审核通过'
  if (v === 3) return '审核驳回'
  return v
}
function approveTagType(v) {
  if (v === 2) return 'success'
  if (v === 3) return 'danger'
  return 'warning'
}

// ============ 查看 ============
const detailVisible = ref(false)
const detailInfo = ref(null)
const detailLoading = ref(false)
async function handleView(row) {
  detailVisible.value = true
  detailInfo.value = null
  detailLoading.value = true
  try {
    const res = await detailShop({ id: row.id })
    detailInfo.value = (res && res.data && res.data.basicInfo) || row
  } catch { } finally { detailLoading.value = false }
}

// ============ 编辑 ============
const dialogVisible = ref(false)
const submitLoading = ref(false)
const editLoading = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null, publicShopId: '', name: '', introduce: '', logo: '', httpLogo: '', logoBase64: '',
  provinceCode: '', cityCode: '', areaCode: '', province: '', city: '', area: '',
  detailAddress: '', enableStatus: 1, remark: '',
  userMainId: null, approveStatus: null, shopRank: null, categoryMaxCount: null, type: null
})
const formRules = {
  name: [{ required: true, message: '请输入店铺名称', trigger: 'blur' }]
}

const provinceList = ref([])
const cityList = ref([])
const areaList = ref([])
const isMunicipality = ref(false)
const provinceLoading = ref(false)
const cityLoading = ref(false)
const areaLoading = ref(false)

async function loadProvinceList() {
  provinceLoading.value = true
  try {
    const res = await listAreaByParentCode({ code: '-1' })
    if (res.code === 1) provinceList.value = res.data || []
  } finally { provinceLoading.value = false }
}

async function onProvinceChange(code) {
  cityList.value = []; areaList.value = []
  formData.cityCode = ''; formData.city = ''; formData.areaCode = ''; formData.area = ''
  const p = provinceList.value.find(x => x.code === code)
  if (p) formData.province = p.name
  isMunicipality.value = !!(p && (p.isMunicipality === 1 || p.isMunicipality === '1'))
  if (isMunicipality.value) {
    areaLoading.value = true
    try {
      const res = await listAreaByParentCode({ code: code })
      if (res.code === 1) areaList.value = res.data || []
    } finally { areaLoading.value = false }
  } else {
    cityLoading.value = true
    try {
      const res = await listAreaByParentCode({ code: code })
      if (res.code === 1) cityList.value = res.data || []
    } finally { cityLoading.value = false }
  }
}

async function onCityChange(code) {
  areaList.value = []
  formData.areaCode = ''; formData.area = ''
  const c = cityList.value.find(x => x.code === code)
  if (c) formData.city = c.name
  areaLoading.value = true
  try {
    const res = await listAreaByParentCode({ code: code })
    if (res.code === 1) areaList.value = res.data || []
  } finally { areaLoading.value = false }
}

function onAreaChange(code) {
  const a = areaList.value.find(x => x.code === code)
  if (a) formData.area = a.name
}

function onLogoChange(file) {
  const reader = new FileReader()
  reader.onload = (e) => {
    formData.logoBase64 = e.target.result
    formData.httpLogo = e.target.result
  }
  reader.readAsDataURL(file.raw)
}
function beforeLogoUpload(file) {
  if (!file.type.startsWith('image/')) { ElMessage.error('只能上传图片文件'); return false }
  return true
}

async function handleEdit(row) {
  dialogVisible.value = true
  editLoading.value = true
  // 先重置，避免残留上一次打开的数据
  Object.assign(formData, {
    id: null, publicShopId: '', name: '', introduce: '', logo: '', httpLogo: '', logoBase64: '',
    provinceCode: '', cityCode: '', areaCode: '', province: '', city: '', area: '',
    detailAddress: '', enableStatus: 1, remark: '',
    userMainId: null, approveStatus: null, shopRank: null, categoryMaxCount: null, type: null
  })
  isMunicipality.value = false
  try {
    const res = await queryShopById({ id: row.id })
    const vo = res && res.data
    if (!vo) {
      dialogVisible.value = false
      return
    }
    Object.assign(formData, {
      id: vo.id, publicShopId: vo.publicShopId, name: vo.name || '', introduce: vo.introduce || '',
      logo: vo.logo || '', httpLogo: vo.logoBase64 || vo.httpLogo || '', logoBase64: vo.logoBase64 || '',
      provinceCode: vo.provinceCode || '', cityCode: vo.cityCode || '', areaCode: vo.areaCode || '',
      province: vo.province || '', city: vo.city || '', area: vo.area || '',
      detailAddress: vo.detailAddress || '', enableStatus: vo.enableStatus != null ? Number(vo.enableStatus) : 1, remark: vo.remark || '',
      userMainId: vo.userMainId != null ? vo.userMainId : null,
      approveStatus: vo.approveStatus != null ? vo.approveStatus : null,
      shopRank: vo.shopRank != null ? vo.shopRank : null,
      categoryMaxCount: vo.categoryMaxCount != null ? vo.categoryMaxCount : null,
      type: vo.type != null ? vo.type : null
    })
    // 级联加载地区并回显
    await loadProvinceList()
    const pCode = formData.provinceCode
    const cCode = vo.cityCode || ''
    const aCode = vo.areaCode || ''
    if (pCode) {
      await onProvinceChange(pCode)
      if (cCode) {
        formData.cityCode = cCode
        formData.city = vo.city || ''
        await onCityChange(cCode)
        formData.areaCode = aCode
        formData.area = vo.area || ''
      } else {
        // 直辖市：省份变更时已直接加载区县
        formData.areaCode = aCode
        formData.area = vo.area || ''
      }
    }
  } catch {
    dialogVisible.value = false
  } finally {
    editLoading.value = false
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const res = await updateShop({ ...formData })
    if (res.code === 1) {
      ElMessage.success('修改成功')
      dialogVisible.value = false
      loadTableData()
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  } catch { } finally { submitLoading.value = false }
}

// ============ 启用/禁用 ============
function handleToggleEnable(row) {
  const action = row.enableStatus === 1 ? '禁用' : '启用'
  ElMessageBox.confirm(`确定${action}该店铺吗？`, `${action}确认`, { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await disabledEnabledShop({ publicShopId: row.publicShopId })
        if (res.code === 1) { ElMessage.success(`${action}成功`); loadTableData() } else { ElMessage.error(res.msg || `${action}失败`) }
      } catch { }
    }).catch(() => {})
}

// ============ 删除 ============
function handleDelete(row) {
  ElMessageBox.confirm('确定删除该店铺吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteShop({ id: row.id })
        if (res.code === 1) { ElMessage.success('删除成功'); loadTableData() } else { ElMessage.error(res.msg || '删除失败') }
      } catch { }
    }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) { ElMessage.warning('请选择要删除的记录'); return }
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个店铺吗？`, '批量删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteShops(selectedRows.value.map(r => ({ id: r.id })))
        if (res.code === 1) { ElMessage.success('批量删除成功'); loadTableData() } else { ElMessage.error(res.msg || '批量删除失败') }
      } catch { }
    }).catch(() => {})
}

// ============ 分类管理 ============
const categoryVisible = ref(false)
const categoryLoading = ref(false)
const categoryData = ref([])
const categoryShopId = ref(null)
const categoryShopName = ref('')
const categoryTableRef = ref(null)
const categoryTableKey = ref(0)

function handleCategory(row) {
  categoryShopId.value = row.id
  categoryShopName.value = row.name || ''
  categoryVisible.value = true
  loadCategoryRoot()
}

async function loadCategoryRoot() {
  categoryLoading.value = true
  try {
    const res = await shopCategoryTreeTableByPid({ shopId: categoryShopId.value, parentId: -1 })
    const roots = res.code === 1 ? (res.data || []) : []
    // 店铺分类最多两级，一次性加载所有子分类，前端构建树，避免懒加载缓存导致列表不刷新
    await Promise.all(roots.map(async (root) => {
      const childRes = await shopCategoryTreeTableByPid({ shopId: categoryShopId.value, parentId: root.id })
      root.children = childRes.code === 1 ? (childRes.data || []) : []
    }))
    categoryData.value = roots
    // 重建表格，清空展开状态，确保新增/删除子分类后重新展开显示最新数据
    categoryTableKey.value++
  } catch { } finally { categoryLoading.value = false }
}

const categoryDialogVisible = ref(false)
const categorySubmitLoading = ref(false)
const categoryFormRef = ref(null)
const categoryForm = reactive({ id: null, parentId: -1, name: '' })
const categoryFormRules = { name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }] }
const categoryDialogTitle = computed(() => categoryForm.id ? '编辑分类' : '添加分类')

function openCategoryAdd(parentId) {
  categoryForm.id = null
  categoryForm.parentId = parentId
  categoryForm.name = ''
  categoryDialogVisible.value = true
}

function openCategoryEdit(row) {
  categoryForm.id = row.id
  categoryForm.parentId = row.parentId
  categoryForm.name = row.name || ''
  categoryDialogVisible.value = true
}

async function handleCategorySubmit() {
  const valid = await categoryFormRef.value.validate().catch(() => false)
  if (!valid) return
  categorySubmitLoading.value = true
  try {
    const payload = { shopId: categoryShopId.value, parentId: categoryForm.parentId, name: categoryForm.name }
    let res
    if (categoryForm.id) {
      res = await updateShopCategory({ ...payload, id: categoryForm.id })
    } else {
      res = await saveShopCategory(payload)
    }
    if (res.code === 1) {
      ElMessage.success(categoryForm.id ? '修改成功' : '添加成功')
      categoryDialogVisible.value = false
      loadCategoryRoot()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch { } finally { categorySubmitLoading.value = false }
}

function handleCategoryDelete(row) {
  ElMessageBox.confirm('确定删除该分类吗？', '删除确认', { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteShopCategory({ id: row.id })
        if (res.code === 1) { ElMessage.success('删除成功'); loadCategoryRoot() } else { ElMessage.error(res.msg || '删除失败') }
      } catch { }
    }).catch(() => {})
}

function handleCategoryMove(type, row) {
  const payload = { shopId: categoryShopId.value, id: row.id, parentId: row.parentId }
  const fnMap = { up: moveShopCategoryUp, down: moveShopCategoryDown, top: moveShopCategoryTop, bottom: moveShopCategoryBottom }
  fnMap[type](payload).then(res => {
    if (res.code === 1) { ElMessage.success('移动成功'); loadCategoryRoot() } else { ElMessage.error(res.msg || '移动失败') }
  }).catch(() => {})
}

loadTableData()
</script>

<style lang="scss" scoped>
.shop-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card { .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; } .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; } }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
}
</style>
