import request from '@/utils/request'

// 查询列表 (AttributeKeyPageInfo @RequestBody, JSON body)
export function listAttributeKey(data) {
  return request({ url: '/product/attribute/attributeKey/list', method: 'post', data })
}

// 树表格查询 (AttributeKeyPageInfo @RequestBody, JSON body)
export function queryTreeTableByPid(params) {
  return request({ url: '/product/attribute/attributeKey/tree/table/by/pid', method: 'post', params })
}

// 保存 (AttributeKeyVO @RequestBody -> data)
export function saveAttributeKey(data) {
  return request({ url: '/product/attribute/attributeKey/save', method: 'post', data })
}

// 修改 (AttributeKeyVO @RequestBody -> data)
export function updateAttributeKey(data) {
  return request({ url: '/product/attribute/attributeKey/update', method: 'post', data })
}

// 删除 (路径参数)
export function deleteAttributeKey(id) {
  return request({ url: `/product/attribute/attributeKey/delete/${id}`, method: 'delete' })
}

// 批量删除 (List @RequestBody -> data)
export function deleteAttributeKeyByIds(data) {
  return request({ url: '/product/attribute/attributeKey/delete/ids', method: 'delete', data })
}

// 分类树 (id @RequestParam -> params)
export function queryCategoryTreeByPid(params) {
  return request({ url: '/product/attribute/attributeKey/query/category/tree/pid', method: 'post', params })
}

// 按分类查询属性树 (categoryId, attributeType @RequestParam -> params)
export function queryTreeByCategoryId(params) {
  return request({ url: '/product/attribute/attributeKey/query/tree/category/id', method: 'post', params })
}
