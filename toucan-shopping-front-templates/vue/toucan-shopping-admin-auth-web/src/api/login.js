import request from '@/utils/request'

// 登录
export function login(username, password, code, uuid) {
  return request({
    url: '/login/submit',
    method: 'post',
    data: { username, password, code, uuid }
  })
}

// 退出
export function logout() {
  return request({
    url: '/logout/out',
    method: 'post'
  })
}

// 获取用户信息
export function getInfo() {
  return request({
    url: '/index/getInfo',
    method: 'get'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}
