<template>
  <div v-if="tabs.length > 0" ref="barRef" class="tab-bar">
    <!-- 左滚动箭头 -->
    <div
      v-if="canScrollLeft"
      class="tab-scroll-arrow tab-scroll-arrow--left"
      @click="scrollBy(-200)"
    >
      <el-icon><ArrowLeft /></el-icon>
    </div>

    <!-- 选项卡滚动区域 -->
    <div ref="clipRef" class="tab-clip">
      <div
        class="tab-list"
        :style="{ transform: `translateX(${-scrollOffset}px)` }"
      >
        <div
          v-for="tab in tabs"
          :key="tab.path"
          class="tab-item"
          :class="{ 'tab-item--active': activeTabPath === tab.path }"
          @click="onTabClick(tab)"
          @contextmenu.prevent="onContextMenu($event, tab)"
        >
          <span class="tab-title">{{ tab.title }}</span>
          <span
            v-if="tab.closable"
            class="tab-close"
            @click.stop="onTabClose(tab)"
          >
            <el-icon :size="12"><Close /></el-icon>
          </span>
        </div>
      </div>
    </div>

    <!-- 右滚动箭头 -->
    <div
      v-if="canScrollRight"
      class="tab-scroll-arrow tab-scroll-arrow--right"
      @click="scrollBy(200)"
    >
      <el-icon><ArrowRight /></el-icon>
    </div>

    <!-- 右键上下文菜单 -->
    <teleport to="body">
      <!-- 点击遮罩关闭菜单 -->
      <div
        v-if="menuVisible"
        class="tab-context-mask"
        @mousedown.prevent="closeMenu"
        @contextmenu.prevent="closeMenu"
      ></div>
      <div
        v-if="menuVisible"
        ref="menuRef"
        class="tab-context-menu"
        :style="{ left: menuX + 'px', top: menuY + 'px' }"
      >
        <div
          v-if="menuTab?.closable"
          class="menu-item"
          @click="handleMenuAction('close')"
        >
          <el-icon :size="14"><Close /></el-icon>
          <span>关闭</span>
        </div>
        <div
          class="menu-item"
          :class="{ disabled: closableCount <= 1 }"
          @click="handleMenuAction('closeOthers')"
        >
          <span>关闭其他</span>
        </div>
        <div
          class="menu-item"
          :class="{ disabled: rightClosableCount === 0 }"
          @click="handleMenuAction('closeRight')"
        >
          <span>关闭右侧</span>
        </div>
        <div class="menu-divider"></div>
        <div
          class="menu-item"
          :class="{ disabled: closableCount === 0 }"
          @click="handleMenuAction('closeAll')"
        >
          <span>全部关闭</span>
        </div>
      </div>
    </teleport>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted, nextTick } from 'vue'
import { storeToRefs } from 'pinia'
import { useTabsStore } from '@/store/modules/tabs'
import { ArrowLeft, ArrowRight, Close } from '@element-plus/icons-vue'

const tabsStore = useTabsStore()
const { tabs, activeTabPath } = storeToRefs(tabsStore)

// ========== 滚动状态 ==========
const barRef = ref(null)
const clipRef = ref(null)
const scrollOffset = ref(0)
const maxScroll = ref(0)
const canScrollLeft = ref(false)
const canScrollRight = ref(false)

function updateScrollState() {
  const clip = clipRef.value
  if (!clip) return
  const list = clip.firstElementChild
  if (!list) return
  const clipWidth = clip.clientWidth
  const listWidth = list.scrollWidth
  maxScroll.value = Math.max(0, listWidth - clipWidth)
  canScrollLeft.value = scrollOffset.value > 0
  canScrollRight.value = scrollOffset.value < maxScroll.value
  // 窗口缩小时修正越界
  if (scrollOffset.value > maxScroll.value) {
    scrollOffset.value = maxScroll.value
  }
}

function scrollBy(delta) {
  scrollOffset.value = Math.max(0, Math.min(maxScroll.value, scrollOffset.value + delta))
  updateScrollState()
}

// ========== Tab 交互 ==========
function onTabClick(tab) {
  tabsStore.setActiveTab(tab.path)
}

function onTabClose(tab) {
  tabsStore.closeTab(tab.path)
}

// ========== 右键菜单 ==========
const menuVisible = ref(false)
const menuX = ref(0)
const menuY = ref(0)
const menuTab = ref(null)
const menuRef = ref(null)

// 可关闭 tab 总数
const closableCount = computed(() => tabs.value.filter(t => t.closable).length)

// 当前右键 tab 右侧可关闭的 tab 数量
const rightClosableCount = computed(() => {
  if (!menuTab.value) return 0
  const idx = tabs.value.findIndex(t => t.path === menuTab.value.path)
  if (idx === -1) return 0
  let count = 0
  for (let i = idx + 1; i < tabs.value.length; i++) {
    if (tabs.value[i].closable) count++
  }
  return count
})

