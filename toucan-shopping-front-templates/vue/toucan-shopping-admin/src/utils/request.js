import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'

// 防止登录过期时重复提示和跳转
let isRelogin = false

// 错误码映射
const errorCode = {
  '401': '认证失败，无法访问系统资源',
  '403': '当前操作没有权限',
  '404': '访问资源不存在',
  'default': '系统未知错误，请反馈给管理员'
}

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 30000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  res => {
    const data = res.data
    const code = data.code
    const msg = errorCode[code] || data.msg || errorCode['default']

    // ResultObjectVO 有 success 字段，优先用它判断（SUCCESS=1, FAILD=0）
    // TableVO 没有 success 字段，用 code 判断（SUCCESS=0, FAILD=1）
    if ('success' in data) {
      if (data.success === true) return data
    } else if (code === 0 || code === 1) {
      return data
    }

    if (code === 401) {
      if (!isRelogin) {
        isRelogin = true
        ElMessage({ message: '登录状态已过期，请重新登录', type: 'error', duration: 2000 })
        removeToken()
        location.href = '/#/login'
      }
      return Promise.reject(msg)
    } else if (code === 500) {
      ElMessage({ message: msg, type: 'error', duration: 2000 })
      return Promise.reject(new Error(msg))
    } else {
      ElMessage({ message: msg, type: 'error', duration: 2000 })
      return Promise.reject(msg)
    }
  },
  error => {
    // 登录超时(401): 清除token跳转登录页
    if (error.response && error.response.status === 401) {
      if (!isRelogin) {
        isRelogin = true
        ElMessage({ message: '登录状态已过期，请重新登录', type: 'error', duration: 2000 })
        removeToken()
        location.href = '/#/login'
      }
      return Promise.reject(error)
    }
    // 没有权限(403): 提示后不跳转
    if (error.response && error.response.status === 403) {
      const data = error.response.data
      ElMessage({ message: data?.msg || '没有权限访问', type: 'warning', duration: 3000 })
      return Promise.reject(data?.msg || '没有权限访问')
    }
    let { message } = error
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    }
    ElMessage({ message, type: 'error', duration: 2000 })
    return Promise.reject(error)
  }
)

export default service
