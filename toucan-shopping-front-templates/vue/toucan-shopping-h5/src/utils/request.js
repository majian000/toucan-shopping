import axios from 'axios';
import ENV from '@/utils/env';
import { clearLogin } from '@/utils/auth';
import errorCode from '@/utils/errorCode';
import { tansParams } from '@/utils/params';
import { toast } from '@/utils/toast';

/**
 * axios 封装：与 admin 的 request.js 同一口径，但：
 *   · 独立会话靠浏览器自动携带 h5_uid / h5_lt / h5_lat cookie（withCredentials），不手动塞 Authorization；
 *   · 后端 ResultObjectVO（success 字段，SUCCESS=1 / FAILD=0）与 TableVO（code 0/1）兼容；
 *   · 失败统一走 utils/toast.js 轻提示（H5 不引 element-ui）。
 */
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';

const service = axios.create({
  baseURL: ENV.apiBase,
  timeout: 60000,
  // 独立会话：靠浏览器自动携带 h5_uid/h5_lt/h5_lat cookie
  withCredentials: true
});

let isRelogin = false;

service.interceptors.request.use(
  config => {
    // multipart：删掉全局的 application/json，让浏览器自己生成 boundary
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type'];
    }
    // GET 不缓存 + 把 params 拼进 URL（与 PC 端一致）
    if (config.method === 'get') {
      config.headers['Cache-Control'] = 'no-cache';
      config.headers['Pragma'] = 'no-cache';
    }
    if (config.method === 'get' && config.params) {
      let url = config.url + '?' + tansParams(config.params);
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }
    return config;
  },
  error => {
    console.error('[h5] 请求错误', error);
    return Promise.reject(error);
  }
);

function relogin() {
  if (isRelogin) return;
  isRelogin = true;
  clearLogin();
  toast('登录状态已过期，请重新登录', 'error', 2000);
  const from = window.location.hash ? window.location.hash.replace(/^#/, '') : '/';
  window.location.href = ENV.publicPath + '#/login?redirect=' + encodeURIComponent(from);
}

service.interceptors.response.use(
  res => {
    const data = res.data;
    const code = data.code;
    const msg = errorCode[code] || data.msg || errorCode['default'];

    // ResultObjectVO 有 success 字段，优先用它判断（SUCCESS=1, FAILD=0）
    // TableVO 没有 success 字段，用 code 判断（SUCCESS=0, FAILD=1）
    if ('success' in data) {
      if (data.success === true) return data;
    } else if (code === 0 || code === 1) {
      return data;
    }

    if (code === 401) {
      relogin();
      return Promise.reject(msg);
    } else if (code === 500) {
      toast(msg, 'error', 2000);
      return Promise.reject(new Error(msg));
    } else {
      toast(msg, 'error', 2000);
      return Promise.reject(msg);
    }
  },
  error => {
    // 登录超时(401)：清登录态跳登录页
    if (error.response && error.response.status === 401) {
      relogin();
      return Promise.reject(error);
    }
    // 没有权限(403)：提示后不跳转
    if (error.response && error.response.status === 403) {
      const data = error.response.data;
      toast(data && data.msg ? data.msg : '没有权限访问', 'warning', 3000);
      return Promise.reject(data && data.msg ? data.msg : '没有权限访问');
    }
    let { message } = error;
    if (message === 'Network Error') {
      message = '后端接口连接异常';
    } else if (message && message.includes('timeout')) {
      message = '系统接口请求超时';
    }
    toast(message, 'error', 2000);
    return Promise.reject(error);
  }
);

export default service;
