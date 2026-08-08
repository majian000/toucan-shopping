import request from '@/utils/request'

// 登录日志列表
export function listLoginHistory(params) {
  return request({ url: '/adminLoginHistory/list', method: 'post', data: params })
}
