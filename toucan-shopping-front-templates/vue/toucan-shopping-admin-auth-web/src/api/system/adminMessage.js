import request from '@/utils/request'

export function listAdminMessage(data) { return request({ url: '/system/adminMessage/list', method: 'post', data }) }
export function addAdminMessage(data) { return request({ url: '/system/adminMessage/save', method: 'post', data }) }
export function updateAdminMessage(data) { return request({ url: '/system/adminMessage/update', method: 'post', data }) }
export function delAdminMessage(id) { return request({ url: '/system/adminMessage/delete', method: 'post', data: { id } }) }
export function sendAdminMessage(data) { return request({ url: '/system/adminMessage/send', method: 'post', data }) }
