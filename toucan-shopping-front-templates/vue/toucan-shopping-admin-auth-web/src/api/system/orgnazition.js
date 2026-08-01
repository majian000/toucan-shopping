import request from '@/utils/request'

export function addOrgnazition(data) {
  return request({ url: '/orgnazition/save', method: 'post', data })
}

export function updateOrgnazition(data) {
  return request({ url: '/orgnazition/update', method: 'post', data })
}

export function delOrgnazition(id) {
  return request({ url: '/orgnazition/delete', method: 'post', data: { id } })
}

export function delBatchOrgnazition(data) {
  return request({ url: '/orgnazition/delete/ids', method: 'post', data })
}

// 懒加载：根据父ID获取组织树
export function listOrgnazitionByPid(data) {
  return request({ url: '/orgnazition/tree/table', method: 'post', data })
}

// 获取完整组织树
export function listOrgnazitionTree() {
  return request({ url: '/orgnazition/query/tree', method: 'post', data: {} })
}
