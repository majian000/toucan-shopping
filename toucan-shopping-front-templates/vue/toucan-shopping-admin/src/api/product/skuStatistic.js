import request from '@/utils/request'

// 分类下商品统计 (ProductSkuStatisticVO @RequestBody -> data)
export function queryProductSkuStatistic(data) {
  return request({ url: '/productSkuStatistic/queryProductSkuStatistic', method: 'post', data })
}

// 分类树 (CategoryVO @RequestBody -> data)
export function queryCategoryTreeByPid(data) {
  return request({ url: '/product/productSku/query/category/tree/pid', method: 'post', data })
}
