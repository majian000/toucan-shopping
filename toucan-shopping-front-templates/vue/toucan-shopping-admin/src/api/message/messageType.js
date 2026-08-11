import request from '@/utils/request'

// 将对象转为 URLSearchParams（用于 form-encoded POST，适配 list 接口）
function toFormData(obj) {
  const params = new URLSearchParams()
  Object.keys(obj).forEach(key => {
    if (obj[key] != null && obj[key] !== '') {
      params.append(key, obj[key])
    }
  })
  return params
}

export function listMessageType(data) {
  return request({
    url: '/message/messageType/list',
    method: 'post',
    data: toFormData(data),
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
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
