import request from '@/utils/request'

// 获取前端菜单
export function getMenus() {
  return request({ url: '/index/menus', method: 'get' })
}

// 获取路由菜单
export function getRouters() {
  return request({ url: '/getRouters', method: 'get' })
}
