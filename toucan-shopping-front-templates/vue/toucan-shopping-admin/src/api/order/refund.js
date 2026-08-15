import request from '@/utils/request'

// 查询退款流水列表 (OrderRefundPageInfo @RequestBody, JSON body; 分页字段 page/limit)
export function listOrderRefund(data) {
  return request({ url: '/order/orderRefund/list', method: 'post', data })
}
