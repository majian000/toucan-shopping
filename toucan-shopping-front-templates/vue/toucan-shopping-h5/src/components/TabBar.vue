<template>
  <!-- 底部 TabBar（内联 SVG，不引 UI 库） -->
  <nav class="tabbar safe-bottom">
    <div
      v-for="item in tabs"
      :key="item.key"
      class="tab"
      :class="{ on: item.key === active }"
      @click="go(item)"
    >
      <span class="ico" v-html="item.icon"></span>
      <span class="txt">{{ item.label }}</span>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
  /** 当前高亮：home | category | cart | my */
  active: { type: String, default: 'home' }
});

const router = useRouter();

const I = {
  home: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M3 10.5 12 3l9 7.5"/><path d="M5.5 9.5V21h13V9.5"/><path d="M9.5 21v-6h5v6"/></svg>',
  category: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3.5" y="3.5" width="7" height="7" rx="1.5"/><rect x="13.5" y="3.5" width="7" height="7" rx="1.5"/><rect x="3.5" y="13.5" width="7" height="7" rx="1.5"/><rect x="13.5" y="13.5" width="7" height="7" rx="1.5"/></svg>',
  cart: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="20" r="1.4"/><circle cx="18" cy="20" r="1.4"/><path d="M3 4h2l2.2 11.2a1.5 1.5 0 0 0 1.5 1.2h8.6a1.5 1.5 0 0 0 1.5-1.2L21 8H6"/></svg>',
  my: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="8" r="3.6"/><path d="M4.5 20.5c0-3.6 3.4-5.6 7.5-5.6s7.5 2 7.5 5.6"/></svg>'
};

const tabs = [
  { key: 'home', label: '首页', icon: I.home, path: '/' },
  { key: 'category', label: '分类', icon: I.category, path: '/category' },
  { key: 'cart', label: '购物车', icon: I.cart, path: '/cart' },
  { key: 'my', label: '我的', icon: I.my, path: '/my' }
];

const go = item => {
  if (item.key === props.active) {
    return;
  }
  router.push(item.path);
};
</script>

<style lang="scss" scoped>
.tabbar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 50;
  display: flex;
  height: 52px;
  padding-bottom: $safe-bottom;
  background: $bg-card;
  border-top: 1px solid $border-color;

  .tab {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 2px;
    color: $text-secondary;
    cursor: pointer;

    .ico {
      display: block;
      width: 22px;
      height: 22px;

      :deep(svg) {
        width: 22px;
        height: 22px;
        display: block;
      }
    }

    .txt {
      font-size: 11px;
      line-height: 1;
    }

    &.on {
      color: $primary;
    }
  }
}

// 带 TabBar 的页面根容器加这个类，底部留出 TabBar 高度
:global(.has-tabbar) {
  padding-bottom: calc(52px + #{$safe-bottom} + #{$gap-md});
}
</style>
