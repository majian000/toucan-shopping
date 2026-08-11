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

export function listMessageUser(data) {
  return request({
    url: '/message/messageUser/list',
    method: 'post',
    data: toFormData(data),
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
}

export function sendMessage(data) {
  return request({ url: '/message/messageUser/save', method: 'post', data })
}

export function updateMessage(data) {
  return request({ url: '/message/messageUser/update', method: 'post', data })
}

export function delMessage(id) {
  return request({ url: `/message/messageUser/delete/${id}`, method: 'delete' })
}

// 获取全部消息类型（用于下拉选项）
export function listAllMessageType() {
  return request({
    url: '/message/messageType/list',
    method: 'post',
    data: toFormData({ page: 1, limit: 1000 }),
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
}
