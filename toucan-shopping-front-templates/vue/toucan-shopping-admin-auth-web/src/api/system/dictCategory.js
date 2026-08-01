import request from '@/utils/request'

export function listDictCategory(data) {
  return request({ url: '/system/dictCategory/list', method: 'post', data })
}

export function listAllDictCategory() {
  return request({ url: '/system/dictCategory/listAll', method: 'post' })
}

export function addDictCategory(data) {
  return request({ url: '/system/dictCategory/save', method: 'post', data })
}

export function updateDictCategory(data) {
  return request({ url: '/system/dictCategory/update', method: 'post', data })
}

export function delDictCategory(id) {
  return request({ url: '/system/dictCategory/delete', method: 'post', data: { id } })
}
