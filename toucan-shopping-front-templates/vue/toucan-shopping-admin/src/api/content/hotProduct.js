import request from '@/utils/request'

// 查询列表 (HotProductPageInfo JSON body)
export function listHotProduct(data) {
  return request({ url: '/hotProduct/list', method: 'post', data })
}

// 保存
export function saveHotProduct(data) {
  return request({ url: '/hotProduct/save', method: 'post', data })
}

// 修改
export function updateHotProduct(data) {
  return request({ url: '/hotProduct/update', method: 'post', data })
}

// 查看详情
export function detailHotProduct(data) {
  return request({ url: '/hotProduct/detail', method: 'post', data })
}

// 删除
export function deleteHotProduct(data) {
  return request({ url: '/hotProduct/delete', method: 'post', data })
}

// 批量删除
export function deleteHotProducts(data) {
  return request({ url: '/hotProduct/delete/ids', method: 'post', data })
}

// 上传图片
export function uploadHotProductImg(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/hotProduct/upload/img', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}
