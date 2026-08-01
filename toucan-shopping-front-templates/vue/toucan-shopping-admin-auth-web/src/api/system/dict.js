import request from '@/utils/request'

export function listDict(data) {
  return request({ url: '/system/dict/list', method: 'post', data })
}

export function addDict(data) {
  return request({ url: '/system/dict/save', method: 'post', data })
}

export function updateDict(data) {
  return request({ url: '/system/dict/update', method: 'post', data })
}

export function delDict(id) {
  return request({ url: '/system/dict/delete', method: 'post', data: { id } })
}

export function queryDictTree(data) {
  return request({ url: '/system/dict/queryTree', method: 'post', data })
}
