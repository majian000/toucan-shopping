import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import { viteMockServe } from 'vite-plugin-mock'

function resolve(dir) {
  return path.join(__dirname, dir)
}

export default defineConfig(({ command, mode }) => {
  // 加载环境变量（VITE_ 前缀的变量会注入到 process.env）
  const env = loadEnv(mode, process.cwd(), '')

  // 代理前缀（默认为 /dev-api，通过 .env.* 文件中的 VITE_APP_BASE_API 配置）
  const baseApi = env.VITE_APP_BASE_API || '/dev-api'

  return {
    plugins: [
      vue(),
      // viteMockServe({
      //   mockPath: 'src/mock',
      //   localEnabled: command === 'serve',
      //   prodEnabled: false
      // })
    ],
    resolve: {
      alias: { '@': resolve('src') }
    },
    server: {
      host: env.VITE_DEV_HOST || '0.0.0.0',
      port: 8198,
      open: true,
      proxy: {
        [baseApi]: {
          target: env.VITE_PROXY_TARGET,
          changeOrigin: true,
          rewrite: (path) => path.replace(new RegExp('^' + baseApi), '')
        }
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `@use "@/styles/variables.scss" as *;`
        }
      }
    }
  }
})
