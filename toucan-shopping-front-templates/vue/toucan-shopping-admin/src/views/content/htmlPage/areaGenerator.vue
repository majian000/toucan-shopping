<template>
  <div class="area-generator-management">
    <h2 class="page-title">选择地区组件静态化</h2>
    <el-card shadow="never">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="最终文件" name="release">
          <div class="tab-toolbar">
            <el-button type="primary" :icon="Upload" v-permission="'toucan:area:html:generator:release'" @click="handleGenerateRelease" :loading="releaseLoading">生成最终版</el-button>
            <span class="tip-text">生成最终版地区组件静态HTML文件，发布到正式环境。</span>
          </div>
          <div v-if="releaseGenerators && releaseGenerators.length > 0" style="margin-top:16px">
            <el-tabs v-model="releaseSubTab" type="card">
              <el-tab-pane v-for="gen in releaseGenerators" :key="gen.name" :label="gen.name" :name="gen.name">
                <div class="iframe-container" v-html="gen.content"></div>
              </el-tab-pane>
            </el-tabs>
          </div>
          <el-empty v-else description="请点击上方「生成最终版」按钮生成地区组件静态文件" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'
import { generateAreaRelease, queryAreaTab } from '@/api/content/areaGenerator'

const activeTab = ref('release')
const releaseSubTab = ref('')
const releaseLoading = ref(false)
const releaseGenerators = ref([])

async function loadTabs() {
  try {
    const res = await queryAreaTab()
    if (res.code === 1 && res.data) {
      releaseGenerators.value = res.data.releaseHtmlGenerators || []
    }
  } catch { }
}

async function handleGenerateRelease() {
  releaseLoading.value = true
  try {
    const res = await generateAreaRelease()
    if (res.code === 1) {
      ElMessage.success(res.msg || '最终版文件生成成功')
      await loadTabs()
    } else {
      ElMessage.error(res.msg || '生成最终版文件失败')
    }
  } catch { } finally { releaseLoading.value = false }
}

onMounted(loadTabs)
</script>

<style lang="scss" scoped>
.area-generator-management {
  .tab-toolbar { display: flex; align-items: center; gap: 12px; padding: 12px 0;
    .tip-text { color: $text-secondary; font-size: 13px; }
  }
  .iframe-container { width: 100%; min-height: 600px; border: 1px solid #ebeef5; border-radius: 4px; padding: 8px; }
}
</style>
