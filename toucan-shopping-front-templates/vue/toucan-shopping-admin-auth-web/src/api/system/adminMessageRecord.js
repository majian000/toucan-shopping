import request from '@/utils/request'

export function listMessageRecord(data) { return request({ url: '/system/adminMessageRecord/list', method: 'post', data }) }
export function delMessageRecord(id) { return request({ url: '/system/adminMessageRecord/delete', method: 'post', data: { id } }) }
export function myMessageList(data) { return request({ url: '/system/adminMessageRecord/myList', method: 'post', data: data || {} }) }
export function countUnreadMsg() { return request({ url: '/system/adminMessageRecord/countUnread', method: 'post' }) }
export function markMsgRead(id) { return request({ url: '/system/adminMessageRecord/markRead', method: 'post', data: { id } }) }
export function markMsgAllRead() { return request({ url: '/system/adminMessageRecord/markAllRead', method: 'post' }) }
