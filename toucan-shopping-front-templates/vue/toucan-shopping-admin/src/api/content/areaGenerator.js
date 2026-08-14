import request from '@/utils/request'

// 生成最终版 (地区组件静态HTML, 发布到正式环境)
export function generateAreaRelease() {
  return request({ url: '/area/html/generate/release', method: 'post' })
}

// 查询地区组件静态文件选项卡
export function queryAreaTab() {
  return request({ url: '/area/html/query/tab', method: 'post' })
}
