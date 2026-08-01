import request from '@/utils/request'

export function listConfig(data) {
  return request({ url: '/system/config/list', method: 'post', data })
}

export function listAllConfig() {
  return request({ url: '/system/config/listAll', method: 'post', data: {} })
}

export function addConfig(data) {
  return request({ url: '/system/config/save', method: 'post', data })
}

export function updateConfig(data) {
  return request({ url: '/system/config/update', method: 'post', data })
}

export function delConfig(id) {
  return request({ url: '/system/config/delete', method: 'post', data: { id } })
}

export function refreshConfigCache() {
  return request({ url: '/system/config/refreshCache', method: 'post', data: {} })
}
