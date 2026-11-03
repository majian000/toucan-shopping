import request from '@/utils/request';

/** 商品详情（按 SKU id） */
export function getProductDetail(data) {
  return request({ url: '/h5/product/detail', method: 'post', data });
}
