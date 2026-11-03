/**
 * 当前环境信息（Vite 编译期注入，见根目录 .env / .env.development / .env.test / .env.production）
 */
const ENV = {
  /** dev / test / prod */
  name: import.meta.env.VITE_APP_ENV_NAME || import.meta.env.MODE || 'dev',
  /** 页面标题 */
  title: import.meta.env.VITE_APP_TITLE || '商城',
  /** 接口前缀：/dev-api、/stage-api、/prod-api（由 nginx 同域反代到 H5 后端） */
  apiBase: import.meta.env.VITE_APP_BASE_API || '/dev-api',
  /** 部署子路径：/h5/ */
  publicPath: import.meta.env.VITE_APP_PUBLIC_PATH || '/',
  /** 是否生产构建 */
  isProd: !!import.meta.env.PROD
};

export default ENV;
