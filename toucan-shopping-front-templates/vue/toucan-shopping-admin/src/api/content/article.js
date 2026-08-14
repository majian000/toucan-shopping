import request from '@/utils/request'
import axios from 'axios'
import { getToken } from '@/utils/auth'

// 查询文章列表 (ArticlePageInfo JSON body)
export function listArticle(data) {
  return request({ url: '/article/list', method: 'post', data })
}

// 查询栏目树（用于左侧栏目选择）
export function queryColumnTreeByPid(id) {
  return request({ url: '/article/query/column/tree/pid', method: 'post', params: { id } })
}

// 保存 (ArticleVO as JSON body)
export function saveArticle(data) {
  return request({ url: '/article/save', method: 'post', data })
}

// 修改 (ArticleVO as JSON body)
export function updateArticle(data) {
  return request({ url: '/article/update', method: 'post', data })
}

// 根据ID查询（回显用，含base64图片数据）
export function queryArticleById(data) {
  return request({ url: '/article/queryById', method: 'post', data })
}

// 查看详情（返回 ArticleDetailVO）
export function queryArticleDetail(data) {
  return request({ url: '/article/detail', method: 'post', data })
}

// 删除 (ArticleVO with id as JSON body)
export function deleteArticle(data) {
  return request({ url: '/article/delete', method: 'post', data })
}

// 批量删除 (List<ArticleVO> as JSON body)
export function deleteArticles(data) {
  return request({ url: '/article/delete/ids', method: 'post', data })
}

// 上传文章图片（富文本/封面通用），返回完整图片地址
// 后端 /article/upload/img 返回的 ResultObjectVO 走的是 code=0 的旧约定，
// 会与 request 拦截器的 success 判断冲突，故这里用原生 axios 绕过拦截器。
export async function uploadArticleImg(file) {
  const formData = new FormData()
  formData.append('file', file)
  const res = await axios.post(import.meta.env.VITE_APP_BASE_API + '/article/upload/img', formData, {
    headers: { Authorization: 'Bearer ' + getToken() }
  })
  const data = res.data
  if (data && data.data && data.data.httpCoverImgUrl) {
    return data.data.httpCoverImgUrl
  }
  throw new Error((data && data.msg) || '图片上传失败')
}
