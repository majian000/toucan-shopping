import request from '@/utils/request'

export function getSummary(data) {
  return request({ url: '/apiMonitor/summary', method: 'post', data })
}

export function getRequestLog(data) {
  return request({ url: '/apiMonitor/requestLog', method: 'post', data })
}
