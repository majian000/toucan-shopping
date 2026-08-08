<template>
  <div class="error-page">
    <div class="error-card">
      <div class="error-code">{{ errorCode }}</div>
      <div class="error-icon">
        <el-icon :size="64">
          <WarningFilled v-if="code === 500" />
          <Lock v-else-if="code === 403" />
          <DocumentDelete v-else />
        </el-icon>
      </div>
      <h2>{{ title }}</h2>
      <p class="error-desc">{{ desc }}</p>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">返回首页</el-button>
        <el-button @click="goBack">返回上页</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { WarningFilled, Lock, DocumentDelete } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const code = computed(() => route.meta?.code || 500)
const errorCode = computed(() => {
  switch (code.value) {
    case 403: return '403'
    case 404: return '404'
    case 500:
    default: return '500'
  }
})
const title = computed(() => {
  switch (code.value) {
    case 403: return '没有访问权限'
    case 404: return '页面不存在'
    case 500:
    default: return '服务器错误'
  }
})
const desc = computed(() => {
  switch (code.value) {
    case 403: return '抱歉，您没有权限访问此页面'
    case 404: return '抱歉，您访问的页面不存在'
    case 500:
    default: return '抱歉，服务器发生错误，请稍后重试'
  }
})

function goHome() {
  router.replace('/dashboard')
}

function goBack() {
  router.back()
}
</script>

<style lang="scss" scoped>
.error-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}

.error-card {
  text-align: center;
  padding: 60px;
}

.error-code {
  font-size: 120px;
  font-weight: 900;
  color: #e4e7ed;
  line-height: 1;
  letter-spacing: 8px;
  user-select: none;
}

.error-icon {
  margin-bottom: 20px;
  color: $text-secondary;
}

h2 {
  margin: 0 0 12px;
  font-size: 22px;
  color: $text-primary;
  font-weight: 600;
}

.error-desc {
  margin: 0 0 32px;
  font-size: 14px;
  color: $text-secondary;
}

.error-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}
</style>
