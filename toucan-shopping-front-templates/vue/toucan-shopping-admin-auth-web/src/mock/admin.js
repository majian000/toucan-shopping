import { apiUrl } from './_baseUrl'

export default [
  // 角色列表
  {
    url: apiUrl('/system/admin/roleList'),
    method: 'post',
    response: () => ({
      code: 1, msg: '操作成功',
      data: [
        { roleId: 1, name: '超级管理员' },
        { roleId: 2, name: '普通用户' },
        { roleId: 3, name: '审核员' }
      ]
    })
  },
  // 岗位列表
  {
    url: apiUrl('/system/admin/postList'),
    method: 'post',
    response: () => ({
      code: 1, msg: '操作成功',
      data: [
        { id: 1, postId: 'p001', name: '总经理' },
        { id: 2, postId: 'p002', name: '部门经理' },
        { id: 3, postId: 'p003', name: '技术主管' },
        { id: 4, postId: 'p004', name: '开发工程师' },
        { id: 5, postId: 'p005', name: '测试工程师' },
        { id: 6, postId: 'p006', name: '产品经理' },
        { id: 7, postId: 'p007', name: '运维工程师' }
      ]
    })
  },
  // 用户列表
  {
    url: apiUrl('/system/admin/list'),
    method: 'get',
    response: () => {
      const rows = [
        { id: 1, adminId: '10d08ecadc0a4e74a9f61a4a979bcd6e', username: 'admin', roleNames: '超级管理员', roleIdsString: '1', postName: '总经理', postId: 'p001', nickName: '系统管理员', phone: '13800000001', email: 'admin@example.com', sex: '1', remark: '系统管理员', enableStatus: 1, createDate: '2025-01-01 08:00:00' },
        { id: 2, adminId: '20d08ecadc0a4e74a9f61a4a979bcd6f', username: 'zhangsan', roleNames: '普通用户', roleIdsString: '2', postName: '开发工程师', postId: 'p004', nickName: '张三', phone: '13800000002', email: 'zhangsan@example.com', sex: '1', remark: '', enableStatus: 1, createDate: '2025-02-15 10:30:00' },
        { id: 3, adminId: '30d08ecadc0a4e74a9f61a4a979bcd70', username: 'lisi', roleNames: '审核员', roleIdsString: '3', postName: '部门经理', postId: 'p002', nickName: '李四', phone: '13800000003', email: 'lisi@example.com', sex: '0', remark: '审核员', enableStatus: 0, createDate: '2025-03-20 14:00:00' },
        { id: 4, adminId: '40d08ecadc0a4e74a9f61a4a979bcd71', username: 'wangwu', roleNames: '普通用户', roleIdsString: '2', postName: '测试工程师', postId: 'p005', nickName: '王五', phone: '', email: '', sex: '', remark: '', enableStatus: 1, createDate: '2025-04-10 09:20:00' },
        { id: 5, adminId: '50d08ecadc0a4e74a9f61a4a979bcd72', username: 'zhaoliu', roleNames: '普通用户', roleIdsString: '2', postName: '产品经理', postId: 'p006', nickName: '赵六', phone: '13800000005', email: '', sex: '1', remark: '临时账号', enableStatus: 1, createDate: '2025-04-18 11:45:00' },
        { id: 6, adminId: '60d08ecadc0a4e74a9f61a4a979bcd73', username: 'sunqi', roleNames: '审核员', roleIdsString: '3', postName: '运维工程师', postId: 'p007', nickName: '孙七', phone: '', email: 'sunqi@example.com', sex: '0', remark: '', enableStatus: 1, createDate: '2025-05-05 08:30:00' }
      ]
      return { code: 1, msg: '查询成功', data: { list: rows, total: rows.length } }
    }
  },
  // 用户详情
  {
    url: apiUrl('/system/admin/'),
    method: 'get',
    response: () => ({
      code: 1, msg: '操作成功',
      data: { id: 1, adminId: '10d08ecadc0a4e74a9f61a4a979bcd6e', username: 'admin', remark: '系统管理员', enableStatus: 1 }
    })
  },
  // 新增用户
  {
    url: apiUrl('/system/admin/save'),
    method: 'post',
    response: () => ({ code: 1, msg: '新增成功' })
  },
  // 修改用户
  {
    url: apiUrl('/system/admin/update'),
    method: 'post',
    response: () => ({ code: 1, msg: '修改成功' })
  },
  // 删除用户
  {
    url: apiUrl('/system/admin/delete'),
    method: 'post',
    response: () => ({ code: 1, msg: '删除成功' })
  },
  // 批量删除
  {
    url: apiUrl('/system/admin/delete/ids'),
    method: 'post',
    response: () => ({ code: 1, msg: '批量删除成功' })
  },
  // 重置密码
  {
    url: apiUrl('/system/admin/update/password'),
    method: 'post',
    response: () => ({ code: 1, msg: '密码重置成功' })
  },
  // 修改用户状态
  {
    url: apiUrl('/system/admin/changeStatus'),
    method: 'put',
    response: () => ({ code: 1, msg: '状态修改成功' })
  }
]
