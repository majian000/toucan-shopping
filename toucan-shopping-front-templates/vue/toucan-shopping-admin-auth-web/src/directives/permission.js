/**
 * v-permission 自定义指令
 *
 * 用于控制页面元素的显示/隐藏，基于用户权限列表，支持实时响应权限变更
 *
 * 用法：
 *   <el-button v-permission="'system:admin:add'">新增</el-button>
 *   <el-button v-permission="['system:admin:edit', 'system:admin:delete']">操作</el-button>
 *
 * 行为：
 *   - 单权限：用户拥有该权限则显示，否则 display:none 隐藏
 *   - 多权限（数组）：用户满足任一权限则显示，否则隐藏
 *   - 权限列表变化时自动更新显隐状态（支持 mock 热更新）
 */

import { watch } from 'vue'
import { usePermissionStore } from '@/store/modules/permission'

function resolve(el, binding) {
  const store = usePermissionStore()
  const { value } = binding

  if (!value) {
    el.style.display = ''
    return
  }

  let has = false
  if (Array.isArray(value)) {
    has = store.hasAnyPermission(value)
  } else {
    has = store.hasPermission(value)
  }

  el.style.display = has ? '' : 'none'
}

const vPermission = {
  mounted(el, binding) {
    // 初始检查
    resolve(el, binding)
    // 监听权限列表变化，实时更新显隐
    const store = usePermissionStore()
    el.__unwatchPerm = watch(
      () => [...store.permissions],
      () => resolve(el, binding)
    )
  },
  updated(el, binding) {
    if (binding.value === binding.oldValue) return
    resolve(el, binding)
  },
  unmounted(el) {
    if (el.__unwatchPerm) {
      el.__unwatchPerm()
      el.__unwatchPerm = null
    }
  }
}

export default vPermission
