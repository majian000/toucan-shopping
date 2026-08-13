import request from '@/utils/request'

// 查询列表 (ProductSkuPageInfo @RequestBody -> data)
export function listProductSku(data) {
  return request({ url: '/product/productSku/list', method: 'post', data })
}

// 分类树 (CategoryVO @RequestBody -> data)
export function queryCategoryTreeByPid(data) {
  return request({ url: '/product/productSku/query/category/tree/pid', method: 'post', data })
}

// 上架/下架 (ShopProductVO @RequestBody -> data)
export function shelves(data) {
  return request({ url: '/product/productSku/shelves', method: 'post', data })
}

// SKU同步搜索缓存 (ShopProductVO @RequestBody -> data)
export function flushSearch(data) {
  return request({ url: '/product/productSku/flush/search', method: 'post', data })
}
