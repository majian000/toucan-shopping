import request from '@/utils/request'

// 查询列表 (ShopProductApprovePageInfo @RequestBody -> data)
export function listShopProductApprove(data) {
  return request({ url: '/product/shopProductApprove/list', method: 'post', data })
}

// 查询关联的平台SPU列表 (ProductSpuPageInfo @RequestBody -> data)
export function queryProductSpuList(data) {
  return request({ url: '/product/shopProductApprove/query/product/spu/list', method: 'post', data })
}

// 查询审核SKU列表 (ShopProductApproveSkuPageInfo @RequestBody -> data)
export function queryShopProductApproveSkuList(data) {
  return request({ url: '/product/shopProductApprove/query/product/sku/list', method: 'post', data })
}

// 分类树 (CategoryVO @RequestBody -> data)
export function queryCategoryTreeByPid(data) {
  return request({ url: '/product/shopProductApprove/query/category/tree/pid', method: 'post', data })
}

// 删除 (ShopProductApprove @RequestBody -> data)
export function deleteShopProductApprove(data) {
  return request({ url: '/product/shopProductApprove/delete', method: 'post', data })
}

// 审核驳回 (ShopProductApproveRecordVO @RequestBody -> data)
export function rejectShopProductApprove(data) {
  return request({ url: '/product/shopProductApprove/reject', method: 'post', data })
}

// 审核通过 (ShopProductApproveVO @RequestBody -> data)
export function passShopProductApprove(data) {
  return request({ url: '/product/shopProductApprove/pass', method: 'post', data })
}
