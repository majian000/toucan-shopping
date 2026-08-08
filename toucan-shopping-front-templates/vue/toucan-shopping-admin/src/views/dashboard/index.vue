<template>
  <div class="dashboard">
    <h2 class="page-title">工作台</h2>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :md="12">
        <el-card shadow="never" v-permission="'pms:system:online:chart'">
          <template #header>
            <div class="card-header">
              <span class="card-title">在线用户统计</span>
              <el-button link size="small" :icon="Refresh" @click="onlineRef?.refresh()">刷新</el-button>
            </div>
          </template>
          <OnlineUserChart ref="onlineRef" />
        </el-card>
      </el-col>

      <el-col :xs="24" :md="12">
        <el-card shadow="never" v-permission="'pms:home:operate-chart'">
          <template #header>
            <div class="card-header">
              <span class="card-title">操作统计</span>
              <el-button link size="small" :icon="Refresh" @click="operateRef?.refresh()">刷新</el-button>
            </div>
          </template>
          <OperateChart ref="operateRef" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Refresh } from '@element-plus/icons-vue'
import OnlineUserChart from './components/OnlineUserChart.vue'
import OperateChart from './components/OperateChart.vue'

const onlineRef = ref(null)
const operateRef = ref(null)
</script>

<style lang="scss" scoped>
.dashboard {
  .chart-row {
    .el-col { margin-bottom: 16px; }
  }
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    .card-title { font-weight: 600; }
  }
  :deep(.el-card__body) { padding: 16px; }
}
</style>
