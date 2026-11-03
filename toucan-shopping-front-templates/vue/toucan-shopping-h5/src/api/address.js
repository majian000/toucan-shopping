import request from '@/utils/request';

/** 地址列表 */
export function addressList(data) {
  return request({ url: '/h5/user/consigneeAddress/list', method: 'post', data });
}

/** 新增或更新地址 */
export function saveAddress(data) {
  return request({ url: '/h5/user/consigneeAddress/update/save', method: 'post', data });
}

/** 删除地址 */
export function deleteAddress(data) {
  return request({ url: '/h5/user/consigneeAddress/delete', method: 'post', data });
}

/** 设为默认 */
export function setDefaultAddress(data) {
  return request({ url: '/h5/user/consigneeAddress/set/default', method: 'post', data });
}

/** 默认地址 */
export function defaultAddress() {
  return request({ url: '/h5/user/consigneeAddress/find/default', method: 'post' });
}
