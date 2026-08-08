<template>
  <aside class="side-menu" :class="{ collapsed: appStore.sidebarCollapsed }">
    <div class="side-menu-inner">
      <el-menu
        :default-active="activeMenuPath"
        :collapse="appStore.sidebarCollapsed"
        :unique-opened="true"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        @select="handleMenuSelect"
      >
        <template v-for="item in appStore.menuData" :key="item.key">
          <!-- 有 children 的顶层菜单 → 包一层 el-sub-menu -->
          <template v-if="item.children && item.children.length">
            <SideMenuItem
              :item="item"
              @select="handleMenuSelect"
            />
          </template>
          <!-- 没有 children 的直接叶子 -->
          <el-menu-item v-else :index="item.path || '/' + item.key">
            <el-icon v-if="resolveIcon(item.icon)"><component :is="resolveIcon(item.icon)" /></el-icon>
            <template #title>{{ item.label }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </div>

    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="appStore.toggleSidebar()">
      <el-icon :size="16">
        <DArrowLeft v-if="!appStore.sidebarCollapsed" />
        <DArrowRight v-else />
      </el-icon>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useAppStore } from '@/store/modules/app'
import { useTabsStore } from '@/store/modules/tabs'
import { DArrowLeft, DArrowRight } from '@element-plus/icons-vue'
import * as Icons from '@element-plus/icons-vue'
import SideMenuItem from './SideMenuItem.vue'

function resolveIcon(name) {
  return name ? Icons[name] : null
}

const appStore = useAppStore()
const tabsStore = useTabsStore()

const activeMenuPath = computed(() => tabsStore.activeTabPath)

function handleMenuSelect(path) {
  if (!path) return
  // 递归搜索整个菜单树找到对应节点
  const found = findInTree(appStore.menuData, path)
  if (found) {
    tabsStore.openTab(path, found.label || '', '')
  }
}

function findInTree(nodes, path) {
  if (!nodes || !nodes.length) return null
  for (const item of nodes) {
    if (item.path === path) return item
    if (item.children) {
      const found = findInTree(item.children, path)
      if (found) return found
    }
  }
  return null
}
</script>

<style lang="scss" scoped>
.side-menu {
  position: relative;
  width: $side-width;
  background: #304156;
  flex-shrink: 0;
  transition: width 0.3s;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  &.collapsed { width: 64px; }

  .side-menu-inner {
    flex: 1;
    overflow-y: auto;
    overflow-x: hidden;
    &::-webkit-scrollbar { width: 3px; }
    &::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.15); border-radius: 2px; }
  }

  :deep(.el-menu) {
    border-right: none;
    .el-menu-item, .el-sub-menu__title {
      height: 48px; line-height: 48px; font-size: 13px;
      &:hover { background-color: rgba(255,255,255,0.06) !important; }
    }
    .el-menu-item.is-active { background-color: rgba(64,158,255,0.15) !important; }
  }

  .collapse-btn {
    height: 40px;
    display: flex; align-items: center; justify-content: center;
    cursor: pointer; color: #bfcbd9;
    border-top: 1px solid rgba(255,255,255,0.08);
    transition: background 0.2s;
    &:hover { background: rgba(255,255,255,0.06); color: #fff; }
  }
}
</style>
