import request from '@/utils/request'

export function roleList() {
  return request({ url: '/system/admin/roleList', method: 'post' })
}

export function postList() {
  return request({ url: '/system/admin/postList', method: 'post' })
}

export function listAdmin(data) {
  return request({ url: '/system/admin/list', method: 'post',  data })
}

export function getAdmin(adminId) {
  return request({ url: '/system/admin/' + adminId, method: 'get' })
}

export function saveAdmin(data) {
  return request({ url: '/system/admin/save', method: 'post', data })
}

export function updateAdmin(data) {
  return request({ url: '/system/admin/update', method: 'post', data })
}

export function delAdmin(id) {
  return request({ url: '/system/admin/delete', method: 'post', data: { id } })
}

export function batchDelAdmin(ids) {
  return request({ url: '/system/admin/delete/ids', method: 'post', data: ids.map(id => ({ id })) })
}

export function resetAdminPwd(data) {
  return request({ url: '/system/admin/update/password', method: 'post', data })
}

export function changeAdminStatus(adminId, enableStatus) {
  return request({ url: '/system/admin/changeStatus', method: 'put', data: { adminId, enableStatus } })
}

export function updateMyPassword(data) {
  return request({ url: '/system/admin/update/mypassword', method: 'post', data })
}

export function listLockedAccounts() {
  return request({ url: '/system/admin/lockedList', method: 'post' })
}

export function unlockAccount(adminId) {
  return request({ url: '/system/admin/unlock', method: 'post', data: { adminId } })
}

export function forceLogout(adminId) {
  return request({ url: '/system/admin/forceLogout', method: 'post', data: { adminId } })
}

export function listOnlineAdmins() {
  return request({ url: '/system/admin/onlineList', method: 'post', data: {} })
}
