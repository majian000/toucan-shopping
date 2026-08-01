import request from '@/utils/request'

// 获取当前用户的所有权限标识
export function getPermissions() {
  return request({ url: '/index/permissions', method: 'get' })
}
