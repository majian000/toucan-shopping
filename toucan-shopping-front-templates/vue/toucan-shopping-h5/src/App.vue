<template>
  <!-- H5 端没有 PC 的页头/页脚：每个页面自己管整屏布局（含安全区，见 styles/index.scss） -->
  <div id="app" class="h5-root">
    <router-view />
  </div>
</template>

<script setup>
defineOptions({ name: 'App' });
</script>

<style lang="scss" scoped>
.h5-root {
  min-height: 100vh;
  min-height: 100dvh;
  box-sizing: border-box;
  background: $bg-page;
}
</style>
