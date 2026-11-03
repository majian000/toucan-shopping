import request from '@/utils/request';

/** 店铺轮播图 */
export function shopBanners(data) {
  return request({ url: '/h5/shop/index/banners', method: 'post', data });
}

/** 店铺分类 */
export function shopCategorys(data) {
  return request({ url: '/h5/shop/index/categorys', method: 'post', data });
}
