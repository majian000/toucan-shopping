import request from '@/utils/request'

// 查询树表格 (按父ID懒加载, AreaTreeInfo JSON body)
export function queryAreaTreeTable(data) {
  return request({ url: '/area/tree/table/by/pid', method: 'post', data })
}

// 查询树
export function queryAreaTree() {
  return request({ url: '/area/query/tree', method: 'post' })
}

// 保存
export function saveArea(data) {
  return request({ url: '/area/save', method: 'post', data })
}

// 修改
export function updateArea(data) {
  return request({ url: '/area/update', method: 'post', data })
}

// 删除
export function deleteArea(data) {
  return request({ url: '/area/delete', method: 'post', data })
}

// 批量删除
export function deleteAreas(data) {
  return request({ url: '/area/delete/ids', method: 'post', data })
}

// 刷新全部缓存
export function flushAllAreaCache() {
  return request({ url: '/area/flush/all/cache', method: 'post' })
}