function onContextMenu(e, tab) {
  menuTab.value = tab
  // 计算菜单位置，确保不超出视口
  const menuWidth = 140
  const menuHeight = menuTab.value.closable ? 136 : 90
  let x = e.clientX
  let y = e.clientY
  if (x + menuWidth > window.innerWidth) x = window.innerWidth - menuWidth - 8
  if (y + menuHeight > window.innerHeight) y = window.innerHeight - menuHeight - 8
  menuX.value = x
  menuY.value = y
  menuVisible.value = true
}

function closeMenu() {
  menuVisible.value = false
  menuTab.value = null
}

function handleMenuAction(action) {
  if (!menuTab.value) return
  const path = menuTab.value.path

  // 禁用态不执行
  if (action === 'closeOthers' && closableCount.value <= 1) return
  if (action === 'closeRight' && rightClosableCount.value === 0) return
  if (action === 'closeAll' && closableCount.value === 0) return

  switch (action) {
    case 'close':
      tabsStore.closeTab(path)
      break
    case 'closeOthers':
      tabsStore.closeOthers(path)
      break
    case 'closeRight':
      tabsStore.closeRight(path)
      break
    case 'closeAll':
      tabsStore.closeAll()
      break
  }
  closeMenu()
}

function onKeyDown(e) {
  if (e.key === 'Escape') closeMenu()
}

// ========== 生命周期 ==========
let resizeObserver = null

onMounted(async () => {
  await nextTick()
  updateScrollState()
  window.addEventListener('resize', updateScrollState)
  document.addEventListener('keydown', onKeyDown)
  if (clipRef.value) {
    resizeObserver = new ResizeObserver(() => updateScrollState())
    resizeObserver.observe(clipRef.value)
  }
})

onUnmounted(() => {
  window.removeEventListener('resize', updateScrollState)
  document.removeEventListener('keydown', onKeyDown)
  if (resizeObserver) resizeObserver.disconnect()
})

// Tab 列表变化时重新计算滚动状态
watch(() => tabs.value.length, () => {
  nextTick(() => updateScrollState())
})
</script>

<style lang="scss" scoped>
.tab-bar {
  position: relative;
  display: flex;
  align-items: center;
  height: 38px;
  background: #fff;
  border-bottom: 1px solid $border-light;
  flex-shrink: 0;
  user-select: none;
}

// ========== 滚动箭头 ==========
.tab-scroll-arrow {
  position: absolute;
  top: 0;
  z-index: 50;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 100%;
  cursor: pointer;
  color: #909399;
  flex-shrink: 0;

  &:hover {
    color: $primary;
  }

  &--left {
    left: 0;
    background: linear-gradient(to right, #fff 60%, transparent);
  }

  &--right {
    right: 0;
    background: linear-gradient(to left, #fff 60%, transparent);
  }
}

// ========== 裁剪区 ==========
.tab-clip {
  flex: 1;
  height: 100%;
  overflow: hidden;
  position: relative;
}

// ========== Tab 列表 ==========
.tab-list {
  display: inline-flex;
  height: 100%;
  align-items: stretch;
  white-space: nowrap;
  transition: transform 0.2s ease;
  padding: 0 4px;
}

// ========== 单个 Tab ==========
.tab-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 0 14px;
  height: 100%;
  cursor: pointer;
  color: $text-regular;
  font-size: 13px;
  white-space: nowrap;
  border-right: 1px solid $border-light;
  position: relative;
  transition: all 0.15s;

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: transparent;
    transition: background 0.15s;
  }

  &:hover {
    color: $primary;
    background: rgba(64, 158, 255, 0.04);
  }

  &--active {
    color: $primary;
    font-weight: 600;
    background: rgba(64, 158, 255, 0.06);

    &::after {
      background: $primary;
    }
  }
}

.tab-title {
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 140px;
}

.tab-close {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  color: #909399;
  flex-shrink: 0;

  &:hover {
    background: #c0c4cc;
    color: #fff;
  }
}

</style>

<!-- 右键菜单全局样式（teleport 到 body 后 scoped 失效） -->
<style lang="scss">
.tab-context-mask {
  position: fixed;
  inset: 0;
  z-index: 9998;
}

.tab-context-menu {
  position: fixed;
  z-index: 9999;
  min-width: 140px;
  padding: 4px 0;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.12);
  border: 1px solid #e4e7ed;

  .menu-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 16px;
    font-size: 13px;
    color: #303133;
    cursor: pointer;
    transition: all 0.15s;

    &:hover:not(.disabled) {
      background: #ecf5ff;
      color: $primary;
    }

    &.disabled {
      color: #c0c4cc;
      cursor: not-allowed;
    }
  }

  .menu-divider {
    height: 1px;
    margin: 4px 0;
    background: #e4e7ed;
  }
}
</style>
