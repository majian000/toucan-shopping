import ENV from '@/utils/env';
import toast from '@/utils/toast';

/**
 * 全局插件：把环境与常用工具挂到 app.config.globalProperties（Vue 3 里的 Vue.prototype）
 *   this.$config  环境信息
 *   this.$toast   轻提示
 */
export default {
  install(app) {
    app.config.globalProperties.$config = {
      envName: ENV.name,
      title: ENV.title,
      apiBase: ENV.apiBase,
      publicPath: ENV.publicPath,
      isProd: ENV.isProd
    };
    app.config.globalProperties.$toast = toast;
  }
};
