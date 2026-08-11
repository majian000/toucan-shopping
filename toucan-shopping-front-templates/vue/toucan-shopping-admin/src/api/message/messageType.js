import request from '@/utils/request'

export function listMessageType(data) {
  return request({ url: '/message/messageType/list', method: 'post', data })
}

export function addMessageType(data) {
  return request({ url: '/message/messageType/save', method: 'post', data })
}

export function updateMessageType(data) {
  return request({ url: '/message/messageType/update', method: 'post', data })
}

export function delMessageType(id) {
  return request({ url: `/message/messageType/delete/${id}`, method: 'delete' })
}

export function flushMessageTypeCache() {
  return request({ url: '/message/messageType/flush/cache', method: 'post' })
}
