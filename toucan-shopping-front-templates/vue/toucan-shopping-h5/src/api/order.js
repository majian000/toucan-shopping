import request from '@/utils/request';

/** 订单列表 */
export function orderList(data) {
  return request({ url: '/h5/order/query/order/list', method: 'post', data });
}

/** 订单详情 */
export function orderDetail(data) {
  return request({ url: '/h5/order/detail', method: 'post', data });
}

/** 下单 */
export function createOrder(data) {
  return request({ url: '/h5/order/create', method: 'post', data });
}
