import request from '@/utils/request'

// 查询栏目树表格
export function queryTreeTable(data) {
  return request({ url: '/column/tree/table/by/pid', method: 'post', data })
}

// 查询栏目类型树
export function queryColumnTypeList() {
  return request({ url: '/column/query/type/list', method: 'post' })
}

// 查询栏目字典（栏目类型、栏目位置）
export function queryColumnDict() {
  return request({ url: '/column/query/dict', method: 'post' })
}

// 查询栏目树（选择上级栏目用）—— 后端未加 @RequestBody，用 query 参数
export function queryColumnTree(params) {
  return request({ url: '/column/query/column/tree', method: 'post', params })
}

// 查询栏目详情（含栏目类型名称、上级标题）
export function queryColumnById(data) {
  return request({ url: '/column/queryById', method: 'post', data })
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
