import request from '@/utils/request'

// 查询支付流水列表 (OrderPayPageInfo @RequestBody, JSON body; 分页字段 page/limit)
export function listOrderPay(data) {
  return request({ url: '/order/orderPay/list', method: 'post', data })
}
