import request from '@/utils/request'

// 分类下商品统计 (ProductSkuStatisticVO @RequestBody -> data)
// 该接口为聚合统计查询、后端较慢，单独调大超时时间(5分钟)
export function queryProductSkuStatistic(data) {
  return request({ url: '/productSkuStatistic/queryProductSkuStatistic', method: 'post', data, timeout: 300000 })
}

// 分类树 (CategoryVO @RequestBody -> data)
export function queryCategoryTreeByPid(data) {
  return request({ url: '/product/productSku/query/category/tree/pid', method: 'post', data })
}
