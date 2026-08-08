<template>
  <nav ref="navRef" class="top-menu-nav" v-click-outside="closeAll">
    <!-- 左滚动箭头 -->
    <div v-if="canScrollLeft" class="scroll-arrow scroll-arrow-left" @click.stop="scrollBy(-200)">
      <el-icon><ArrowLeft /></el-icon>
    </div>

    <!-- 菜单裁剪区（仅包含顶部菜单项，不包含下拉面板） -->
    <div ref="clipRef" class="menu-clip">
      <div class="top-menu-list" :style="{ transform: `translateX(${-scrollOffset}px)` }">
        <div
          v-for="item in menuItems"
          :key="item.key"
          class="menu-item-wrapper"
          :ref="el => setItemRef(item.key, el)"
        >
          <div
            class="top-menu-item"
            :class="{ active: activeKey === item.key, hover: hoverKey === item.key }"
            @click="onTopClick(item)"
          >
            <el-icon v-if="item.icon" class="menu-icon"><component :is="item.icon" /></el-icon>
            <span class="menu-label">{{ item.label }}</span>
            <el-icon v-if="item.children && item.children.length" class="arrow"><ArrowDown /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 右滚动箭头 -->
    <div v-if="canScrollRight" class="scroll-arrow scroll-arrow-right" @click.stop="scrollBy(200)">
      <el-icon><ArrowRight /></el-icon>
    </div>

    <!-- ========== 下拉面板（渲染在裁剪区外部） ========== -->
    <template v-for="item in menuItems" :key="'dp-' + item.key">
      <div
        v-if="item.children && item.children.length && hoverKey === item.key"
        class="dropdown-panel"
        :style="getDropdownStyle(item.key)"
      >
        <!-- L2 列表 -->
        <div class="panel-scroll">
          <div
            v-for="child in item.children"
            :key="child.path || child.label"
            :ref="el => setRowRef(child, el)"
          >
            <div
              v-if="!child.children || child.children.length === 0"
              class="dropdown-item"
              :class="{ active: route.path === child.path }"
              @click.stop="navigate(child.path)"
            >
              <el-icon v-if="child.icon" class="item-icon"><component :is="child.icon" /></el-icon>
              <span>{{ child.label }}</span>
            </div>
            <div
              v-else
              class="dropdown-item has-children"
              :class="{ 'is-open': openPath[0] === (child.path || child.label) }"
              @click.stop="toggleL2(child)"
            >
              <el-icon v-if="child.icon" class="item-icon"><component :is="child.icon" /></el-icon>
              <span>{{ child.label }}</span>
              <el-icon class="right-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
        </div>

        <!-- 二级子面板 -->
        <template v-for="child in item.children.filter(c => c.children)" :key="'s2-' + (child.path || child.label)">
          <div
            v-show="openPath[0] === (child.path || child.label)"
            class="sub-panel"
            :style="getSubStyle(child)"
          >
            <div class="sub-scroll">
              <div v-for="gc in child.children" :key="gc.path || gc.label" :ref="el => setRowRef(gc, el)">
                <div
                  v-if="!gc.children || gc.children.length === 0"
                  class="dropdown-item"
                  :class="{ active: route.path === gc.path }"
                  @click.stop="navigate(gc.path)"
                >
                  <el-icon v-if="gc.icon" class="item-icon"><component :is="gc.icon" /></el-icon>
                  <span>{{ gc.label }}</span>
                </div>
                <div
                  v-else
                  class="dropdown-item has-children"
                  :class="{ 'is-open': openPath[1] === (gc.path || gc.label) }"
                  @click.stop="toggleL3(gc)"
                >
                  <el-icon v-if="gc.icon" class="item-icon"><component :is="gc.icon" /></el-icon>
                  <span>{{ gc.label }}</span>
                  <el-icon class="right-arrow"><ArrowRight /></el-icon>
                </div>
              </div>
            </div>

            <!-- 三级子面板 -->
            <template v-for="gc in child.children.filter(c => c.children)" :key="'s3-' + (gc.path || gc.label)">
              <div
                v-show="openPath[1] === (gc.path || gc.label)"
                class="sub-panel"
                :style="getSubStyle(gc)"
              >
                <div class="sub-scroll">
                  <div v-for="ggc in gc.children" :key="ggc.path || ggc.label" :ref="el => setRowRef(ggc, el)">
                    <div
                      v-if="!ggc.children || ggc.children.length === 0"
                      class="dropdown-item"
                      :class="{ active: route.path === ggc.path }"
                      @click.stop="navigate(ggc.path)"
                    >
                      <el-icon v-if="ggc.icon" class="item-icon"><component :is="ggc.icon" /></el-icon>
                      <span>{{ ggc.label }}</span>
                    </div>
                    <div
                      v-else
                      class="dropdown-item has-children"
                      :class="{ 'is-open': openPath[2] === (ggc.path || ggc.label) }"
                      @click.stop="toggleL4(ggc)"
                    >
                      <el-icon v-if="ggc.icon" class="item-icon"><component :is="ggc.icon" /></el-icon>
                      <span>{{ ggc.label }}</span>
                      <el-icon class="right-arrow"><ArrowRight /></el-icon>
                    </div>
                  </div>
                </div>

                <!-- 四级子面板 -->
                <template v-for="ggc in gc.children.filter(c => c.children)" :key="'s4-' + (ggc.path || ggc.label)">
                  <div
                    v-show="openPath[2] === (ggc.path || ggc.label)"
                    class="sub-panel"
                    :style="getSubStyle(ggc)"
                  >
                    <div class="sub-scroll">
                      <div
                        v-for="gggc in ggc.children"
                        :key="gggc.path"
                        class="dropdown-item"
                        :class="{ active: route.path === gggc.path }"
                        @click.stop="navigate(gggc.path)"
                      >
                        <el-icon v-if="gggc.icon" class="item-icon"><component :is="gggc.icon" /></el-icon>
                        <span>{{ gggc.label }}</span>
                      </div>
                    </div>
                  </div>
                </template>
              </div>
            </template>
          </div>
        </template>
      </div>
    </template>
  </nav>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowDown, ArrowRight, ArrowLeft } from '@element-plus/icons-vue'
