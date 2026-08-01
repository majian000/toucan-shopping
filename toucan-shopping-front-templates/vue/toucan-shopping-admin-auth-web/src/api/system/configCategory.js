import request from '@/utils/request'

export function listConfigCategory(data) {
  return request({ url: '/system/configCategory/list', method: 'post', data })
}

export function listAllConfigCategory() {
  return request({ url: '/system/configCategory/listAll', method: 'post', data: {} })
}

export function addConfigCategory(data) {
  return request({ url: '/system/configCategory/save', method: 'post', data })
}

export function updateConfigCategory(data) {
  return request({ url: '/system/configCategory/update', method: 'post', data })
}

export function delConfigCategory(id) {
  return request({ url: '/system/configCategory/delete', method: 'post', data: { id } })
}
