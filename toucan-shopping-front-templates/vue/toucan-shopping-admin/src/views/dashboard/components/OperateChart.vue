<template>
  <div v-loading="loading" class="chart-wrapper">
    <div ref="chartRef" class="chart" />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useChart } from '../hooks/useChart'
import { queryOperateChart } from '@/api/system/chart'

function buildOption(data) {
  const vo = data || {}
  const names = vo.appNames || []
  const categorys = vo.categorys || []
  const datas = vo.datas || []

  return {
    color: ['#00A3E0', '#FFA100', '#ffc0cb', '#CCCCCC', '#BBFFAA', '#749f83', '#ca8622'],
    tooltip: { trigger: 'axis' },
    legend: { data: names, bottom: 0 },
    toolbox: { feature: { saveAsImage: { title: '保存' } } },
    grid: { left: '3%', right: '4%', bottom: '12%', containLabel: true },
    xAxis: { type: 'category', boundaryGap: false, data: categorys },
    yAxis: { type: 'value', minInterval: 1, name: '操作次数' },
    series: datas.map(d => ({
      name: d.appName,
      type: 'line',
      data: (d.values || []).map(v => Number(v))
    }))
  }
}

const { chartRef, loading, setData } = useChart(buildOption)

async function refresh() {
  loading.value = true
  try {
    const res = await queryOperateChart()
    setData(res.data || {})
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
