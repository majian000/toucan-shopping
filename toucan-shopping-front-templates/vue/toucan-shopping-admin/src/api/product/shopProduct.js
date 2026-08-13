import request from '@/utils/request'

// 查询列表 (ShopProductPageInfo @RequestBody -> data)
export function listShopProduct(data) {
  return request({ url: '/product/shopProduct/list', method: 'post', data })
}

// 分类树 (CategoryVO @RequestBody -> data)
export function queryCategoryTreeByPid(data) {
  return request({ url: '/product/shopProduct/query/category/tree/pid', method: 'post', data })
}

// 查询店铺商品的SKU列表 (ProductSkuPageInfo @RequestBody -> data)
export function queryShopProductSkuList(data) {
  return request({ url: '/product/shopProduct/query/product/sku/list', method: 'post', data })
}

// 上架/下架 (ShopProductVO @RequestBody -> data)
export function shelves(data) {
  return request({ url: '/product/shopProduct/shelves', method: 'post', data })
}

// SKU同步搜索缓存 (ShopProductVO @RequestBody -> data)
export function flushSearch(data) {
  return request({ url: '/product/shopProduct/flush/search', method: 'post', data })
}
