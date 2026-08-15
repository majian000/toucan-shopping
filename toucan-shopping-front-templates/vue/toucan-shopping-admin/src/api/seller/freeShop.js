import request from '@/utils/request'

// 生成预览
export function generatePreview() {
  return request({ url: '/seller/web/freeShop/html/generate/preview', method: 'post' })
}

// 生成最终版
export function generateRelease() {
  return request({ url: '/seller/web/freeShop/html/generate/release', method: 'post' })
}

// 查询免费开店页静态文件选项卡
export function queryTab() {
  return request({ url: '/seller/web/freeShop/html/query/tab', method: 'post' })
}