import { useTabsStore } from '@/store/modules/tabs'

const props = defineProps({
  menuItems: { type: Array, required: true },
  activeKey: { type: String, default: '' }
})

const emit = defineEmits(['select'])

const route = useRoute()
const router = useRouter()
const tabsStore = useTabsStore()

// ========= 状态 =========
const hoverKey = ref('')
const openPath = ref([])

// ========= 横向滚动 =========
const navRef = ref(null)
const clipRef = ref(null)
const scrollOffset = ref(0)
const maxScroll = ref(0)
const canScrollLeft = ref(false)
const canScrollRight = ref(false)

// 顶级菜单项的 DOM 引用（用于计算下拉面板位置）
const itemRefs = {}

function setItemRef(key, el) {
  if (el) itemRefs[key] = el
}

function updateScrollState() {
  const clip = clipRef.value
  if (!clip) return
  // 使用 scrollWidth/scrollHeight 技巧：临时设置 overflow 来测量
  const list = clip.firstElementChild  // .top-menu-list
  if (!list) return
  const clipWidth = clip.clientWidth
  const listWidth = list.scrollWidth
  maxScroll.value = Math.max(0, listWidth - clipWidth)
  canScrollLeft.value = scrollOffset.value > 0
  canScrollRight.value = scrollOffset.value < maxScroll.value
  // 修正越界
  if (scrollOffset.value > maxScroll.value) {
    scrollOffset.value = maxScroll.value
  }
}

function scrollBy(delta) {
  scrollOffset.value = Math.max(0, Math.min(maxScroll.value, scrollOffset.value + delta))
  updateScrollState()
}

// ========= 下拉面板定位（相对于 top-menu-nav） =========
function getDropdownStyle(key) {
  const itemEl = itemRefs[key]
  if (!itemEl || !navRef.value) return {}
  const navRect = navRef.value.getBoundingClientRect()
  const itemRect = itemEl.getBoundingClientRect()
  return {
    left: (itemRect.left - navRect.left) + 'px',
    top: navRect.height + 'px'
  }
}

// ========= 行 DOM 引用（子面板对齐） =========
const rowRefs = {}
function setRowRef(item, el) {
  if (el) rowRefs[item.path || item.label] = el
}

function getSubStyle(item) {
  const id = item.path || item.label
  const rowEl = rowRefs[id]
  if (!rowEl) return {}
  const panel = rowEl.closest('.sub-panel') || rowEl.closest('.dropdown-panel')
  if (!panel) return {}
  const pr = panel.getBoundingClientRect()
  const rr = rowEl.getBoundingClientRect()
  return {
    left: (pr.right - pr.left) + 'px',
    top: (rr.top - pr.top) + 'px'
  }
}

// ========= 顶层菜单点击 =========
function onTopClick(item) {
  if (hoverKey.value === item.key) {
    hoverKey.value = ''
    openPath.value = []
  } else {
    hoverKey.value = item.key
    openPath.value = []
    emit('select', item)
  }
}

// ========= 逐级展开/收起 =========
function toggleL2(item) {
  const id = item.path || item.label
  openPath.value = openPath.value[0] === id ? [] : [id]
}

function toggleL3(item) {
  const id = item.path || item.label
  if (openPath.value[1] === id) {
    openPath.value = openPath.value.slice(0, 1)
  } else {
    openPath.value = [openPath.value[0], id]
  }
}

function toggleL4(item) {
  const id = item.path || item.label
  if (openPath.value[2] === id) {
    openPath.value = openPath.value.slice(0, 2)
  } else {
    openPath.value = [openPath.value[0], openPath.value[1], id]
  }
}

// ========= 叶子节点导航 =========
function navigate(path) {
  if (path) {
    const resolved = router.resolve(path)
    const title = resolved.meta?.title || ''
    const name = resolved.name || ''
    tabsStore.openTab(path, title, name)
  }
  hoverKey.value = ''
  openPath.value = []
}

