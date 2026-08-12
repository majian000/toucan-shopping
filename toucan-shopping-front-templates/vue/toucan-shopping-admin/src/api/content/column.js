import request from '@/utils/request'

// 查询栏目树表格
export function queryTreeTable(data) {
  return request({ url: '/column/tree/table/by/pid', method: 'post', data })
}

// 查询栏目类型树
export function queryColumnTypeList() {
  return request({ url: '/column/query/type/list', method: 'post' })
}

// 查询栏目树（选择上级栏目用）
export function queryColumnTree(data) {
  return request({ url: '/column/query/column/tree', method: 'post', data })
}

// 保存
export function saveColumn(data) {
  return request({ url: '/column/save', method: 'post', data })
}

// 修改
export function updateColumn(data) {
  return request({ url: '/column/update', method: 'post', data })
}

// 删除
export function deleteColumn(data) {
  return request({ url: '/column/delete', method: 'post', data })
}

// 批量删除
export function deleteColumns(data) {
  return request({ url: '/column/delete/ids', method: 'post', data })
}
