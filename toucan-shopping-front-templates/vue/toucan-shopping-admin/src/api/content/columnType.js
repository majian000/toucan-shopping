import request from '@/utils/request'

// 查询列表 (ColumnTypePageInfo 无 @RequestBody, 使用查询参数)
export function listColumnType(params) {
  return request({ url: '/column/columnType/list', method: 'post', params })
}

// 保存
export function saveColumnType(data) {
  return request({ url: '/column/columnType/save', method: 'post', data })
}

// 修改
export function updateColumnType(data) {
  return request({ url: '/column/columnType/update', method: 'post', data })
}

// 删除
export function deleteColumnType(data) {
  return request({ url: '/column/columnType/delete', method: 'post', data })
}
