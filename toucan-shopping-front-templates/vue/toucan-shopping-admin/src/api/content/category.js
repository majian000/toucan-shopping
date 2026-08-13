import request from '@/utils/request'

// 查询树表格 (按父ID懒加载, CategoryTreeInfo JSON body)
export function queryCategoryTreeTable(data) {
  return request({ url: '/category/tree/table/by/pid', method: 'post', data })
}

// 查询类别树 (按父ID，用于上级类别选择)
export function queryCategoryTreeByPid(id) {
  return request({ url: '/category/query/category/tree/pid', method: 'post', params: { id } })
}

// 保存
export function saveCategory(data) {
  return request({ url: '/category/save', method: 'post', data })
}

// 修改
export function updateCategory(data) {
  return request({ url: '/category/update', method: 'post', data })
}

// 查看详情
export function detailCategory(data) {
  return request({ url: '/category/detail', method: 'post', data })
}

// 查询类别类型字典
export function queryCategoryTypeList() {
  return request({ url: '/category/query/type/list', method: 'post' })
}

// 删除
export function deleteCategory(data) {
  return request({ url: '/category/delete', method: 'post', data })
}

// 批量删除
export function deleteCategories(data) {
  return request({ url: '/category/delete/ids', method: 'post', data })
}

// 刷新全部缓存
export function flushAllCategoryCache() {
  return request({ url: '/category/flush/all/cache', method: 'post' })
}

// 清空PC首页缓存
export function clearCategoryIndexCache() {
  return request({ url: '/category/clear/index/cache', method: 'post' })
}
