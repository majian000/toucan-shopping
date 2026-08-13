import request from '@/utils/request'

// 查询文章图片列表 (ArticleImagePageInfo @RequestBody, JSON body)
export function listArticleImage(data) {
  return request({ url: '/articleImage/list', method: 'post', data })
}

// 删除 (ArticleImage JSON body)
export function deleteArticleImage(data) {
  return request({ url: '/articleImage/delete', method: 'post', data })
}

// 批量删除 (List<ArticleImageVO> JSON body)
export function deleteArticleImages(data) {
  return request({ url: '/articleImage/delete/ids', method: 'post', data })
}
