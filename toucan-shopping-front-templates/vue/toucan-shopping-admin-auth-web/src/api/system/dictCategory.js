import request from '@/utils/request'

export function listDictCategory(data) {
  return request({ url: '/dictCategory/list', method: 'post', data })
}

export function listAllDictCategory() {
  return request({ url: '/dictCategory/list', method: 'post' })
}

export function addDictCategory(data) {
  return request({ url: '/dictCategory/save', method: 'post', data })
}

export function updateDictCategory(data) {
  return request({ url: '/dictCategory/update', method: 'post', data })
}

export function delDictCategory(id) {
  return request({ url: '/dictCategory/delete', method: 'post', data: { id } })
}
