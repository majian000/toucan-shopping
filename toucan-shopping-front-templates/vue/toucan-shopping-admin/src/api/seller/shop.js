import request from '@/utils/request'

// 查询列表 (SellerShopPageInfo @RequestBody, JSON body)
export function listShop(data) {
  return request({ url: '/seller/shop/list', method: 'post', data })
}

// 修改
export function updateShop(data) {
  return request({ url: '/seller/shop/update', method: 'post', data })
}

// 根据ID查询（编辑回显用，含 base64 图标数据）
export function queryShopById(data) {
  return request({ url: '/seller/shop/queryById', method: 'post', data })
}

// 查看详情 (返回 SellerShopDetailVO: { basicInfo })
export function detailShop(data) {
  return request({ url: '/seller/shop/detail', method: 'post', data })
}

// 删除
export function deleteShop(data) {
  return request({ url: '/seller/shop/delete', method: 'post', data })
}

// 批量删除
export function deleteShops(data) {
  return request({ url: '/seller/shop/delete/ids', method: 'post', data })
}

// 启用/禁用
export function disabledEnabledShop(data) {
  return request({ url: '/seller/shop/disabled/enabled', method: 'post', data })
}

// 上传店铺 logo (multipart/form-data)
export function uploadShopLogo(data) {
  return request({ url: '/seller/shop/upload/logo', method: 'post', data })
}

// 按 code 查询地区列表 (AreaVO JSON body: code = 父节点编码)
export function listAreaByParentCode(data) {
  return request({ url: '/area/list/by/parentCode', method: 'post', data })
}

// ================= 店铺分类 =================

// 查询分类树表格 (按父ID懒加载)
export function shopCategoryTreeTableByPid(data) {
  return request({ url: '/seller/shop/category/tree/table/by/pid', method: 'post', data })
}

// 保存分类
export function saveShopCategory(data) {
  return request({ url: '/seller/shop/category/save', method: 'post', data })
}

// 修改分类
export function updateShopCategory(data) {
  return request({ url: '/seller/shop/category/update', method: 'post', data })
}

// 删除分类
export function deleteShopCategory(data) {
  return request({ url: '/seller/shop/category/delete', method: 'post', data })
}

// 上移
export function moveShopCategoryUp(data) {
  return request({ url: '/seller/shop/category/move/up', method: 'post', data })
}

// 下移
export function moveShopCategoryDown(data) {
  return request({ url: '/seller/shop/category/move/down', method: 'post', data })
}

// 置顶
export function moveShopCategoryTop(data) {
  return request({ url: '/seller/shop/category/move/top', method: 'post', data })
}

// 置底
export function moveShopCategoryBottom(data) {
  return request({ url: '/seller/shop/category/move/bottom', method: 'post', data })
}
