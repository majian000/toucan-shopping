import request from '@/utils/request'

export function listRole(data) {
  return request({ url: '/role/list', method: 'post', data })
}

export function getRole(roleId) {
  return request({ url: '/role/' + roleId, method: 'get' })
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

export function batchDelRole(ids) {
  return request({ url: '/role/delete/ids', method: 'post', data: ids.map(id => ({ id })) })
}

export function getRoleFunctionTree(roleId, appCode, pid) {
  return request({ url: '/function/query/role/function/tree', method: 'post', data: { roleId, appCode, pid: pid != null ? pid : -1 } })
}

export function saveRoleFunctions(data) {
  return request({ url: '/role/saveFunctions', method: 'post', data })
}

export function refreshRoleFunctionCache(roleId) {
  return request({ url: '/role/refresh/cache/functions', method: 'post', data: { roleId } })
}
