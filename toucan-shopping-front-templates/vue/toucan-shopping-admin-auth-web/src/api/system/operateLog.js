import request from '@/utils/request'

export function listOperateLog(data) {
  return request({ url: '/operateLog/list', method: 'post', data })
}

export function queryOperateLogDetail(id) {
  return request({ url: '/operateLog/detail', method: 'post', data: { id } })
}

export function delOperateLog(id) {
  return request({ url: '/operateLog/delete', method: 'post', data: { id } })
}
