import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

/**
 * echarts 图表通用 composable
 * @param {Function} buildOption 根据数据构建 option 的函数
 */
export function useChart(buildOption) {
  const chartRef = ref(null)
  const loading = ref(false)
  let instance = null

  function init() {
    if (!chartRef.value) return
    instance = echarts.init(chartRef.value)
  }

  function setData(data) {
    if (!instance) return
    instance.setOption(buildOption(data), { notMerge: true })
  }

  function resize() {
    instance?.resize()
  }

  function dispose() {
    instance?.dispose()
    instance = null
  }

  onMounted(() => {
    init()
    window.addEventListener('resize', resize)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', resize)
    dispose()
  })

  return { chartRef, loading, setData }
}
