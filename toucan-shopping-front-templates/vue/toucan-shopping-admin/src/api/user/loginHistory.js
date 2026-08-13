import request from '@/utils/request'

// 查询用户登录历史 (SellerLoginHistoryPageInfo @RequestBody -> JSON body)
export function listLoginHistory(data) {
  return request({ url: '/user/loginHistory/list', method: 'post', data })
}
