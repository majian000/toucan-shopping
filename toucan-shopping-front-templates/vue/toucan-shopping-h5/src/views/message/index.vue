<template>
  <div class="message page-scroll safe-top">
    <div class="message-head">
      <h2 class="message-title">我的消息</h2>
      <button v-if="list.length" class="read-all" @click="handleReadAll">全部已读</button>
    </div>

    <div v-if="list.length" class="message-list">
      <div v-for="m in list" :key="m.id" class="message-item" :class="{ unread: m.status == 0 }" @click="handleRead(m)">
        <div class="message-item-head">
          <span class="message-item-title">{{ m.title }}</span>
          <span v-if="m.status == 0" class="unread-dot"></span>
        </div>
        <div class="message-item-content">{{ m.content }}</div>
        <div class="message-item-time">{{ m.sendDate }}</div>
      </div>
    </div>

    <div v-else-if="!loading" class="message-empty">暂无消息</div>
    <div v-if="loading" class="message-loading">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { messageList, messageRead, messageReadAll } from '@/api/user';

defineOptions({ name: 'Message' });

const list = ref([]);
const loading = ref(true);

async function load() {
  try {
    const res = await messageList({ page: 1, limit: 100 });
    const data = (res && res.data) || {};
    list.value = data.list || [];
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
}

onMounted(load);

async function handleRead(m) {
  if (m.status == 0) {
    try {
      await messageRead(m.id);
      m.status = 1;
    } catch (e) {
      // 忽略
    }
  }
}

async function handleReadAll() {
  try {
    await messageReadAll();
    list.value.forEach(m => (m.status = 1));
  } catch (e) {
    // 忽略
  }
}
</script>

<style lang="scss" scoped>
.message {
  padding: $gap-md;

  .message-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $gap-md;

    .message-title {
      margin: 0;
      font-size: 18px;
      color: $text-primary;
    }
    .read-all {
      border: 1px solid $border-color;
      background: $bg-card;
      color: $primary;
      border-radius: $radius-sm;
      padding: 4px 12px;
      font-size: 13px;
      cursor: pointer;
    }
  }

  .message-list {
    .message-item {
      padding: $gap-md;
      margin-bottom: $gap-sm;
      background: $bg-card;
      border-radius: $radius-md;

      &.unread {
        background: #ecf5ff;
      }

      .message-item-head {
        display: flex;
        align-items: center;
        gap: $gap-xs;

        .message-item-title {
          font-size: 15px;
          font-weight: 600;
          color: $text-primary;
        }
        .unread-dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          background: $danger;
        }
      }
      .message-item-content {
        margin-top: 6px;
        font-size: 13px;
        color: $text-regular;
        line-height: 1.6;
      }
      .message-item-time {
        margin-top: 6px;
        font-size: 12px;
        color: $text-placeholder;
      }
    }
  }

  .message-empty,
  .message-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }
}
</style>
