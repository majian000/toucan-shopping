import request from '@/utils/request'

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

// 删除 (ArticleVO with id as JSON body)
export function deleteArticle(data) {
  return request({ url: '/article/delete', method: 'post', data })
}

// 批量删除 (List<ArticleVO> as JSON body)
export function deleteArticles(data) {
  return request({ url: '/article/delete/ids', method: 'post', data })
}

// 上传封面图片
export function uploadArticleImg(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: '/article/upload/img', method: 'post', data: formData, headers: { 'Content-Type': 'multipart/form-data' } })
}
