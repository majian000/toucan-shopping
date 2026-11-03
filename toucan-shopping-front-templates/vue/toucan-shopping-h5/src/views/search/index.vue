<template>
  <div class="search page-scroll safe-top">
    <!-- 搜索框 -->
    <div class="search-bar">
      <input
        v-model.trim="keyword"
        type="search"
        placeholder="搜索商品"
        class="search-input"
        @keyup.enter="handleSearch"
      />
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>

    <!-- 结果列表 -->
    <div v-if="list.length" class="result-list">
      <div v-for="item in list" :key="item.id" class="product-card" @click="$router.push('/product/detail?id=' + item.id)">
        <img :src="item.httpProductPreviewPath" :alt="item.name" class="product-img" />
        <div class="product-info">
          <div class="product-name">{{ item.name }}</div>
          <div class="product-brand">{{ item.brandName }}</div>
          <div class="product-price">¥{{ item.price }}</div>
        </div>
      </div>
    </div>

    <div v-else-if="!loading && searched" class="search-empty">未找到相关商品</div>
    <div v-if="loading" class="search-loading">加载中...</div>

    <!-- 加载更多 -->
    <div v-if="hasMore && !loading" class="load-more" @click="loadMore">加载更多</div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { searchProduct } from '@/api/search';

defineOptions({ name: 'Search' });

const keyword = ref('');
const list = ref([]);
const page = ref(1);
const size = 20;
const hasMore = ref(false);
const loading = ref(false);
const searched = ref(false);

async function doSearch(reset) {
  if (reset) {
    page.value = 1;
    list.value = [];
  }
  loading.value = true;
  try {
    const res = await searchProduct({ keyword: keyword.value || undefined, page: page.value, size });
    const data = (res && res.data) || {};
    const newList = data.list || [];
    if (reset) {
      list.value = newList;
    } else {
      list.value = list.value.concat(newList);
    }
    const total = Number(data.total || 0);
    hasMore.value = list.value.length < total;
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
    searched.value = true;
  }
}

function handleSearch() {
  doSearch(true);
}

function loadMore() {
  page.value += 1;
  doSearch(false);
}
</script>

<style lang="scss" scoped>
.search {
  .search-bar {
    display: flex;
    gap: $gap-sm;
    padding: $gap-sm $gap-md;
    position: sticky;
    top: 0;
    background: $bg-page;
    z-index: 10;

    .search-input {
      flex: 1;
      height: 38px;
      padding: 0 14px;
      border: 1px solid $border-color;
      border-radius: $radius-lg;
      background: $bg-card;
      font-size: 14px;
      outline: none;

      &:focus {
        border-color: $primary;
      }
    }
    .search-btn {
      height: 38px;
      padding: 0 16px;
      border: none;
      border-radius: $radius-lg;
      background: $primary;
      color: #fff;
      font-size: 14px;
      cursor: pointer;
    }
  }

  .result-list {
    padding: 0 $gap-md;

    .product-card {
      display: flex;
      gap: $gap-md;
      padding: $gap-md 0;
      border-bottom: 1px solid $border-color;

      .product-img {
        width: 90px;
        height: 90px;
        object-fit: cover;
        border-radius: $radius-sm;
      }
      .product-info {
        flex: 1;
        min-width: 0;

        .product-name {
          font-size: 15px;
          color: $text-primary;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
        .product-brand {
          margin-top: 4px;
          font-size: 12px;
          color: $text-secondary;
        }
        .product-price {
          margin-top: 6px;
          font-size: 16px;
          font-weight: 600;
          color: $danger;
        }
      }
    }
  }

  .search-empty,
  .search-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }

  .load-more {
    margin: $gap-md;
    padding: $gap-sm;
    text-align: center;
    color: $primary;
    font-size: 14px;
    background: $bg-card;
    border-radius: $radius-md;
    cursor: pointer;
  }
}
</style>
