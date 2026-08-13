import request from '@/utils/request'

// 查询列表 (ProductSpuPageInfo @RequestBody -> data)
export function listProductSpu(data) {
  return request({ url: '/productSpu/list', method: 'post', data })
}

// 保存 (ProductSpuVO @RequestBody -> data)
export function saveProductSpu(data) {
  return request({ url: '/productSpu/save', method: 'post', data })
}

// 修改 (ProductSpuVO @RequestBody -> data)
export function updateProductSpu(data) {
  return request({ url: '/productSpu/update', method: 'post', data })
}

// 删除 (ProductSpuVO @RequestBody -> data)
export function deleteProductSpu(data) {
  return request({ url: '/productSpu/delete', method: 'post', data })
}

// 批量删除 (List @RequestBody -> data)
export function deleteProductSpuByIds(data) {
  return request({ url: '/productSpu/delete/ids', method: 'post', data })
}

// 分类树 (CategoryVO @RequestBody -> data)
export function queryCategoryTreeByPid(data) {
  return request({ url: '/productSpu/query/category/tree/pid', method: 'post', data })
}

// 品牌列表 (BrandPageInfo @RequestBody -> data)
export function listBrand(data) {
  return request({ url: '/productSpu/brand/list', method: 'post', data })
}

// 属性树分页 (AttributeKeyPageInfo @RequestBody -> data)
export function queryAttributeTreePage(data) {
  return request({ url: '/productSpu/query/attribute/tree/page', method: 'post', data })
}

// 查询详情(含属性名和属性值) (ProductSpuVO @RequestBody -> data)
export function findByIdProductSpu(data) {
  return request({ url: '/productSpu/findById', method: 'post', data })
}
