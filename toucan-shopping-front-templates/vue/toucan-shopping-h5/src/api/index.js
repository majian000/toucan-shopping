import request from '@/utils/request';

/** 首页轮播图 */
export function getBanners() {
  return request({ url: '/h5/index/banners', method: 'post' });
}

/** 首页分类列表 */
export function getCategorys() {
  return request({ url: '/h5/index/categorys', method: 'post' });
}

/** 分类导航列表 */
export function getNavigation() {
  return request({ url: '/h5/index/navigation', method: 'post' });
}

/** 首页热门商品 */
export function getHotProducts() {
  return request({ url: '/h5/index/hot_product', method: 'post' });
}
