import request from '@/utils/request'

// 查询列表 (AttributeValuePageInfo @RequestBody, JSON body)
export function listAttributeValue(data) {
  return request({ url: '/product/attribute/attributeValue/list', method: 'post', data })
}

// 保存 (AttributeValueVO @RequestBody -> data)
export function saveAttributeValue(data) {
  return request({ url: '/product/attribute/attributeValue/save', method: 'post', data })
}

// 修改 (AttributeValueVO @RequestBody -> data)
export function updateAttributeValue(data) {
  return request({ url: '/product/attribute/attributeValue/update', method: 'post', data })
}

// 删除 (路径参数)
export function deleteAttributeValue(id) {
  return request({ url: `/product/attribute/attributeValue/delete/${id}`, method: 'delete' })
}

// 批量删除 (List @RequestBody -> data)
export function deleteAttributeValueByIds(data) {
  return request({ url: '/product/attribute/attributeValue/delete/ids', method: 'delete', data })
}
