import request from '@/utils/request'

// 获取我的信息
export function getMyInfo() {
  return request({ url: '/adminInfo/myInfo', method: 'post' })
}

// 保存我的信息
export function saveMyInfo(data) {
  return request({ url: '/adminInfo/saveOrUpdateMyInfo', method: 'post', data })
}
