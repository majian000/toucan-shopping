import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import router from '@/router'

export const useTabsStore = defineStore('tabs', () => {
  // ========== State ==========

  // 首页 tab 始终存在，不可关闭
  const tabs = ref([
    {
      path: '/dashboard',
      title: '工作台',
      name: 'Dashboard',
      closable: false
    }
  ])

  const activeTabPath = ref('/dashboard')

  // ========== Getters ==========

  const activeTab = computed(() => {
    return tabs.value.find(t => t.path === activeTabPath.value) || tabs.value[0]
  })

  const tabNames = computed(() => {
    return tabs.value.map(t => t.name).filter(Boolean)
  })

  // ========== Actions ==========

  /**
   * 打开选项卡。
   * 如果已存在同路径 tab，直接激活；否则新增并激活。
   */
  function openTab(path, title, name) {
    const existing = tabs.value.find(t => t.path === path)
    if (existing) {
      activeTabPath.value = path
    } else {
      tabs.value.push({
        path,
        title: title || '',
        name: name || '',
        closable: path !== '/dashboard'
      })
      activeTabPath.value = path
    }
    // URL 同步
    router.replace(path).catch(() => {})
  }

  /**
   * 关闭选项卡。
   * 如果关闭的是当前激活 tab，优先激活右侧相邻 tab，无右侧则左侧。
   */
  function closeTab(path) {
    const tab = tabs.value.find(t => t.path === path)
    if (!tab || !tab.closable) return

    const idx = tabs.value.indexOf(tab)

    // 关闭当前激活 tab 时，切换到相邻 tab
    if (activeTabPath.value === path) {
      if (tabs.value.length > 1) {
        const nextTab = tabs.value[idx + 1] || tabs.value[idx - 1]
        if (nextTab) {
          activeTabPath.value = nextTab.path
          router.replace(nextTab.path).catch(() => {})
        }
      }
    }

    // 移除 tab
    tabs.value.splice(idx, 1)

    // 安全检查：确保有激活的 tab
    if (!tabs.value.find(t => t.path === activeTabPath.value)) {
      activeTabPath.value = tabs.value[0]?.path || '/dashboard'
      router.replace(activeTabPath.value).catch(() => {})
    }
  }

  /**
   * 关闭其他选项卡，保留不可关闭的 tab 和目标 tab。
   */
  function closeOthers(path) {
    const tab = tabs.value.find(t => t.path === path)
    if (!tab) return

    // 先激活目标 tab
    activeTabPath.value = path
    router.replace(path).catch(() => {})

    // 移除除不可关闭 tab 和目标 tab 之外的所有 tab
    tabs.value = tabs.value.filter(t => !t.closable || t.path === path)
  }

  /**
   * 关闭目标 tab 右侧所有可关闭的选项卡。
   */
  function closeRight(path) {
    const idx = tabs.value.findIndex(t => t.path === path)
    if (idx === -1) return

    // 激活目标 tab
    activeTabPath.value = path
    router.replace(path).catch(() => {})

    // 移除右侧所有可关闭的 tab
    const keep = []
    for (let i = 0; i < tabs.value.length; i++) {
      if (i > idx && tabs.value[i].closable) continue
      keep.push(tabs.value[i])
    }
    tabs.value = keep
  }

  /**
   * 关闭所有可关闭的选项卡（只保留不可关闭的 tab）。
   */
  function closeAll() {
    const unclosable = tabs.value.find(t => !t.closable)
    tabs.value = tabs.value.filter(t => !t.closable)
    // 激活保留的 tab
    if (unclosable) {
      activeTabPath.value = unclosable.path
      router.replace(unclosable.path).catch(() => {})
    }
  }

  /**
   * 切换激活的选项卡。
   */
  function setActiveTab(path) {
    if (tabs.value.find(t => t.path === path)) {
      activeTabPath.value = path
      router.replace(path).catch(() => {})
    }
  }

  /**
   * 页面加载时从当前路由初始化 store。
   * 由 AdminLayout onMounted 调用。
   */
  function initFromRoute(route) {
    const path = route.path
    if (path === '/' || path === '/dashboard') return // 已有 dashboard tab

    const existing = tabs.value.find(t => t.path === path)
    if (!existing) {
      tabs.value.push({
        path,
        title: route.meta?.title || '',
        name: route.name || '',
        closable: path !== '/dashboard'
      })
    }
    activeTabPath.value = path
  }

  return {
    tabs,
    activeTabPath,
    activeTab,
    tabNames,
    openTab,
    closeTab,
    closeOthers,
    closeRight,
    closeAll,
    setActiveTab,
    initFromRoute
  }
})
