import request from '@/utils/request'

// 查询列表 (SellerDesignerPageModelPageInfo @RequestBody, JSON body)
export function listPageModel(data) {
  return request({ url: '/seller/designer/page/model/list', method: 'post', data })
}

// 删除
export function deletePageModel(data) {
  return request({ url: '/seller/designer/page/model/delete', method: 'post', data })
}
