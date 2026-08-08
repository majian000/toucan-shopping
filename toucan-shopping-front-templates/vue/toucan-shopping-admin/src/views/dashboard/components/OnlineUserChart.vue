<template>
  <div v-loading="loading" class="chart-wrapper">
    <div ref="chartRef" class="chart" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useChart } from '../hooks/useChart'
import { queryOnlineUserChart } from '@/api/system/chart'

const COLORS = ['#00A3E0', '#FFA100', '#ffc0cb', '#CCCCCC', '#BBFFAA', '#749f83', '#ca8622']

function buildOption(list) {
  const items = list || []
  return {
    color: COLORS,
    tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: items.map(i => i.appName || ''),
      axisLabel: { rotate: items.length > 4 ? 30 : 0 }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      name: '在线用户数'
    },
    series: [{
      type: 'bar',
      data: items.map(i => i.loginCount || 0)
    }]
  }
}

const { chartRef, loading, setData } = useChart(buildOption)

async function refresh() {
  loading.value = true
  try {
    const res = await queryOnlineUserChart()
    setData(res.data || [])
  } finally {
    loading.value = false
  }
}

onMounted(() => { refresh() })

defineExpose({ refresh })
</script>

<style lang="scss" scoped>
.chart-wrapper {
  width: 100%;
  height: 400px;
  .chart { width: 100%; height: 100%; }
}
</style>
