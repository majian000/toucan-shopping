import request from '@/utils/request'

export function listOperateLog(data) {
  return request({ url: '/system/operateLog/list', method: 'post', data })
}

export function delOperateLog(id) {
  return request({ url: '/system/operateLog/delete', method: 'post', data: { id } })
}
