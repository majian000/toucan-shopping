import request from '@/utils/request'

// 查询列表 (ProductSearchVO @RequestBody, JSON body)
export function searchList(data) {
  return request({ url: '/product/productSku/search/list', method: 'post', data })
}

// 分类树 (id @RequestParam -> params)
export function queryCategoryTreeByPid(params) {
  return request({ url: '/product/productSku/search/query/category/tree/pid', method: 'post', params })
}

// 根据ID删除缓存 (ProductSearchResultVO @RequestBody -> data)
export function deleteSearchById(data) {
  return request({ url: '/product/productSku/search/deleteById', method: 'post', data })
}

// 批量删除缓存 (List @RequestBody -> data)
export function deleteSearchByIds(data) {
  return request({ url: '/product/productSku/search/delete/ids', method: 'post', data })
}

// 清空搜索
export function clearSearch() {
  return request({ url: '/product/productSku/search/clear', method: 'post' })
}
