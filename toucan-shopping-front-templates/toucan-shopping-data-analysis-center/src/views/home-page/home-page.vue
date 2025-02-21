<template>
  <div class="home-page">
    <header class="header-box">
      <h1 class="title">可视化展板</h1>
      <div class="date-time">当前时间: 2024年7月8日 20时50分49秒</div>
    </header>
    <main class="main-box">
      <aside class="left-box">
        <CommonBoxVue>
          <EchartsBoxVue :option="BAR_OPTION1" id="BAR_OPTION1" />
        </CommonBoxVue>
        <CommonBoxVue>
          <EchartsBoxVue :option="LINE_OPTION1" id="LINE_OPTION1" />
        </CommonBoxVue>
        <CommonBoxVue>
          <EchartsBoxVue :option="PIE_OPTION1" id="PIE_OPTION1" />
        </CommonBoxVue>
      </aside>
      <section class="middle-box">
        <div class="top">
          <div class="num-box">
            <div class="left-num">125811</div>
            <div class="right-num">100000</div>
          </div>
          <div class="text-box">
            <div class="left-text">前端需求人数</div>
            <div class="right-text">市场供应人数</div>
          </div>
        </div>
        <div class="map-box">
          <!-- 地图的三个背景 -->
          <div class="bg1"></div>
          <div class="bg2"></div>
          <div class="bg3"></div>
          <!-- 地图 -->
          <EchartsBoxVue :option="state.option" id="MAP_ID" />
        </div>
      </section>
      <aside class="right-box">
        <CommonBoxVue>
          <EchartsBoxVue :option="BAR_OPTION2" id="BAR_OPTION2" />
        </CommonBoxVue>
        <CommonBoxVue>
          <EchartsBoxVue :option="LINE_OPTION2" id="LINE_OPTION2" />
        </CommonBoxVue>
        <CommonBoxVue>
          <EchartsBoxVue :option="PIE_OPTION2" id="PIE_OPTION2" />
        </CommonBoxVue>
      </aside>
    </main>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from "vue";
import CommonBoxVue from "../../components/common-box.vue";
import EchartsBoxVue from "../../components/echarts-box.vue";
import {
  BAR_OPTION1,
  BAR_OPTION2,
  LINE_OPTION1,
  LINE_OPTION2,
  PIE_OPTION1,
  PIE_OPTION2,
} from "./home-page-config";

import HomePage from "./use-home-page.js";

const { state, init } = new HomePage().use();

onMounted(() => {
  init();
  // 初始化自适应  ----在刚显示的时候就开始适配一次
  handleScreenAuto();
  // 绑定自适应函数   ---防止浏览器栏变化后不再适配
  window.onresize = () => handleScreenAuto();
});

onUnmounted(() => {
  // 移除自适应函数
  window.onresize = null;
});

// 适配方案先不处理，先把功能实现了先
function handleScreenAuto() {
  const designDraftWidth = 1920; //设计稿的宽度
  const designDraftHeight = 1080; //设计稿的高度

  // 根据屏幕的变化适配的比例,这么处理，可能会出现留白问题,可以通过控制背景色与主题色相匹配，来降低留白的影响
  // 当 设备宽高比 > 16/9时，两边出现留白
  // 当 设备宽高比 < 16/9时，上下出现留白
  // 接下来写样式就直接通过px来写即可

  const screenWidth =
    window.innerWidth ||
    document.documentElement.clientWidth ||
    document.body.clientWidth;
  const screenHeight =
    window.innerHeight ||
    document.documentElement.clientHeight ||
    document.body.clientHeight;

  // x y轴自适应缩放
  const scaleX = screenWidth / designDraftWidth;
  const scaleY = screenHeight / designDraftHeight;

  // 设置缩放中心点 很重要否则，当是2560 * 1440 、  3840 * 2160等分辨率时，会出现向上偏移
  document.querySelector(".home-page").style.transformOrigin = "0 0";
  // 缩放比例
  document.querySelector(
    ".home-page"
  ).style.transform = `scale(${scaleX}, ${scaleY})`;
}
</script>

<style lang="less" scoped>
@import "./home-page.less";
</style>
