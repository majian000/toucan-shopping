<template>
  <div class="common-box">
    <span class="left-top"></span>
    <span class="right-top"></span>
    <span class="left-bottom"></span>
    <span class="right-bottom"></span>
    <slot />
  </div>
</template>

<script setup></script>

<style lang="less" scoped>
.common-box {
  width: 100%;
  height: 316px;
  padding: 10px;
  box-sizing: border-box;
  margin-bottom: 10px;
  position: relative;
  border: 1px solid rgba(25, 186, 139, 0.17);
  background: url("/images/line(1).png") rgba(255, 255, 255, 0.03);
  .left-top,
  .right-top,
  .left-bottom,
  .right-bottom {
    position: absolute;
    display: inline-block;
    width: 15px;
    height: 15px;
    border: 2px solid #02a6b5;
    z-index: 2;
  }

  .left-top {
    left: 0;
    top: 0;
    border-bottom: none;
    border-right: none;
  }
  .right-top {
    right: 0;
    top: 0;
    border-bottom: none;
    border-left: none;
  }
  .left-bottom {
    left: 0;
    bottom: 0;
    border-top: none;
    border-right: none;
  }
  .right-bottom {
    right: 0;
    bottom: 0;
    border-top: none;
    border-left: none;
  }
}
</style>
