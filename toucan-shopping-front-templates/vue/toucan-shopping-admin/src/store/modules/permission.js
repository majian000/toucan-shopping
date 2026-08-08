import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePermissionStore = defineStore('permission', () => {
  const permissions = ref([])
  const loaded = ref(false)

  function setPermissions(list) {
    permissions.value = list
    loaded.value = true
  }

  function hasPermission(perm) {
    // 权限未加载时默认隐藏，避免无权限按钮闪现
    if (!loaded.value) return false
    return permissions.value.includes(perm)
  }

  function hasAnyPermission(permList) {
    if (!loaded.value) return false
    return permList.some(p => permissions.value.includes(p))
  }

  function clearPermissions() {
    permissions.value = []
    loaded.value = false
  }

  return {
    permissions, loaded,
    setPermissions, hasPermission, hasAnyPermission, clearPermissions
  }
})
