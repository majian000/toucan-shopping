import request from '@/utils/request'

// 查询订单列表 (OrderPageInfo @RequestBody, JSON body; 分页字段 page/limit)
export function listOrder(data) {
  return request({ url: '/order/list', method: 'post', data })
}

// 修改订单 (OrderVO JSON body)
export function updateOrder(data) {
  return request({ url: '/order/update', method: 'post', data })
}

// 取消订单 (OrderVO JSON body: orderNo + cancelRemark)
export function cancelOrder(data) {
  return request({ url: '/order/cancel', method: 'post', data })
}

// 查询订单项分页列表 (OrderItemPageInfo JSON body)
export function listOrderItem(data) {
  return request({ url: '/order/orderItem/list', method: 'post', data })
}

// 查询订单下全部订单项 (OrderItemPageInfo JSON body)
export function listOrderItemAll(data) {
  return request({ url: '/order/orderItem/all/list', method: 'post', data })
}

// 修改订单项(从订单列表) (List<OrderItemVO> JSON body)
export function updatesOrderItems(data) {
  return request({ url: '/order/orderItem/updatesFromOrderList', method: 'post', data })
}

// 查询订单日志 (OrderLogPageInfo JSON body)
export function listOrderLog(data) {
  return request({ url: '/order/orderLog/list', method: 'post', data })
}

// 按 parentCode 查询地区列表 (AreaVO JSON body)
export function listAreaByParentCode(data) {
  return request({ url: '/area/list/by/parentCode', method: 'post', data })
}
