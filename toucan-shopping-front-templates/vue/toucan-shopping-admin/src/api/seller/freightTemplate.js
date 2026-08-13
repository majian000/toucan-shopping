import request from '@/utils/request'

// 查询列表 (FreightTemplatePageInfo @RequestBody, JSON body)
export function listFreightTemplate(data) {
  return request({ url: '/freightTemplate/list', method: 'post', data })
}

// 查询详情
export function detailFreightTemplate(data) {
  return request({ url: '/freightTemplate/detail', method: 'post', data })
}

// 删除
export function deleteFreightTemplate(data) {
  return request({ url: '/freightTemplate/delete', method: 'post', data })
}
