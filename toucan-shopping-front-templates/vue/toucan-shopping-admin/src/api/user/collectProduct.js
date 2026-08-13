import request from '@/utils/request'

// 查询收藏商品列表 (UserCollectProductPageInfo @RequestBody -> JSON body)
export function listCollectProduct(data) {
  return request({ url: '/user/collect/product/list', method: 'post', data })
}

// 删除收藏商品
export function deleteCollectProduct(id) {
  return request({ url: `/user/collect/product/delete/${id}`, method: 'delete' })
}

// 批量删除收藏商品 (List<UserCollectProductVO> @RequestBody -> JSON body)
export function deleteCollectProducts(data) {
  return request({ url: '/user/collect/product/delete/ids', method: 'delete', data })
}
