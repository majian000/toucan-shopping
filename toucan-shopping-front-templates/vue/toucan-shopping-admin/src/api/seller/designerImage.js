import request from '@/utils/request'

// 查询列表 (SellerDesignerImagePageInfo @RequestBody, JSON body)
export function listDesignerImage(data) {
  return request({ url: '/seller/designer/image/list', method: 'post', data })
}

// 修改 (REQUEST_FORM 表单, 含 bannerImgFile 文件, 使用 FormData)
export function updateDesignerImage(data) {
  return request({ url: '/seller/designer/image/update', method: 'post', data })
}

// 删除
export function deleteDesignerImage(data) {
  return request({ url: '/seller/designer/image/delete', method: 'post', data })
}
