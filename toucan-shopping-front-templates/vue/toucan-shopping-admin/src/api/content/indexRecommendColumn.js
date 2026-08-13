import request from '@/utils/request'

// 查询列表 (ColumnPageInfo @RequestBody, JSON body)
export function listIndexRecommendColumn(data) {
  return request({ url: '/column/indexRecommendColumn/list', method: 'post', data })
}

// 保存
export function saveIndexRecommendColumn(data) {
  return request({ url: '/column/indexRecommendColumn/save', method: 'post', data })
}

// 修改
export function updateIndexRecommendColumn(data) {
  return request({ url: '/column/indexRecommendColumn/update', method: 'post', data })
}

// 查询详情
export function findIndexRecommendColumn(data) {
  return request({ url: '/column/indexRecommendColumn/findById', method: 'post', data })
}

// 删除
export function deleteIndexRecommendColumn(data) {
  return request({ url: '/column/indexRecommendColumn/delete', method: 'post', data })
}

// 查询地区树
export function queryAreaTree(columnId) {
  return request({ url: '/column/indexRecommendColumn/query/area/tree', method: 'post', params: { columnId } })
}
