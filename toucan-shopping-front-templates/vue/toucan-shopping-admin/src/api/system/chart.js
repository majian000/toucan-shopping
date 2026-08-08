import request from '@/utils/request'

// 在线用户统计（柱状图）
export function queryOnlineUserChart() {
  return request({ url: '/online/user/chart/queryAppLoginUserCountList', method: 'post', data: {} })
}

// 操作统计（折线图）
export function queryOperateChart() {
  return request({ url: '/operate/log/chart/queryOperateChart', method: 'post', data: {} })
}
