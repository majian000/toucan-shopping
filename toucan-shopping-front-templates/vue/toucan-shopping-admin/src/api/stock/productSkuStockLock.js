import request from '@/utils/request'

// 查询列表 (ProductSkuStockLockPageInfo @RequestBody, JSON body)
export function listProductSkuStockLock(data) {
  return request({ url: '/stock/productSkuStockLock/list', method: 'post', data })
}
