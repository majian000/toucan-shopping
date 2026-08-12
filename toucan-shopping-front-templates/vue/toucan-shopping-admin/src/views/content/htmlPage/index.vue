<template>
  <div class="html-page-management">
    <h2 class="page-title">首页静态化管理</h2>
    <el-card shadow="never">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="最终效果" name="release">
          <div class="tab-toolbar">
            <el-button type="primary" :icon="Upload" @click="handleGenerateRelease" :loading="releaseLoading">生成最终版</el-button>
            <span class="tip-text">生成最终版首页静态HTML文件，发布到正式环境。</span>
          </div>
          <div v-if="releaseGenerators && releaseGenerators.length > 0" style="margin-top:16px">
            <el-tabs v-model="releaseSubTab" type="card">
              <el-tab-pane v-for="gen in releaseGenerators" :key="gen.name" :label="gen.name" :name="gen.name">
                <div class="iframe-container" v-html="gen.content"></div>
              </el-tab-pane>
            </el-tabs>
          </div>
          <el-empty v-else description="请点击"生成最终版"按钮生成首页静态文件" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'
import { generatePreview, generateRelease } from '@/api/content/htmlPage'

const activeTab = ref('release')
const releaseSubTab = ref('')
const releaseLoading = ref(false)
const previewLoading = ref(false)
const releaseGenerators = ref([])
const previewGenerators = ref([])

async function handleGeneratePreview() {
  previewLoading.value = true
  try {
    const res = await generatePreview()
    if (res.code === 1) {
      ElMessage.success(res.msg || '预览文件生成成功')
      // 重新加载页面内容
      setTimeout(() => window.location.reload(), 1500)
    } else {
      ElMessage.error(res.msg || '生成预览文件失败')
    }
  } catch { } finally { previewLoading.value = false }
}

async function handleGenerateRelease() {
  releaseLoading.value = true
  try {
    const res = await generateRelease()
    if (res.code === 1) {
      ElMessage.success(res.msg || '最终版文件生成成功')
      setTimeout(() => window.location.reload(), 1500)
    } else {
      ElMessage.error(res.msg || '生成最终版文件失败')
    }
  } catch { } finally { releaseLoading.value = false }
}
</script>

<style lang="scss" scoped>
.html-page-management {
  .tab-toolbar { display: flex; align-items: center; gap: 12px; padding: 12px 0;
    .tip-text { color: $text-secondary; font-size: 13px; }
  }
  .iframe-container { width: 100%; min-height: 600px; border: 1px solid #ebeef5; border-radius: 4px; padding: 8px; }
}
</style>
