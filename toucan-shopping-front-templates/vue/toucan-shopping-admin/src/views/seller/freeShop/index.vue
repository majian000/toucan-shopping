<template>
  <div class="free-shop-management">
    <h2 class="page-title">免费开店页静态化管理</h2>
    <el-card shadow="never">
      <div class="toolbar">
        <el-radio-group v-model="env" @change="switchEnv">
          <el-radio-button value="preview">预览版</el-radio-button>
          <el-radio-button value="release">正式版</el-radio-button>
        </el-radio-group>
        <el-button v-if="env === 'preview'" type="primary" :icon="Upload" v-permission="'toucan:seller:freeShop:html:generator:previewBtn'" :loading="previewLoading" @click="handleGeneratePreview">生成预览</el-button>
        <el-button v-else type="primary" :icon="Upload" v-permission="'toucan:seller:freeShop:html:generator:releaseBtn'" :loading="releaseLoading" @click="handleGenerateRelease">生成最终版</el-button>
      </div>
      <p class="tip-text">预览版仅用于内网预览，不影响线上；正式版生成后即对外发布，请确认后操作。</p>

      <el-collapse v-if="currentNodes.length" v-model="activeNode" accordion>
        <el-collapse-item v-for="node in currentNodes" :key="node.name" :name="node.name">
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
      <el-empty v-else description="请点击上方「生成」按钮生成免费开店页静态文件" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Upload, Link } from '@element-plus/icons-vue'
import { generatePreview, generateRelease, queryTab } from '@/api/seller/freeShop'

const env = ref('preview')
const previewLoading = ref(false)
const releaseLoading = ref(false)
const frameLoading = ref(false)
const previewNodes = ref([])
const releaseNodes = ref([])
const activeNode = ref('')

const currentNodes = computed(() => (env.value === 'preview' ? previewNodes.value : releaseNodes.value))

function extractSrc(content) {
  const m = /src=['"]([^'"]+)['"]/.exec(content || '')
  return m ? m[1] : ''
}

function mapNodes(list) {
  return (list || []).map(g => ({ name: g.name, src: extractSrc(g.content) }))
}

function setActiveNode() {
  const nodes = currentNodes.value
  activeNode.value = nodes.length ? nodes[0].name : ''
  frameLoading.value = true
}

async function loadTabs() {
  try {
    const res = await queryTab()
    if (res.code === 1 && res.data) {
      previewNodes.value = mapNodes(res.data.previewHtmlGenerators)
      releaseNodes.value = mapNodes(res.data.releaseHtmlGenerators)
      setActiveNode()
    }
  } catch { }
}

function switchEnv() {
  setActiveNode()
}

async function handleGeneratePreview() {
  previewLoading.value = true
  try {
    const res = await generatePreview()
    if (res.code === 1) {
      ElMessage.success(res.msg || '预览文件生成成功')
      await loadTabs()
    } else {
      ElMessage.error(res.msg || '生成预览文件失败')
    }
  } catch { } finally { previewLoading.value = false }
}

async function handleGenerateRelease() {
  try {
    await ElMessageBox.confirm('正式版生成后即对外发布，确定继续吗？', '生成确认', { confirmButtonText: '确定生成', cancelButtonText: '取消', type: 'warning' })
  } catch { return }
  releaseLoading.value = true
  try {
    const res = await generateRelease()
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
.free-shop-management {
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
