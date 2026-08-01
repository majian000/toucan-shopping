import { apiUrl } from './_baseUrl'

let postsData = [
  { id: 1, name: '总经理', postId: 'p001', enableStatus: 1, remark: '公司总经理岗位', createDate: '2025-01-01 08:00:00' },
  { id: 2, name: '部门经理', postId: 'p002', enableStatus: 1, remark: '部门经理岗位', createDate: '2025-01-15 10:00:00' },
  { id: 3, name: '技术主管', postId: 'p003', enableStatus: 1, remark: '技术团队主管', createDate: '2025-02-01 09:00:00' },
  { id: 4, name: '开发工程师', postId: 'p004', enableStatus: 1, remark: '软件开发工程师', createDate: '2025-02-15 14:00:00' },
  { id: 5, name: '测试工程师', postId: 'p005', enableStatus: 1, remark: '软件测试工程师', createDate: '2025-03-01 10:30:00' },
  { id: 6, name: '产品经理', postId: 'p006', enableStatus: 0, remark: '产品规划与管理', createDate: '2025-03-10 16:00:00' },
  { id: 7, name: '运维工程师', postId: 'p007', enableStatus: 1, remark: '系统运维岗位', createDate: '2025-04-01 11:00:00' }
]

let nextId = 8

export default [
  // 岗位列表
  {
    url: apiUrl('/system/post/list'),
    method: 'post',
    response: ({ body }) => {
      const { name, enableStatus, page = 1, size = 10 } = body || {}
      let filtered = [...postsData]
      if (name) {
        filtered = filtered.filter(p => p.name.includes(name))
      }
      if (enableStatus !== undefined && enableStatus !== null && enableStatus !== '') {
        filtered = filtered.filter(p => p.enableStatus === Number(enableStatus))
      }
      const total = filtered.length
      const start = (page - 1) * size
      const rows = filtered.slice(start, start + size)
      return { code: 1, msg: '查询成功', data: { rows, total } }
    }
  },
  // 新增岗位
  {
    url: apiUrl('/system/post/save'),
    method: 'post',
    response: ({ body }) => {
      const newPost = {
        id: nextId++,
        postId: 'p' + String(nextId - 1).padStart(3, '0'),
        name: body.name,
        enableStatus: body.enableStatus ?? 1,
        remark: body.remark || '',
        createDate: new Date().toISOString().replace('T', ' ').substring(0, 19)
      }
      postsData.unshift(newPost)
      return { code: 1, msg: '新增成功' }
    }
  },
  // 编辑岗位
  {
    url: apiUrl('/system/post/update'),
    method: 'post',
    response: ({ body }) => {
      const idx = postsData.findIndex(p => p.id === body.id)
      if (idx > -1) {
        postsData[idx] = { ...postsData[idx], ...body }
      }
      return { code: 1, msg: '修改成功' }
    }
  },
  // 删除岗位
  {
    url: apiUrl('/system/post/delete'),
    method: 'post',
    response: ({ body }) => {
      postsData = postsData.filter(p => p.id !== body.id)
      return { code: 1, msg: '删除成功' }
    }
  }
]
