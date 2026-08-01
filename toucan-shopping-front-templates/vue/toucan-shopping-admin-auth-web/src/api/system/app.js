import request from '@/utils/request'

// 应用列表
export function listApp(params) {
  return request({ url: '/app/list', method: 'post', data: params })
}

// 保存应用
export function saveApp(data) {
  return request({ url: '/app/save', method: 'post', data })
}

// 更新应用
export function updateApp(data) {
  return request({ url: '/app/update', method: 'post', data })
}

// 删除应用
export function delApp(id) {
  return request({ url: '/app/delete', method: 'post', data: { id } })
}

// 批量删除应用
export function batchDelApp(ids) {
  return request({ url: '/app/delete/ids', method: 'post', data: ids.map(id => ({ id })) })
}
