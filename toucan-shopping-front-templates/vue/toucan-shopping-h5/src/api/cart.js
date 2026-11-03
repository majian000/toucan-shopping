import request from '@/utils/request';

/** 购物车列表 */
export function cartList() {
  return request({ url: '/h5/user/buyCar/list', method: 'post' });
}

/** 加入购物车 */
export function saveCart(data) {
  return request({ url: '/h5/user/buyCar/save', method: 'post', data });
}

/** 删除购物车项 */
export function removeCart(data) {
  return request({ url: '/h5/user/buyCar/remove', method: 'post', data });
}

/** 清空购物车 */
export function clearCart() {
  return request({ url: '/h5/user/buyCar/clear', method: 'post' });
}

/** 修改购物车项 */
export function updateCart(data) {
  return request({ url: '/h5/user/buyCar/update', method: 'post', data });
}
