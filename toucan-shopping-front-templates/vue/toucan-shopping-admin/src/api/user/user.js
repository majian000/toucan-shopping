import request from '@/utils/request'

// 查询用户列表 (UserPageInfo @RequestBody -> JSON body)
// 该接口查询量大、后端响应较慢，单独调大超时时间(5分钟)，避免走全局默认的30秒超时
export function listUser(data) {
  return request({ url: '/user/list', method: 'post', data, timeout: 300000 })
}

// 查看用户详情 (含base64头像/证件照,用于查看与编辑回显)
export function detailUser(data) {
  return request({ url: '/user/detail', method: 'post', data })
}

// 注册用户 (UserRegistVO @RequestBody -> JSON body)
export function regist(data) {
  return request({ url: '/user/regist', method: 'post', data })
}

// 修改用户详情 (UserRegistVO @RequestBody -> JSON body)
export function updateDetail(data) {
  return request({ url: '/user/update/detail', method: 'post', data })
}

// 重置密码 (UserRegistVO @RequestBody -> JSON body)
export function resetPassword(data) {
  return request({ url: '/user/reset/password', method: 'post', data })
}

// 关联手机号 (UserRegistVO @RequestBody -> JSON body)
export function connectMobilePhone(data) {
  return request({ url: '/user/connect/mobile/phone', method: 'post', data })
}

// 关联邮箱 (UserRegistVO @RequestBody -> JSON body)
export function connectEmail(data) {
  return request({ url: '/user/connect/email', method: 'post', data })
}

// 关联用户名 (UserRegistVO @RequestBody -> JSON body)
export function connectUsername(data) {
  return request({ url: '/user/connect/username', method: 'post', data })
}

// 启用/停用用户 (id 为 userMainId)
export function disabledEnabled(userMainId) {
  return request({ url: `/user/disabled/enabled/${userMainId}`, method: 'delete' })
}

// 批量禁用 (List<UserVO> @RequestBody -> JSON body)
export function disabledByIds(data) {
  return request({ url: '/user/disabled/ids', method: 'delete', data })
}

// 刷新缓存
export function flushCache(userMainId) {
  return request({ url: `/user/flush/cache/${userMainId}`, method: 'post' })
}

// 手机号列表 (UserPageInfo @RequestBody -> JSON body)
export function listMobilePhone(data) {
  return request({ url: '/user/mobile/phone/list', method: 'post', data })
}

// 邮箱列表 (UserPageInfo @RequestBody -> JSON body)
export function listEmail(data) {
  return request({ url: '/user/email/list', method: 'post', data })
}

// 用户名列表 (UserPageInfo @RequestBody -> JSON body)
export function listUsername(data) {
  return request({ url: '/user/username/list', method: 'post', data })
}

// 手机号 启用/停用 (UserMobilePhoneVO @RequestBody -> JSON body)
export function disabledEnabledMobilePhone(data) {
  return request({ url: '/user/mobile/phone/disabled/enabled', method: 'post', data })
}

// 邮箱 启用/停用 (UserEmailVO @RequestBody -> JSON body)
export function disabledEnabledEmail(data) {
  return request({ url: '/user/email/disabled/enabled', method: 'post', data })
}

// 用户名 启用/停用 (UserUserNameVO @RequestBody -> JSON body)
export function disabledEnabledUsername(data) {
  return request({ url: '/user/username/disabled/enabled', method: 'post', data })
}
