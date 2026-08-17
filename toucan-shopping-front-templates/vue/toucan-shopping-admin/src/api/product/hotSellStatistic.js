import request from '@/utils/request'

// 查询热销列表 (OrderHotSellPageInfo @RequestBody, JSON body)
// 该接口为聚合统计查询、后端较慢，单独调大超时时间(5分钟)
export function queryHotSellListPage(data) {
  return request({ url: '/productSkuStatistic/queryHotSellListPage', method: 'post', data, timeout: 300000 })
}
