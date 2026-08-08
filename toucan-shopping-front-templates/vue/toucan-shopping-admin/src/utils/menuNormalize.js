/**
 * 将后端返回的菜单数据标准化为前端期望的格式
 *
 * 后端字段 → 前端字段：
 *   href     → path       路由路径
 *   title    → label      显示文本（优先用 name）
 *   type     → type       0→'directory'  1→'menu'  2→'button'
 *   children: null → children: undefined
 *
 * 并生成 key 字段供菜单组件使用
 */

/** 0=目录 1=菜单 2=按钮 */
function mapType(t) {
  if (t === 0) return 'directory'
  if (t === 1) return 'menu'
  if (t === 2) return 'button'
  return 'menu'
}

/** 从 path 生成 key：取最后一段或首段有意义的部分 */
function generateKey(path, name) {
  if (path && path.length > 0) {
    return path.replace(/^\//, '').replace(/\//g, '_')
  }
  return name
}

/**
 * 标准化菜单树
 * @param {Array} nodes 后端返回的菜单数组
 * @returns {Array} 前端格式的菜单数组
 */
export function normalizeMenuTree(nodes) {
  if (!nodes || !nodes.length) return []
  return nodes.map(node => {
    const path = node.href || ''
    const label = node.name || node.title || ''
    const type = mapType(node.type)
    const children = node.children && node.children.length
      ? normalizeMenuTree(node.children)
      : undefined

    return {
      id: node.id,
      name: node.name || node.title || '',
      label,
      path,
      icon: node.icon || '',
      permission: node.permission || '',
      type,
      status: node.status,
      // directory 节点需要 key 供 TopMenu 使用
      ...(type === 'directory' ? { key: generateKey(path, node.name) } : {}),
      ...(children ? { children } : {})
    }
  })
}
