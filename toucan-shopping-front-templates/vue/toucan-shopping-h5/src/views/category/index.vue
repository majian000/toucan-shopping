<template>
  <div class="category page-scroll safe-top has-tabbar">
    <div class="category-head">
      <h2 class="category-title">商品分类</h2>
    </div>

    <div v-if="list.length" class="category-list">
      <div v-for="c in list" :key="c.id" class="category-row">
        <div class="category-row-head">
          <span class="category-row-name">{{ c.name }}</span>
        </div>
        <div v-if="c.children && c.children.length" class="category-children">
          <span v-for="child in c.children" :key="child.id" class="category-child">
            {{ child.name }}
          </span>
        </div>
      </div>
    </div>

    <div v-else-if="!loading" class="category-empty">暂无分类数据</div>
    <div v-if="loading" class="category-loading">加载中...</div>

    <TabBar active="category" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getNavigation } from '@/api/index';
import TabBar from '@/components/TabBar.vue';

defineOptions({ name: 'Category' });

const list = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const res = await getNavigation();
    list.value = (res && res.data) || [];
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
});
</script>

<style lang="scss" scoped>
.category {
  padding: $gap-md;

  .category-head {
    .category-title {
      margin: 0 0 $gap-md;
      font-size: 18px;
      color: $text-primary;
    }
  }

  .category-list {
    .category-row {
      background: $bg-card;
      border-radius: $radius-md;
      padding: $gap-md;
      margin-bottom: $gap-sm;

      .category-row-head {
        .category-row-name {
          font-size: 15px;
          font-weight: 600;
          color: $text-primary;
        }
      }

      .category-children {
        margin-top: $gap-sm;
        display: flex;
        flex-wrap: wrap;
        gap: $gap-sm;

        .category-child {
          padding: 6px 12px;
          background: $bg-page;
          border-radius: $radius-sm;
          font-size: 13px;
          color: $text-regular;
        }
      }
    }
  }

  .category-empty,
  .category-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }
}
</style>
