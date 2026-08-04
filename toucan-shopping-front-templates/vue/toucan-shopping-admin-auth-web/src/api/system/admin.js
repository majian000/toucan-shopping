import request from '@/utils/request'

// 角色列表(下拉用)
export function roleList() {
  return request({ url: '/role/list', method: 'post' })
}

// 管理员列表
export function listAdmin(data) {
  return request({ url: '/admin/list', method: 'post', data })
}

// 保存
export function saveAdmin(data) {
  return request({ url: '/admin/save', method: 'post', data })
}

// 更新
export function updateAdmin(data) {
  return request({ url: '/admin/update', method: 'post', data })
}

// 删除
export function delAdmin(id) {
  return request({ url: '/admin/delete', method: 'post', data: { id } })
}

// 批量删除
export function batchDelAdmin(ids) {
  return request({ url: '/admin/delete/ids', method: 'post', data: ids.map(id => ({ id })) })
}

// 重置密码
export function resetAdminPwd(data) {
  return request({ url: '/admin/update/password', method: 'post', data })
}

// 修改自己的密码
export function updateMyPassword(data) {
  return request({ url: '/admin/update/mypassword', method: 'post', data })
}

// 关联角色
export function connectRoles(data) {
  return request({ url: '/admin/connect/roles', method: 'post', data })
}

// 关联机构
export function connectOrgs(data) {
  return request({ url: '/admin/connect/orgnazitions', method: 'post', data })
}

// 在线管理员列表
export function listOnlineAdmins() {
  return request({ url: '/admin/online/list', method: 'post' })
}

// 强制退出(id为AdminApp记录ID)
export function forceLogout(id) {
  return request({ url: '/admin/online/logout', method: 'post', data: { id } })
}

// 查询管理员的角色树（含选中状态）
export function queryAdminRoleTree(adminId) {
  return request({ url: '/role/query/admin/role/tree', method: 'post', data: { adminId } })
}

// 保存/更新管理员详细信息（完善信息）
export function saveAdminInfo(data) {
  return request({ url: '/adminInfo/saveOrUpdate', method: 'post', data })
}

// 应用列表
export function listApp(data) {
  return request({ url: '/app/list', method: 'post', data: data || {} })
}

// 获取完整组织树
export function listOrgnazitionTree() {
  return request({ url: '/orgnazition/query/tree', method: 'post', data: {} })
}

// 查询管理员的组织机构树（含选中状态）
export function queryAdminOrgnazitionTree(data) {
  return request({ url: '/orgnazition/query/admin/orgnazition/tree', method: 'post', data })
}

// 查询管理员详情
export function getAdminDetail(id) {
  return request({ url: '/admin/detail', method: 'post', data: { id } })
}

// 查询管理员关联的应用列表
export function listAdminApps(adminId) {
  return request({ url: '/admin/apps', method: 'post', data: { adminId } })
}
