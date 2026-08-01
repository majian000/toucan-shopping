import request from '@/utils/request'

export function listRole(data) {
  return request({ url: '/role/list', method: 'post', data })
}

export function getRole(roleId) {
  return request({ url: '/system/role/' + roleId, method: 'get' })
}

export function addRole(data) {
  return request({ url: '/role/save', method: 'post', data })
}

export function updateRole(data) {
  return request({ url: '/role/update', method: 'post', data })
}

export function delRole(id) {
  return request({ url: '/role/delete', method: 'post', data: { id } })
}

export function getRoleFunctionTree(roleId) {
  return request({ url: '/system/function/query/role/function/tree', method: 'post', data: { roleId } })
}

export function saveRoleFunctions(data) {
  return request({ url: '/system/role/save/functions', method: 'post', data })
}
