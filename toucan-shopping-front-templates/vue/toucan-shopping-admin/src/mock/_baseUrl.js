/**
 * Mock 基础 URL 工具
 * 读取 VITE_APP_BASE_API 环境变量（如 /dev-api, /test-api, /prod-api）
 * mock 文件统一通过 regUrl() 生成匹配规则
 */

const BASE_API = process.env.VITE_APP_BASE_API || '/dev-api'

/**
 * 生成 mock 匹配的 url 字符串（精确匹配）
 * @param {string} path 接口路径，如 '/system/user/list'
 * @returns {string} 完整匹配路径，如 '/dev-api/system/user/list'
 */
export function apiUrl(path) {
  return BASE_API + path
}

/**
 * 生成 mock 匹配的正则表达式（用于带路径参数的接口）
 * @param {string} path 接口路径，如 '/system/user/'
 * @returns {RegExp}
 */
export function apiReg(path) {
  return new RegExp(BASE_API + path.replace(/\//g, '\\/'))
}
