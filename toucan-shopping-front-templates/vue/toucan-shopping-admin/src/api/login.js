import request from '@/utils/request'

// 登录
export function login(username, password, vcode) {
  return request({
    url: '/login/submit',
    method: 'post',
    data: { username, password, vcode }
  })
}

// 退出
export function logout() {
  return request({
    url: '/logout/out',
    method: 'post'
  })
}

// 获取当前用户信息
export function getInfo() {
  return request({
    url: '/index/getInfo',
    method: 'get'
  })
}
