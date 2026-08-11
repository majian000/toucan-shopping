import request from '@/utils/request'

export function listUser(data) {
  return request({ url: '/user/list', method: 'post', data })
}
