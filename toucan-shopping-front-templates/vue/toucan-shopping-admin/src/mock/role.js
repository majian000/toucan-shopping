import { apiUrl } from './_baseUrl'
import { menuTreeData } from './_menuTree'

// 收集所有菜单类节点的ID（用于超级管理员全选）
function collectAllMenuIds(tree) {
  const ids = []
  function walk(nodes) {
    nodes.forEach(n => {
      ids.push(n.id)
      if (n.children && n.children.length) walk(n.children)
    })
  }
  walk(tree)
  return ids
}

const allMenuIds = collectAllMenuIds(menuTreeData)

// 角色数据（可变，支持保存后即时生效）
let rolesData = [
  {
    id: 1, name: '超级管理员', code: 'admin', description: '拥有所有权限', remark: '超级管理员角色', enableStatus: 1,
    permKeys: [...allMenuIds],
    createTime: '2025-01-01 08:00:00'
  },
  {
    id: 2, name: '普通用户', code: 'user', description: '基础功能权限', remark: '普通用户角色', enableStatus: 1,
    permKeys: [
      // 工作台
      1, 11,
      // 系统管理 - 用户列表
      2, 210, 211, 2111, 2112, 2113, 2114, 2115, 2116, 2117, 2118,
      // 个人中心 - 修改密码
      3, 31, 311
    ],
    createTime: '2025-01-15 10:00:00'
  },
  {
    id: 3, name: '审核员', code: 'auditor', description: '审核相关权限', remark: '审核员角色', enableStatus: 1,
    permKeys: [
      // 工作台
      1, 11,
      // 系统管理 - 权限管理（查看角色和菜单）
      2, 220, 221, 2211, 2212, 2213, 2214, 2215, 222, 2221, 2222, 2223,
      // 个人中心 - 修改密码
      3, 31, 311
    ],
    createTime: '2025-02-01 09:00:00'
  }
]

export default [
  {
    url: apiUrl('/system/role/list'),
    method: 'post',
    response: () => ({
      code: 1, msg: '查询成功',
      data: { rows: rolesData, total: rolesData.length }
    })
  },
  {
    url: apiUrl('/system/role/'),
    method: 'get',
    response: () => ({
      code: 1, msg: '操作成功',
      data: { id: 1, name: '超级管理员', code: 'admin', description: '拥有所有权限', enableStatus: 1, permKeys: [...allMenuIds], menuTree: menuTreeData }
    })
  },
  { url: apiUrl('/system/role/save'), method: 'post', response: () => ({ code: 1, msg: '新增成功' }) },
  {
    url: apiUrl('/system/role'),
    method: 'put',
    response: ({ body }) => {
      // 支持保存权限：更新对应角色的 permKeys
      const { id, permKeys } = body
      if (id && permKeys) {
        const role = rolesData.find(r => r.id === id)
        if (role) {
          role.permKeys = [...permKeys]
        }
      }
      return { code: 1, msg: '修改成功' }
    }
  },
  { url: apiUrl('/system/role/delete'), method: 'post', response: () => ({ code: 1, msg: '删除成功' }) },
  {
    url: apiUrl('/system/role/menuTree'),
    method: 'get',
    response: () => ({
      code: 1, msg: '操作成功',
      data: menuTreeData
    })
  }
]
