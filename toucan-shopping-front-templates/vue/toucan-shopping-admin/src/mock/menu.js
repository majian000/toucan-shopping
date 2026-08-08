import { apiUrl } from './_baseUrl'
import { menuTreeData } from './_menuTree'

/**
 * 将 menuTreeData 转换为前端菜单格式（过滤 status:0 和 button 节点）
 */
function buildMenuFromTree(nodes) {
  return nodes
    .filter(n => n.status === 1 && n.type !== 'button')
    .map(n => {
      const base = { id: n.id, label: n.name, icon: n.icon }

      if (n.type === 'directory') {
        base.key = n.path ? n.path.replace(/\//g, '_').replace(/^_/, '') : n.name
      }

      if (n.path && n.path.length > 0) {
        base.path = n.path
      }

      if (n.children && n.children.length) {
        const filtered = buildMenuFromTree(n.children)
        if (filtered.length) base.children = filtered
      }

      return base
    })
}

// 递归查找节点
function findNodeById(tree, id) {
  for (const n of tree) {
    if (n.id === id) return n
    if (n.children) {
      const found = findNodeById(n.children, id)
      if (found) return found
    }
  }
  return null
}

// 递归去除 children 只保留 hasChildren 标记，用于根节点列表
function stripChildren(nodes) {
  return nodes.map(n => ({
    ...n,
    hasChildren: !!(n.children && n.children.length > 0),
    children: undefined
  }))
}

export default [
  // 获取前端菜单（从 menuTreeData 构建，status:0 的节点不显示）
  {
    url: apiUrl('/index/menus'),
    method: 'get',
    response: () => {
      const data = buildMenuFromTree(menuTreeData)
      return { code: 1, msg: '操作成功', data }
    }
  },
  {
    url: apiUrl('/getRouters'),
    method: 'get',
    response: () => ({ code: 1, msg: '操作成功', data: [] })
  },
  // 菜单管理/权限加载 - 全量菜单树（含按钮权限）
  {
    url: apiUrl('/system/menu/list'),
    method: 'get',
    response: () => ({
      code: 1, msg: '查询成功',
      data: menuTreeData
    })
  },
  // 菜单管理 - 懒加载根节点（不含 children）
  {
    url: apiUrl('/system/menu/roots'),
    method: 'get',
    response: () => ({
      code: 1, msg: '查询成功',
      data: stripChildren(menuTreeData)
    })
  },
  // 菜单管理 - 懒加载子节点
  {
    url: apiUrl('/system/menu/children'),
    method: 'get',
    response: ({ query }) => {
      const parentId = Number(query.parentId)
      const node = findNodeById(menuTreeData, parentId)
      const children = node?.children ? stripChildren(node.children) : []
      return { code: 1, msg: '查询成功', data: children }
    }
  },
  {
    url: apiUrl('/system/menu/'),
    method: 'get',
    response: () => ({
      code: 1, msg: '操作成功',
      data: { id: 11, name: '工作台', type: 'menu', icon: 'Monitor', path: '/dashboard', component: 'dashboard/index', permission: 'dashboard:view', sort: 1, status: 1 }
    })
  },
  { url: apiUrl('/system/menu'), method: 'post', response: () => ({ code: 1, msg: '新增成功' }) },
  { url: apiUrl('/system/menu'), method: 'put', response: () => ({ code: 1, msg: '修改成功' }) },
  { url: apiUrl('/system/menu/'), method: 'delete', response: () => ({ code: 1, msg: '删除成功' }) }
]
