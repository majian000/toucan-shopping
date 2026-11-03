import Cookies from 'js-cookie';

/**
 * H5 登录态：**独立于 PC web**（后端 H5 鉴权读 Cookie 里的 h5_uid / h5_lt / h5_lat）。
 *
 * 与 PC web 的 tss_uid / tss_lt / tss_lat 同名机制、不同 cookie 名：
 *   · PC web 登录只种 tss_*，H5 鉴权只认 h5_*，两边互不认；
 *   · 同一用户可在 PC 和 H5 各自登录，互不串号。
 */
const UidKey = 'h5_uid';
const TokenKey = 'h5_lt';
const AuthTokenKey = 'h5_lat';

export function getUid() {
  return Cookies.get(UidKey);
}

export function getToken() {
  return Cookies.get(TokenKey);
}

export function getAuthToken() {
  return Cookies.get(AuthTokenKey);
}

/** 登录成功后写入 cookie（path=/，与后端 Set-Cookie 口径一致） */
export function setLogin({ userMainId, loginToken, loginAuthToken }) {
  if (userMainId !== null && userMainId !== undefined) {
    Cookies.set(UidKey, String(userMainId), { path: '/' });
  }
  if (loginToken) {
    Cookies.set(TokenKey, loginToken, { path: '/' });
  }
  if (loginAuthToken) {
    Cookies.set(AuthTokenKey, loginAuthToken, { path: '/' });
  }
}

/** 是否已登录（只看本地有没有 h5_uid + h5_lt；真伪由后端拦截器校验） */
export function isLogin() {
  return !!(getUid() && getToken());
}

/** 清空登录态（退出登录 / 401 时调用） */
export function clearLogin() {
  Cookies.remove(UidKey, { path: '/' });
  Cookies.remove(TokenKey, { path: '/' });
  Cookies.remove(AuthTokenKey, { path: '/' });
}

/** 兼容命名（部分公共代码习惯用 removeToken） */
export function removeToken() {
  clearLogin();
}
