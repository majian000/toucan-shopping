import { defineStore } from 'pinia';

/**
 * 全局状态（Pinia）
 *
 * ⚠️ 登录态**不放这里**：统一走 cookie（utils/auth.js 的 h5_uid / h5_lt / h5_lat）。
 *    这里只放"页面内共享的展示态"，比如用户信息缓存。
 */
export const useAppStore = defineStore('app', {
  state: () => ({
    /** 用户信息缓存（来自 /h5/user/info 之类的接口） */
    userInfo: null
  }),
  actions: {
    setUserInfo(info) {
      this.userInfo = info || null;
    },
    reset() {
      this.userInfo = null;
    }
  }
});
