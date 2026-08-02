import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const activeTopMenu = ref('dashboard')
  const sidebarCollapsed = ref(false)
  const breadcrumbs = ref([])

  // 布局模式：'top' 顶部菜单 | 'side' 左侧菜单
  const layoutMode = ref(localStorage.getItem('layoutMode') || 'top')

  // 菜单数据：权限接口未返回时为空，加载完成后才显示
  const menuData = ref([])

  function setActiveTopMenu(key) {
    activeTopMenu.value = key
  }

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function setBreadcrumbs(list) {
    breadcrumbs.value = list
  }

  function setMenuData(data) {
    menuData.value = data
  }

  function setLayoutMode(mode) {
    layoutMode.value = mode
    localStorage.setItem('layoutMode', mode)
    if (mode === 'side') {
      sidebarCollapsed.value = false
    }
  }

  return {
    activeTopMenu,
    sidebarCollapsed,
    breadcrumbs,
    layoutMode,
    menuData,
    setActiveTopMenu,
    toggleSidebar,
    setBreadcrumbs,
    setLayoutMode,
    setMenuData
  }
})
