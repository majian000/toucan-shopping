import request from '@/utils/request'

export function listOperateLog(data) {
  return request({ url: '/operateLog/list', method: 'post', data })
}

export function delOperateLog(id) {
  return request({ url: '/operateLog/delete', method: 'post', data: { id } })
}