// ========= 点击外部关闭 =========
function closeAll() {
  hoverKey.value = ''
  openPath.value = []
}

const vClickOutside = {
  mounted(el, binding) {
    el.__clickOutside = (e) => {
      if (!el.contains(e.target)) binding.value()
    }
    document.addEventListener('click', el.__clickOutside, true)
  },
  unmounted(el) {
    document.removeEventListener('click', el.__clickOutside, true)
  }
}

// ========= 生命周期 =========
let resizeObserver = null

onMounted(async () => {
  await nextTick()
  updateScrollState()
  window.addEventListener('resize', updateScrollState)
  if (clipRef.value) {
    resizeObserver = new ResizeObserver(() => updateScrollState())
    resizeObserver.observe(clipRef.value)
  }
})

watch(() => props.menuItems, () => {
  nextTick(() => updateScrollState())
}, { deep: true })

onUnmounted(() => {
  window.removeEventListener('resize', updateScrollState)
  if (resizeObserver) resizeObserver.disconnect()
})
</script>

<style lang="scss" scoped>
.top-menu-nav {
  position: relative;
  flex: 1;
  min-width: 0;
  height: 100%;
  overflow: visible;
}

// ========== 滚动箭头 ==========
.scroll-arrow {
  position: absolute;
  top: 0;
  z-index: 50;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 100%;
  cursor: pointer;
  color: #909399;
  background: linear-gradient(to right, rgba(255,255,255,0.95), rgba(255,255,255,0.7));
  transition: color 0.15s;

  &:hover { color: #409eff; }

  // 覆盖 v-click-outside：阻止冒泡到 document 导致关闭菜单
}

.scroll-arrow-left {
  left: 0;
  background: linear-gradient(to right, #fff 60%, transparent);
}

.scroll-arrow-right {
  right: 0;
  background: linear-gradient(to left, #fff 60%, transparent);
}

// ========== 裁剪区 ==========
.menu-clip {
  height: 100%;
  overflow: hidden;    // 只裁剪横向溢出
  position: relative;
}

// ========== 菜单列表 ==========
.top-menu-list {
  display: inline-flex;  // 用 inline-flex 以便测量真实宽度
  height: 100%;
  align-items: stretch;
  white-space: nowrap;
  transition: transform 0.25s ease;
}

// ========== 顶级菜单项 ==========
.menu-item-wrapper {
  position: relative;
  flex-shrink: 0;
}

.top-menu-item {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 0 18px;
  height: 100%;
  cursor: pointer;
  color: #606266;
  font-size: 14px;
  white-space: nowrap;
  transition: all 0.15s;
  user-select: none;
  border-bottom: 2px solid transparent;

  .menu-icon { font-size: 15px; }
  .arrow { font-size: 10px; margin-left: 1px; transition: transform 0.2s; }

  &:hover { color: #409eff; background: rgba(64,158,255,0.04); }

  &.active,
  &.hover {
    color: #409eff;
    font-weight: 600;
    border-bottom-color: #409eff;
    .arrow { transform: rotate(180deg); }
  }
}

// ========== 下拉面板（渲染在裁剪区外，不受 overflow:hidden 影响） ==========
.dropdown-panel {
  position: absolute;
  top: 100%;
  z-index: 1000;
  animation: fadeIn 0.12s ease;
}

.panel-scroll {
  background: #fff;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
  padding: 6px 0;
  min-width: 180px;
  max-width: 260px;
  max-height: 420px;
  overflow-y: auto;
  position: relative;
  z-index: 1;

  &::-webkit-scrollbar { width: 4px; }
  &::-webkit-scrollbar-thumb { background: #dcdfe6; border-radius: 2px; }
}

// ========== 菜单项 ==========
.dropdown-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 9px 16px;
  font-size: 13px;
  color: #303133;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.1s;

  .item-icon {
    font-size: 15px;
    color: #909399;
    flex-shrink: 0;
    width: 16px;
    text-align: center;
  }

  span {
    flex: 1;
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  &.has-children {
    .right-arrow {
      font-size: 11px;
      color: #909399;
      flex-shrink: 0;
      margin-left: auto;
      transition: transform 0.15s;
    }
    &.is-open .right-arrow {
      transform: rotate(90deg);
    }
  }

  &:hover,
  &.is-open {
    background: #ecf5ff;
    color: #409eff;
    .item-icon, .right-arrow { color: #409eff; }
  }

  &.active {
    background: #ecf5ff;
    color: #409eff;
    font-weight: 600;
    .item-icon { color: #409eff; }
  }
}

// ========== 子面板 ==========
.sub-panel {
  position: absolute;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
  padding: 6px 0;
  min-width: 160px;
  max-width: 230px;
  z-index: 10;
  overflow: visible;
  animation: fadeIn 0.1s ease;
}

.sub-scroll {
  max-height: 380px;
  overflow-y: auto;

  &::-webkit-scrollbar { width: 4px; }
  &::-webkit-scrollbar-thumb { background: #dcdfe6; border-radius: 2px; }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateX(-4px); }
  to { opacity: 1; transform: translateX(0); }
}
</style>
