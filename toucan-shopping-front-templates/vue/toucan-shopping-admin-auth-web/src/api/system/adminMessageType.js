import request from '@/utils/request'

export function listAdminMessageType(data) { return request({ url: '/system/adminMessageType/list', method: 'post', data }) }
export function listAllAdminMessageType() { return request({ url: '/system/adminMessageType/listAll', method: 'post' }) }
export function addAdminMessageType(data) { return request({ url: '/system/adminMessageType/save', method: 'post', data }) }
export function updateAdminMessageType(data) { return request({ url: '/system/adminMessageType/update', method: 'post', data }) }
export function delAdminMessageType(id) { return request({ url: '/system/adminMessageType/delete', method: 'post', data: { id } }) }
