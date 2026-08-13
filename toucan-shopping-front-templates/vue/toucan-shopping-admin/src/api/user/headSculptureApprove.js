import request from '@/utils/request'

// 查询头像审核列表 (UserHeadSculptureApprovePageInfo @RequestBody -> JSON body)
export function listHeadSculptureApprove(data) {
  return request({ url: '/user/head/sculpture/approve/list', method: 'post', data })
}

// 审核通过
export function passHeadSculptureApprove(id) {
  return request({ url: `/user/head/sculpture/approve/pass/${id}`, method: 'post' })
}

// 审核驳回 (UserHeadSculptureApproveVO @RequestBody -> JSON body)
export function rejectHeadSculptureApprove(data) {
  return request({ url: '/user/head/sculpture/approve/reject', method: 'post', data })
}
