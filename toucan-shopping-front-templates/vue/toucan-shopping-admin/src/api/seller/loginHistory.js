import request from '@/utils/request'

// 查询列表 (SellerLoginHistoryPageInfo @RequestBody, JSON body)
export function listLoginHistory(data) {
  return request({ url: '/seller/loginHistory/list', method: 'post', data })
}
