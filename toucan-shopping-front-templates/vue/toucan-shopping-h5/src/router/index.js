import { createRouter, createWebHashHistory } from 'vue-router';
import ENV from '@/utils/env';
import { isLogin } from '@/utils/auth';

/**
 * 路由：统一用 hash 模式（vue-router 4）
 * H5 挂在 nginx 子路径 /h5/ 下，hash 模式服务端只需一个 location + index.html，刷新不 404。
 */
const routes = [
  {
    // 首页（游客可浏览）
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '首页', public: true }
  },
  {
    // 登录：/h5/#/login?redirect=<登录后要回的页面>
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', public: true }
  },
  {
    // 注册：/h5/#/register
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册', public: true }
  },
  {
    // 分类导航：/h5/#/category
    path: '/category',
    name: 'Category',
    component: () => import('@/views/category/index.vue'),
    meta: { title: '分类', public: true }
  },
  {
    // 商品详情：/h5/#/product/detail?id=xxx
    path: '/product/detail',
    name: 'ProductDetail',
    component: () => import('@/views/product/detail.vue'),
    meta: { title: '商品详情', public: true }
  },
  {
    // 搜索：/h5/#/search
    path: '/search',
    name: 'Search',
    component: () => import('@/views/search/index.vue'),
    meta: { title: '搜索', public: true }
  },
  {
    // 我的（公开：未登录时页面内显示登录引导）
    path: '/my',
    name: 'My',
    component: () => import('@/views/my/index.vue'),
    meta: { title: '我的', public: true }
  },
  {
    // 收藏（需登录）
    path: '/collect',
    name: 'Collect',
    component: () => import('@/views/collect/index.vue'),
    meta: { title: '我的收藏' }
  },
  {
    // 消息（需登录）
    path: '/message',
    name: 'Message',
    component: () => import('@/views/message/index.vue'),
    meta: { title: '我的消息' }
  },
  {
    // 收货地址（需登录）
    path: '/address',
    name: 'Address',
    component: () => import('@/views/address/index.vue'),
    meta: { title: '收货地址' }
  },
  {
    // 购物车（需登录）
    path: '/cart',
    name: 'Cart',
    component: () => import('@/views/cart/index.vue'),
    meta: { title: '购物车' }
  },
  {
    // 订单（需登录）
    path: '/order',
    name: 'Order',
    component: () => import('@/views/order/index.vue'),
    meta: { title: '我的订单' }
  },
  {
    // 店铺：/h5/#/shop?id=xxx
    path: '/shop',
    name: 'Shop',
    component: () => import('@/views/shop/index.vue'),
    meta: { title: '店铺', public: true }
  },
  {
    // 确认订单（需登录）
    path: '/checkout',
    name: 'Checkout',
    component: () => import('@/views/checkout/index.vue'),
    meta: { title: '确认订单' }
  },
  // 兜底：未知路径回首页
  { path: '/:pathMatch(.*)*', redirect: '/' }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
  scrollBehavior: () => ({ left: 0, top: 0 })
});

/**
 * 登录守卫：
 *   · meta.public === true 的页面（首页/登录/注册）直接放行；
 *   · 其余页面（购物车/订单/用户中心等，后续补充）没登录态 → 跳 H5 自己的登录页并带 redirect。
 * 注意：这里只判"本地有没有 h5_uid/h5_lt"，token 真伪由后端拦截器校验（401 兜底，见 utils/request.js）。
 */
router.beforeEach(to => {
  if (to.meta && to.meta.public === true) return true;
  if (isLogin()) return true;
  return { path: '/login', query: { redirect: to.fullPath } };
});

router.afterEach(to => {
  if (to.meta && to.meta.title) {
    document.title = to.meta.title + ' - ' + ENV.title;
  } else if (ENV.title) {
    document.title = ENV.title;
  }
});

export default router;
