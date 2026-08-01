import request from '@/utils/request'

export function listPost(data) {
  return request({ url: '/system/post/list', method: 'post', data })
}

export function addPost(data) {
  return request({ url: '/system/post/save', method: 'post', data })
}

export function updatePost(data) {
  return request({ url: '/system/post/update', method: 'post', data })
}

export function delPost(id) {
  return request({ url: '/system/post/delete', method: 'post', data: { id } })
}
