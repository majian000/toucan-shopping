import request from '@/utils/request'

export function addOrgnazition(data) {
  return request({ url: '/system/orgnazition/save', method: 'post', data })
}

export function updateOrgnazition(data) {
  return request({ url: '/system/orgnazition/update', method: 'post', data })
}

export function delOrgnazition(id) {
  return request({ url: '/system/orgnazition/delete', method: 'post', data: { id } })
}

// 懒加载：根据父ID获取组织树
export function listOrgnazitionByPid(data) {
  return request({ url: '/orgnazition/tree/table', method: 'post', data })
}

// 获取完整组织树（表格 + 上级机构下拉框共用）
export function listOrgnazitionTree() {
  return request({ url: '/system/orgnazition/query/tree', method: 'post', data: {} })
}
