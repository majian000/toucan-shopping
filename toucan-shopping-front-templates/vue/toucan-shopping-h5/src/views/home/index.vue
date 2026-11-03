<template>
  <div class="home page-scroll">
    <!-- 顶部搜索入口 + 我的 -->
    <div class="home-top safe-top">
      <div class="home-search" @click="$router.push('/search')">
        <span class="search-icon">🔍</span>
        <span class="search-text">搜索商品</span>
      </div>
      <span class="my-entry" @click="$router.push('/my')">我的</span>
    </div>

    <!-- 轮播图 -->
    <div v-if="banners.length" class="banner">
      <div class="banner-scroll">
        <div v-for="(b, i) in banners" :key="i" class="banner-item">
          <img :src="b.httpImgPath || b.imgPath" :alt="b.title" />
        </div>
      </div>
    </div>

    <!-- 分类 -->
    <div v-if="categorys.length" class="category-section">
      <div class="section-title">商品分类</div>
      <div class="category-grid">
        <div v-for="c in categorys" :key="c.id" class="category-item" @click="$router.push('/category')">
          <img v-if="c.icon" :src="c.icon" class="category-icon" />
          <span v-else class="category-icon-placeholder">{{ (c.name || '').charAt(0) }}</span>
          <span class="category-name">{{ c.name }}</span>
        </div>
      </div>
    </div>

    <!-- 热门商品 -->
    <div v-if="hotProducts.length" class="hot-section">
      <div class="section-title">热门商品</div>
      <div class="product-grid">
        <div v-for="p in hotProducts" :key="p.id" class="product-card">
          <img :src="p.httpImgPath" :alt="p.productName" class="product-img" />
          <div class="product-info">
            <div class="product-name">{{ p.productName }}</div>
            <div class="product-desc">{{ p.productDesc }}</div>
            <div class="product-price">¥{{ p.productPrice }}</div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="loading" class="home-loading">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getBanners, getCategorys, getHotProducts } from '@/api/index';

defineOptions({ name: 'Home' });

const banners = ref([]);
const categorys = ref([]);
const hotProducts = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const [b, c, h] = await Promise.all([
      getBanners(),
      getCategorys(),
      getHotProducts()
    ]);
    banners.value = (b && b.data) || [];
    categorys.value = (c && c.data) || [];
    hotProducts.value = (h && h.data) || [];
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
});
</script>

<style lang="scss" scoped>
.home {
  .home-top {
    display: flex;
    align-items: center;
    gap: $gap-sm;
    margin: $gap-sm $gap-md $gap-md;

    .my-entry {
      flex-shrink: 0;
      font-size: 14px;
      color: $text-primary;
    }
  }

  .home-search {
    flex: 1;
    display: flex;
    align-items: center;
    gap: $gap-sm;
    padding: 10px 14px;
    background: $bg-card;
    border-radius: $radius-lg;
    color: $text-placeholder;
    font-size: 14px;

    .search-icon {
      font-size: 16px;
    }
  }

  .banner {
    margin: 0 $gap-md;

    .banner-scroll {
      display: flex;
      gap: $gap-sm;
      overflow-x: auto;
      -webkit-overflow-scrolling: touch;

      .banner-item {
        flex: 0 0 88%;
        border-radius: $radius-md;
        overflow: hidden;

        img {
          width: 100%;
          height: 150px;
          object-fit: cover;
          border-radius: $radius-md;
        }
      }
    }
  }

  .category-section,
  .hot-section {
    margin: $gap-lg $gap-md 0;
  }

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: $text-primary;
    margin-bottom: $gap-md;
  }

  .category-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: $gap-sm;

    .category-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: $gap-xs;
      padding: $gap-sm;
      background: $bg-card;
      border-radius: $radius-md;

      .category-icon {
        width: 36px;
        height: 36px;
        object-fit: contain;
      }
      .category-icon-placeholder {
        width: 36px;
        height: 36px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        background: $primary-light;
        color: #fff;
        font-size: 16px;
      }
      .category-name {
        font-size: 12px;
        color: $text-regular;
      }
    }
  }

  .product-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: $gap-sm;

    .product-card {
      background: $bg-card;
      border-radius: $radius-md;
      overflow: hidden;

      .product-img {
        width: 100%;
        height: 140px;
        object-fit: cover;
      }
      .product-info {
        padding: $gap-sm;

        .product-name {
          font-size: 14px;
          color: $text-primary;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .product-desc {
          margin-top: 2px;
          font-size: 12px;
          color: $text-secondary;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .product-price {
          margin-top: $gap-xs;
          font-size: 15px;
          font-weight: 600;
          color: $danger;
        }
      }
    }
  }

  .home-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }
}
</style>
