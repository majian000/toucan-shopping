import request from '@/utils/request'

// 查询列表 (BrandPageInfo @RequestBody, JSON body)
export function listBrand(data) {
  return request({ url: '/product/brand/list', method: 'post', data })
}

// 保存 (BrandVO @RequestBody -> data)
export function saveBrand(data) {
  return request({ url: '/product/brand/save', method: 'post', data })
}

// 修改 (BrandVO @RequestBody -> data)
export function updateBrand(data) {
  return request({ url: '/product/brand/update', method: 'post', data })
}

// 删除 (Brand @RequestBody -> data)
export function deleteBrand(data) {
  return request({ url: '/product/brand/delete', method: 'post', data })
}

// 批量删除 (List @RequestBody -> data)
export function deleteBrandByIds(data) {
  return request({ url: '/product/brand/delete/ids', method: 'post', data })
}

// 列表页分类树 (无参)
export function queryCategoryTreeForListPage() {
  return request({ url: '/product/brand/list/page/query/category/tree', method: 'post' })
}

// 含品牌关联选中状态的分类树 (brandId -> params)
export function queryCategoryTree(params) {
  return request({ url: '/product/brand/query/category/tree', method: 'post', params })
}
