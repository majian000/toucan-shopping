import request from '@/utils/request'

// 查询列表 (BannerPageInfo JSON body)
export function listBanner(data) {
  return request({ url: '/banner/list', method: 'post', data })
}

// 保存
export function saveBanner(data) {
  return request({ url: '/banner/save', method: 'post', data })
}

// 修改
export function updateBanner(data) {
  return request({ url: '/banner/update', method: 'post', data })
}

// 删除
export function deleteBanner(data) {
  return request({ url: '/banner/delete', method: 'post', data })
}

// 批量删除
export function deleteBanners(data) {
  return request({ url: '/banner/delete/ids', method: 'post', data })
}

// 上传图片
export function uploadBannerImg(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/banner/upload/img', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}

// 刷新PC首页缓存
export function flushIndexCache(data) {
  return request({ url: '/banner/flush/index/cache', method: 'post', data })
}

// 清空PC首页缓存
export function clearIndexCache() {
  return request({ url: '/banner/clear/index/cache', method: 'post' })
}

// 查询地区树
export function queryAreaTree(bannerId) {
  return request({ url: '/banner/query/area/tree', method: 'post', params: { bannerId } })
}
