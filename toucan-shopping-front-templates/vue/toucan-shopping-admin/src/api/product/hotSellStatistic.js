import request from '@/utils/request'

// 查询热销列表 (OrderHotSellPageInfo @RequestBody, JSON body)
export function queryHotSellListPage(data) {
  return request({ url: '/productSkuStatistic/queryHotSellListPage', method: 'post', data })
}
