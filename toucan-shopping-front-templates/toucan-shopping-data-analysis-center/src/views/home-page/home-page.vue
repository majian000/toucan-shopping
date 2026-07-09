<template>
  <div class="home-page">
    <header class="header-box">
      <h1 class="title">电商数据可视化大屏</h1>
      <div class="date-time">当前时间: {{ currentTime }}</div>
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
            <div class="num-item">
              <div class="num-value">¥2,358</div>
              <div class="num-unit">万元</div>
            </div>
            <div class="num-item">
              <div class="num-value">186,520</div>
              <div class="num-unit">单</div>
            </div>
            <div class="num-item">
              <div class="num-value">98.6</div>
              <div class="num-unit">%</div>
            </div>
            <div class="num-item last">
              <div class="num-value">32,861</div>
              <div class="num-unit">人</div>
            </div>
          </div>
          <div class="text-box">
            <div class="text-item">成交总额(GMV)</div>
            <div class="text-item">订单总数</div>
            <div class="text-item">好评率</div>
            <div class="text-item last">新增用户</div>
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
import { ref, onMounted, onUnmounted } from "vue";
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

const currentTime = ref("");

function updateTime() {
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1;
  const day = now.getDate();
  const hour = now.getHours();
  const minute = now.getMinutes();
  const second = now.getSeconds();
  currentTime.value = year + '年' + month + '月' + day + '日 ' + String(hour).padStart(2, '0') + ':' + String(minute).padStart(2, '0') + ':' + String(second).padStart(2, '0');
}

let timer = null;
onMounted(() => {
  init();
  updateTime();
  timer = setInterval(updateTime, 1000);
  // 初始化自适应  ----在刚显示的时候就开始适配一次
  handleScreenAuto();
  // 绑定自适应函数   ---防止浏览器栏变化后不再适配
  window.onresize = () => handleScreenAuto();
});

onUnmounted(() => {
  // 移除自适应函数
  window.onresize = null;
  if (timer) clearInterval(timer);
});

// 适配方案先不处理，先把功能实现了先
function handleScreenAuto() {
  const designDraftWidth = 1920; //设计稿的宽度
  const designDraftHeight = 1080; //设计稿的高度

  const screenWidth =
    window.innerWidth ||
    document.documentElement.clientWidth ||
    document.body.clientWidth;
  const screenHeight =
    window.innerHeight ||
    document.documentElement.clientHeight ||
    document.body.clientHeight;

  const scaleX = screenWidth / designDraftWidth;
  const scaleY = screenHeight / designDraftHeight;

  document.querySelector(".home-page").style.transformOrigin = "0 0";
  document.querySelector(
    ".home-page"
  ).style.transform = 'scale(' + scaleX + ', ' + scaleY + ')';
}
</script>

<style lang="less" scoped>
@import "./home-page.less";
</style>


