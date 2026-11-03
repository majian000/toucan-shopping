import request from '@/utils/request';

// H5 独立会话：后端 /h5/** 前缀，鉴权读 h5_uid/h5_lt cookie

/** 登录 */
export function login(data) {
  return request({ url: '/h5/user/login/password', method: 'post', data });
}

/** 注册 */
export function regist(data) {
  return request({ url: '/h5/user/regist', method: 'post', data });
}

/** 退出登录 */
export function logout() {
  return request({ url: '/h5/user/logout', method: 'post' });
}

/** 登录信息（用户中心顶部展示用） */
export function getLoginInfo() {
  return request({ url: '/h5/user/login/info', method: 'post' });
}

/** 查询当前登录用户信息 */
export function getUserInfo() {
  return request({ url: '/h5/user/info', method: 'post' });
}

// ===== 收藏 =====

/** 收藏/取消收藏（type: 1 收藏，其他 取消） */
export function collect(data) {
  return request({ url: '/h5/user/collect/product/collect', method: 'post', data });
}

/** 是否已收藏 */
export function isCollect(data) {
  return request({ url: '/h5/user/collect/product/isCollect', method: 'post', data });
}

/** 收藏列表 */
export function collectList(data) {
  return request({ url: '/h5/user/collect/product/list', method: 'post', data });
}

// ===== 消息 =====

/** 消息列表 */
export function messageList(data) {
  return request({ url: '/h5/user/message/list', method: 'post', data });
}

/** 未读消息数 */
export function messageUnread() {
  return request({ url: '/h5/user/message/unread/count', method: 'post' });
}

/** 标记已读 */
export function messageRead(id) {
  return request({ url: '/h5/user/message/read/' + id, method: 'post' });
}

/** 全部已读 */
export function messageReadAll() {
  return request({ url: '/h5/user/message/read/all', method: 'post' });
}
