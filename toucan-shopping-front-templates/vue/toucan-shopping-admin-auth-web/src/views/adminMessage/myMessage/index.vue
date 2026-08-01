<template>
  <div class="my-messages-page" v-loading="loading">
    <h2 class="page-title">我的消息</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="关键词"><el-input v-model="searchForm.keyword" placeholder="搜索标题或内容" clearable style="width:240px" @keyup.enter="handleSearch" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.isRead" placeholder="请选择" clearable style="width:120px">
            <el-option label="未读" :value="0" /><el-option label="已读" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button><el-button :icon="Refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>

    <div class="notice-list" v-if="notices.length > 0">
      <div v-for="item in notices" :key="item.id" class="notice-card" :class="{ unread: item.isRead === 0 }" @click="handleView(item)">
        <div class="notice-card-left">
          <div class="notice-avatar"><el-icon :size="20"><Bell /></el-icon></div>
        </div>
        <div class="notice-card-body">
          <div class="notice-card-header">
            <span class="notice-card-title">
              <span v-if="item.isRead === 0" class="unread-dot"></span>
              {{ item.messageTitle }}
            </span>
            <el-tag v-if="item.isRead === 0" size="small" type="danger" effect="plain">未读</el-tag>
          </div>
          <div class="notice-card-excerpt" v-html="item.messageContent || '--'"></div>
          <div class="notice-card-meta">
            <span class="meta-item"><el-icon><UserFilled /></el-icon> {{ item.adminUsername || item.adminId }}</span>
            <span class="meta-item"><el-icon><Clock /></el-icon> {{ item.createDate }}</span>
          </div>
        </div>
      </div>
    </div>
    <el-empty v-if="!loading && notices.length === 0" description="暂无消息" />
    <div class="pagination-wrap" v-if="total > 0">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :page-sizes="[10,20,50]" :total="total" layout="total, sizes, prev, pager, next" background small />
    </div>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" :title="detail?.messageTitle" width="700px" destroy-on-close>
      <div v-if="detail" class="notice-detail">
        <div class="detail-meta">
          <span><el-icon><UserFilled /></el-icon> {{ detail.adminUsername || detail.adminId }}</span>
          <span><el-icon><Clock /></el-icon> {{ detail.createDate }}</span>
          <el-tag v-if="detail.isRead === 0" size="small" type="danger">未读</el-tag>
          <el-tag v-else size="small" type="info">已读</el-tag>
        </div>
        <el-divider />
        <div class="detail-content" v-html="detail.messageContent || '--'"></div>
      </div>
      <template #footer><el-button @click="detailVisible=false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { Bell, UserFilled, Clock, Search, Refresh } from '@element-plus/icons-vue'
import { myMessageList, markMsgRead } from '@/api/system/adminMessageRecord'

const loading = ref(false)
const notices = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const searchForm = reactive({ keyword: '', isRead: '' })

function handleSearch() { page.value = 1; fetchData() }
function handleReset() { searchForm.keyword = ''; searchForm.isRead = ''; page.value = 1; fetchData() }

async function fetchData() {
  loading.value = true
  try {
    const r = await myMessageList({ page: page.value, size: size.value, keyword: searchForm.keyword || undefined, isRead: searchForm.isRead !== '' ? searchForm.isRead : undefined })
    notices.value = r.data.list || r.data || []
    total.value = r.data.total || 0
  } finally { loading.value = false }
}
onMounted(() => fetchData())
watch(() => page.value, fetchData)
watch(() => size.value, () => { page.value = 1; fetchData() })

const detailVisible = ref(false)
const detail = ref(null)
function handleView(item) {
  detail.value = item; detailVisible.value = true
  if (item.isRead === 0) { markMsgRead(item.id).catch(()=>{}); item.isRead = 1 }
}
</script>

<style lang="scss" scoped>
.my-messages-page { padding: 0; }
.search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding: 16px 20px 0; } }
.notice-list { display: flex; flex-direction: column; gap: 12px; }
.notice-card {
  display: flex; gap: 16px; background: #fff; border-radius: 8px; padding: 20px 24px;
  cursor: pointer; transition: all 0.2s; border: 1px solid #ebeef5;
  &:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.06); border-color: #409eff; }
  &.unread { background: #f0f7ff; border-color: #cce0ff; }
}
.notice-card-left { flex-shrink: 0; .notice-avatar { width: 40px; height: 40px; border-radius: 50%; background: linear-gradient(135deg, #409eff, #337ecc); display: flex; align-items: center; justify-content: center; color: #fff; } }
.notice-card-body { flex: 1; min-width: 0; }
.notice-card-header { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; .notice-card-title { font-size: 15px; font-weight: 600; color: #303133; display: flex; align-items: center; gap: 6px; } .unread-dot { width: 8px; height: 8px; border-radius: 50%; background: #f56c6c; flex-shrink: 0; } }
.notice-card-excerpt { font-size: 13px; color: #909399; line-height: 1.7; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; margin-bottom: 12px; }
.notice-card-meta { display: flex; gap: 20px; font-size: 12px; color: #c0c4cc; .meta-item { display: flex; align-items: center; gap: 4px; } }
.notice-detail { .detail-meta { display: flex; align-items: center; gap: 16px; font-size: 13px; color: #909399; span { display: flex; align-items: center; gap: 4px; } } .detail-content { font-size: 14px; color: #303133; line-height: 2; min-height: 80px; word-break: break-word; } }
.pagination-wrap { display: flex; justify-content: center; margin-top: 20px; }
</style>
