import request from '@/utils/request'

export function listSmsTemplate(data) {
  return request({ url: '/system/smsTemplate/list', method: 'post', data })
}

export function addSmsTemplate(data) {
  return request({ url: '/system/smsTemplate/save', method: 'post', data })
}

export function updateSmsTemplate(data) {
  return request({ url: '/system/smsTemplate/update', method: 'post', data })
}

export function delSmsTemplate(id) {
  return request({ url: '/system/smsTemplate/delete', method: 'post', data: { id } })
}

export function refreshSmsTemplateCache() {
  return request({ url: '/system/smsTemplate/refreshCache', method: 'post', data: {} })
}
