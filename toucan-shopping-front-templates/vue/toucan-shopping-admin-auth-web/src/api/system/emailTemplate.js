import request from '@/utils/request'

export function listEmailTemplate(data) {
  return request({ url: '/system/emailTemplate/list', method: 'post', data })
}

export function addEmailTemplate(data) {
  return request({ url: '/system/emailTemplate/save', method: 'post', data })
}

export function updateEmailTemplate(data) {
  return request({ url: '/system/emailTemplate/update', method: 'post', data })
}

export function delEmailTemplate(id) {
  return request({ url: '/system/emailTemplate/delete', method: 'post', data: { id } })
}

export function refreshEmailTemplateCache() {
  return request({ url: '/system/emailTemplate/refreshCache', method: 'post', data: {} })
}
