<template>
  <div class="free-shop-management">
    <h2 class="page-title">免费开店页静态化管理</h2>
    <el-card shadow="never">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="预览版" name="preview">
          <div class="tab-toolbar">
            <el-button type="primary" :icon="Upload" v-permission="'toucan:seller:freeShop:html:generator:preview'" @click="handleGeneratePreview" :loading="previewLoading">生成预览版</el-button>
            <span class="tip-text">生成免费开店页预览静态HTML文件。</span>
          </div>
        </el-tab-pane>
        <el-tab-pane label="最终版" name="release">
          <div class="tab-toolbar">
            <el-button type="primary" :icon="Upload" v-permission="'toucan:seller:web:freeShop:html:generator:release'" @click="handleGenerateRelease" :loading="releaseLoading">生成最终版</el-button>
            <span class="tip-text">生成免费开店页最终版静态HTML文件，发布到正式环境。</span>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'
import { generatePreview, generateRelease } from '@/api/seller/freeShop'

const activeTab = ref('preview')
const previewLoading = ref(false)
const releaseLoading = ref(false)

async function handleGeneratePreview() {
  previewLoading.value = true
  try {
    const res = await generatePreview()
    if (res.code === 1) {
      ElMessage.success(res.msg || '预览文件生成成功')
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
    } else {
      ElMessage.error(res.msg || '生成最终版文件失败')
    }
  } catch { } finally { releaseLoading.value = false }
}
</script>

<style lang="scss" scoped>
.free-shop-management {
  .tab-toolbar { display: flex; align-items: center; gap: 12px; padding: 12px 0;
    .tip-text { color: $text-secondary; font-size: 13px; }
  }
}
</style>
