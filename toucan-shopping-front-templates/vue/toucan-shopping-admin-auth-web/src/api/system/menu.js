import request from '@/utils/request'

export function getMenu(menuId) {
  return request({ url: '/system/menu/' + menuId, method: 'get' })
}

export function addMenu(data) {
  return request({ url: '/system/function/save', method: 'post', data })
}

export function updateMenu(data) {
  return request({ url: '/system/function/update', method: 'post', data })
}

export function delMenu(id, functionId) {
  return request({ url: '/system/function/delete', method: 'post', data: { id, functionId } })
}

// 懒加载：根据父ID获取菜单树（pid为空则获取根节点/全量树）
export function listMenuByPid(data) {
  return request({ url: '/system/function/tree/table/by/pid', method: 'post', data })
}

// 获取完整菜单树
export function listMenuTree() {
  return request({ url: '/system/function/tree/table', method: 'post', data: {} })
}
