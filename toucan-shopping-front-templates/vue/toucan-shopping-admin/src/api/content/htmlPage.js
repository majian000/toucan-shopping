import request from '@/utils/request'

// 生成预览
export function generatePreview() {
  return request({ url: '/index/html/generate/preview', method: 'post' })
}

// 生成最终版
export function generateRelease() {
  return request({ url: '/index/html/generate/release', method: 'post' })
}
