import request from '@/utils/request'

export function listMessageUser(data) {
  return request({ url: '/message/messageUser/list', method: 'post', data })
}

export function sendMessage(data) {
  return request({ url: '/message/messageUser/save', method: 'post', data })
}

export function updateMessage(data) {
  return request({ url: '/message/messageUser/update', method: 'post', data })
}

export function delMessage(data) {
  return request({ url: '/message/messageUser/delete', method: 'post', data })
}

// 获取全部消息类型（用于下拉选项）
export function listAllMessageType() {
  return request({ url: '/message/messageType/list', method: 'post', data: { page: 1, limit: 1000 } })
}
