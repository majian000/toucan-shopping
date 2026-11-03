<template>
  <div class="collect page-scroll safe-top">
    <div class="collect-head">
      <h2 class="collect-title">我的收藏</h2>
    </div>

    <div v-if="list.length" class="collect-list">
      <div v-for="item in list" :key="item.productSkuId" class="collect-card" @click="$router.push('/product/detail?id=' + item.productSkuId)">
        <img :src="item.httpProductImgPath" class="collect-img" />
        <div class="collect-info">
          <div class="collect-name">{{ item.productSkuName }}</div>
          <div class="collect-price">¥{{ item.productPrice }}</div>
        </div>
      </div>
    </div>

    <div v-else-if="!loading" class="collect-empty">暂无收藏</div>
    <div v-if="loading" class="collect-loading">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { collectList } from '@/api/user';

defineOptions({ name: 'Collect' });

const list = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const res = await collectList({ page: 1, limit: 100 });
    const data = (res && res.data) || {};
    list.value = data.list || [];
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
});
</script>

<style lang="scss" scoped>
.collect {
  padding: $gap-md;

  .collect-head {
    .collect-title {
      margin: 0 0 $gap-md;
      font-size: 18px;
      color: $text-primary;
    }
  }

  .collect-list {
    .collect-card {
      display: flex;
      gap: $gap-md;
      padding: $gap-md 0;
      border-bottom: 1px solid $border-color;

      .collect-img {
        width: 80px;
        height: 80px;
        object-fit: cover;
        border-radius: $radius-sm;
      }
      .collect-info {
        flex: 1;
        min-width: 0;

        .collect-name {
          font-size: 15px;
          color: $text-primary;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
        .collect-price {
          margin-top: 6px;
          font-size: 16px;
          font-weight: 600;
          color: $danger;
        }
      }
    }
  }

  .collect-empty,
  .collect-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }
}
</style>
