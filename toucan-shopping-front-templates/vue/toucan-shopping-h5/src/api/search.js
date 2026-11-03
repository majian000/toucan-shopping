import request from '@/utils/request';

/** 商品搜索 */
export function searchProduct(data) {
  return request({ url: '/h5/product/search', method: 'post', data });
}
