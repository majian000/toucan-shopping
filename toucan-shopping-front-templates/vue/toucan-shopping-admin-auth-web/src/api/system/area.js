import request from '@/utils/request'

export function listArea(data) {
  return request({ url: '/system/area/list', method: 'post', data })
}

export function listAreaByPid(data) {
  return request({ url: '/system/area/listByPid', method: 'post', data })
}

export function getAreaTree() {
  return request({ url: '/system/area/tree', method: 'post', data: {} })
}

export function addArea(data) {
  return request({ url: '/system/area/save', method: 'post', data })
}

export function updateArea(data) {
  return request({ url: '/system/area/update', method: 'post', data })
}

export function delArea(id) {
  return request({ url: '/system/area/delete', method: 'post', data: { id } })
}
