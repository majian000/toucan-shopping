<template>
  <div ref="chartRef" class="bar-chart-container" :style="{ height: height }"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: {
    type: Object,
    required: true,
    default: () => ({ xAxis: [], series: [] })
  },
  title: {
    type: String,
    default: ''
  },
  height: {
    type: String,
    default: '300px'
  }
})

const chartRef = ref(null)
let chartInstance = null

function initChart() {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)
  updateChart()
}

function updateChart() {
  if (!chartInstance) return
  const option = {
    title: {
      text: props.title,
      left: 'center',
      textStyle: { fontSize: 14, fontWeight: 500 }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: props.data.series.map(s => s.name),
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '12%',
      top: props.title ? '15%' : '5%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: props.data.xAxis
    },
    yAxis: {
      type: 'value'
    },
    series: props.data.series.map(s => ({
      name: s.name,
      type: 'bar',
      data: s.data,
      emphasis: {
        focus: 'series'
      },
      itemStyle: {
        borderRadius: [4, 4, 0, 0]
      }
    }))
  }
  chartInstance.setOption(option, true)
}

function handleResize() {
  chartInstance?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose()
  chartInstance = null
})

watch(
  () => props.data,
  () => {
    updateChart()
  },
  { deep: true }
)
</script>

<style lang="scss" scoped>
.bar-chart-container {
  width: 100%;
}
</style>
