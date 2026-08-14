<template>
  <div class="area-generator-management">
    <h2 class="page-title">选择地区组件静态化</h2>
    <el-card shadow="never">
      <div class="toolbar">
        <el-button type="primary" :icon="Upload" v-permission="'toucan:area:htmlGenerator:release'" :loading="releaseLoading" @click="handleGenerateRelease">生成最终版</el-button>
      </div>
      <p class="tip-text">生成最终版地区组件静态HTML文件，发布到正式环境。</p>

      <el-collapse v-if="nodes.length" v-model="activeNode" accordion>
        <el-collapse-item v-for="node in nodes" :key="node.name" :name="node.name">
          <template #title>
            <div class="node-title">
              <span class="node-ip">{{ node.name }}</span>
              <el-link class="open-link" type="primary" :icon="Link" @click.stop="openNewWindow(node)">新窗口打开</el-link>
            </div>
          </template>
          <div class="iframe-wrap" v-loading="frameLoading">
            <iframe :src="node.src" class="preview-frame" frameborder="0" @load="frameLoading = false"></iframe>
          </div>
        </el-collapse-item>
      </el-collapse>
      <el-empty v-else description="请点击上方「生成最终版」按钮生成地区组件静态文件" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Upload, Link } from '@element-plus/icons-vue'
import { generateAreaRelease, queryAreaTab } from '@/api/content/areaGenerator'

const releaseLoading = ref(false)
const frameLoading = ref(false)
const nodes = ref([])
const activeNode = ref('')

function extractSrc(content) {
  const m = /src=['"]([^'"]+)['"]/.exec(content || '')
  return m ? m[1] : ''
}

async function loadTabs() {
  try {
    const res = await queryAreaTab()
    if (res.code === 1 && res.data) {
      nodes.value = (res.data.releaseHtmlGenerators || []).map(g => ({ name: g.name, src: extractSrc(g.content) }))
      activeNode.value = nodes.value.length ? nodes.value[0].name : ''
      frameLoading.value = true
    }
  } catch { }
}

async function handleGenerateRelease() {
  try {
    await ElMessageBox.confirm('正式版生成后即对外发布，确定继续吗？', '生成确认', { confirmButtonText: '确定生成', cancelButtonText: '取消', type: 'warning' })
  } catch { return }
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

function openNewWindow(node) {
  if (node && node.src) window.open(node.src, '_blank')
}

onMounted(loadTabs)
</script>

<style lang="scss" scoped>
.area-generator-management {
  .toolbar { display: flex; align-items: center; gap: 16px; margin-bottom: 8px; }
  .tip-text { color: $text-secondary; font-size: 13px; margin: 0 0 16px; }
  .node-title { display: flex; align-items: center; gap: 12px; width: 100%;
    .node-ip { font-weight: 600; }
    .open-link { margin-left: auto; }
  }
  .iframe-wrap { width: 100%; }
  .preview-frame { width: 100%; height: calc(100vh - 320px); min-height: 480px; border: none; background: #fff; }
}
</style>
