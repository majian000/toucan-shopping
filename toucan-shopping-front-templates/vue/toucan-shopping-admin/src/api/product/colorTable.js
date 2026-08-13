import request from '@/utils/request'

// 查询列表 (ColorTablePageInfo @RequestBody, JSON body)
export function listColorTable(data) {
  return request({ url: '/colorTable/list', method: 'post', data })
}

// 保存 (ColorTableVO @RequestBody -> data)
export function saveColorTable(data) {
  return request({ url: '/colorTable/save', method: 'post', data })
}

// 修改 (ColorTableVO @RequestBody -> data)
export function updateColorTable(data) {
  return request({ url: '/colorTable/update', method: 'post', data })
}

// 删除 (Banner @RequestBody -> data)
export function deleteColorTable(data) {
  return request({ url: '/colorTable/delete', method: 'post', data })
}

// 批量删除 (List @RequestBody -> data)
export function deleteColorTableByIds(data) {
  return request({ url: '/colorTable/delete/ids', method: 'post', data })
}
