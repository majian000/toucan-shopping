/**
 * 根据用户权限标识集合过滤菜单树
 *
 * 规则：
 *   - 节点有 permission 且用户不拥有 → 节点及子树全部排除
 *   - 节点无 permission（容器）→ 递归判断子节点，有可见子节点才保留
 *   - 节点有 permission 且用户拥有 → 保留，并递归过滤子节点
 *
 * @param {Array}  menuTree   菜单树
 * @param {Set}    userPerms  用户拥有的权限标识集合
 * @returns {Array}  过滤后的菜单树
 */
export function filterMenuByPerms(menuTree, userPerms) {
  function walk(nodes) {
    if (!nodes || !nodes.length) return []
    const result = []
    for (const node of nodes) {
      if (node.permission && !userPerms.has(node.permission)) {
        continue
      }

      let children
      if (node.children && node.children.length) {
        children = walk(node.children)
      }

      // 容器节点（无 path）：只有有可见子节点时才保留
      if (!node.path || node.path.length === 0) {
        if (children && children.length > 0) {
          result.push({ ...node, children })
        }
        continue
      }

      // 有 path 的节点 → 保留
      result.push(children && children.length ? { ...node, children } : { ...node, children: undefined })
    }
    return result
  }

  return walk(menuTree)
}
