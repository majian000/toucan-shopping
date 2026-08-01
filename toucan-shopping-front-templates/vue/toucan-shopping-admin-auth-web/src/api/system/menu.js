import request from '@/utils/request'

// 树形表格: 根据父ID获取下级菜单
export function listMenuByPid(data) {
  return request({ url: '/function/tree/table/by/pid', method: 'post', data })
}

// 完整菜单树
export function listMenuTree() {
  return request({ url: '/function/tree/table', method: 'post', data: {} })
}

// 保存
export function addMenu(data) {
  return request({ url: '/function/save', method: 'post', data })
}

// 更新
export function updateMenu(data) {
  return request({ url: '/function/update', method: 'post', data })
}

// 删除
export function delMenu(id, functionId) {
  return request({ url: '/function/delete', method: 'post', data: { id, functionId } })
}

// 批量保存
export function batchAddMenus(data) {
  return request({ url: '/function/saves', method: 'post', data })
}
