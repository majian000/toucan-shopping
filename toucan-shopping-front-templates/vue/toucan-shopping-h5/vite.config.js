import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import { fileURLToPath, URL } from 'node:url';

/**
 * Vite 配置（Vue 3 + Vite 5）
 *
 * 参考 seal-h5 的配置口径，但插件/依赖版本与 toucan-shopping-admin 保持一致：
 *   base         → 部署子路径（由 VITE_APP_PUBLIC_PATH 决定，默认 /h5/）
 *   server.proxy → 本地联调把 VITE_APP_BASE_API 反代到 H5 后端
 *   scss         → 全局注入 variables.scss（无需手动 @import）
 *   build.target → es2015，照顾安卓微信/老 WebView
 */
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const publicPath = env.VITE_APP_PUBLIC_PATH || '/';
  const baseApi = env.VITE_APP_BASE_API;
  const proxyTarget = env.VITE_APP_PROXY_TARGET;

  return {
    base: publicPath,

    plugins: [vue()],

    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },

    css: {
      preprocessorOptions: {
        scss: {
          additionalData: '@use "@/styles/variables.scss" as *;'
        }
      }
    },

    server: {
      host: env.VITE_APP_DEV_HOST || '0.0.0.0',
      port: Number(env.VITE_APP_DEV_PORT) || 8301,
      proxy: baseApi
        ? {
            [baseApi]: {
              target: proxyTarget,
              changeOrigin: true,
              rewrite: path => path.replace(new RegExp('^' + baseApi), '')
            }
          }
        : {}
    },

    build: {
      outDir: 'dist',
      assetsDir: 'static',
      sourcemap: false,
      target: 'es2015',
      cssTarget: ['chrome61', 'safari12'],
      chunkSizeWarningLimit: 500
    }
  };
});
