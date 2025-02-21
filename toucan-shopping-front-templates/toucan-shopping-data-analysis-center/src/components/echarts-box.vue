<template>
  <div :id="id" class="echarts-box"></div>
</template>

<script setup>
import { onMounted, onUnmounted, watch } from "vue";
import * as echarts from "echarts";

const props = defineProps({
  id: {
    type: String,
    default: "echarts-box",
  },
  option: {
    type: Object,
    default: () => {},
  },
});

let chart = null;
onMounted(() => {
  // 1实例化对象
  chart = echarts.init(document.getElementById(props.id));

  // 2. 把配置项给实例对象
  chart.setOption(props.option);
  // 3. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", resize);
});

onUnmounted(() => {
  window.removeEventListener("resize", resize);
});

function resize() {
  chart.resize();
}

watch(
  () => props.option,
  () => {
    chart?.setOption(props.option);
  },
  {
    immediate: true,
  }
);
</script>

<style lang="less" scoped>
.echarts-box {
  width: 100%;
  height: 100%;
}
</style>
