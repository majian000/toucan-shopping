import request from '@/utils/request'

export function listDict(data) {
  return request({ url: '/dict/tree/table/by/pid', method: 'post', data })
}

export function addDict(data) {
  return request({ url: '/dict/save', method: 'post', data })
}

export function updateDict(data) {
  return request({ url: '/dict/update', method: 'post', data })
}

export function delDict(id) {
  return request({ url: '/dict/delete', method: 'post', data: { id } })
}

export function delBatchDict(data) {
  return request({ url: '/dict/delete/ids', method: 'post', data })
}

export function queryDictTree(data) {
  return request({ url: '/dict/tree/table/by/pid', method: 'post', data })
}
