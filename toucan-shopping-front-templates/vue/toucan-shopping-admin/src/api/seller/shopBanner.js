import request from '@/utils/request'

// 查询列表 (ShopBannerPageInfo @RequestBody, JSON body)
export function listShopBanner(data) {
  return request({ url: '/seller/shopBanner/list', method: 'post', data })
}

// 修改 (REQUEST_FORM 表单, 含 bannerImgFile 文件, 使用 FormData)
export function updateShopBanner(data) {
  return request({ url: '/seller/shopBanner/update', method: 'post', data })
}

// 删除
export function deleteShopBanner(data) {
  return request({ url: '/seller/shopBanner/delete', method: 'post', data })
}

// 根据ID查询（编辑回显用，含 base64 图片数据）
export function queryShopBannerById(data) {
  return request({ url: '/seller/shopBanner/queryById', method: 'post', data })
}

// 查看详情 (返回 ShopBannerDetailVO: { basicInfo })
export function detailShopBanner(data) {
  return request({ url: '/seller/shopBanner/detail', method: 'post', data })
}
