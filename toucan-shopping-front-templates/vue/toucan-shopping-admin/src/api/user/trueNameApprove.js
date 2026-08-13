import request from '@/utils/request'

// 查询实名审核列表 (UserTrueNameApprovePageInfo @RequestBody -> JSON body)
export function listTrueNameApprove(data) {
  return request({ url: '/user/true/name/approve/list', method: 'post', data })
}

// 审核通过
export function passTrueNameApprove(id, userMainId) {
  return request({ url: `/user/true/name/approve/pass/${id}/${userMainId}`, method: 'post' })
}

// 审核驳回 (UserTrueNameApproveVO @RequestBody -> JSON body)
export function rejectTrueNameApprove(data) {
  return request({ url: '/user/true/name/approve/reject', method: 'post', data })
}
