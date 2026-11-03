<template>
  <div class="shop page-scroll safe-top">
    <!-- 轮播图 -->
    <div v-if="banners.length" class="banner">
      <div class="banner-scroll">
        <div v-for="(b, i) in banners" :key="i" class="banner-item">
          <img :src="b.httpImgPath || b.imgPath" />
        </div>
      </div>
    </div>

    <!-- 分类 -->
    <div v-if="categorys.length" class="category-section">
      <div class="section-title">店铺分类</div>
      <div class="category-scroll">
        <span v-for="c in categorys" :key="c.id" class="category-chip">{{ c.name }}</span>
      </div>
    </div>

    <!-- 商品 -->
    <div class="section-title">店铺商品</div>
    <div v-if="products.length" class="product-grid">
      <div v-for="p in products" :key="p.id" class="product-card" @click="$router.push('/product/detail?id=' + p.id)">
        <img :src="p.httpProductPreviewPath" class="product-img" />
        <div class="product-info">
          <div class="product-name">{{ p.name }}</div>
          <div class="product-price">¥{{ p.price }}</div>
        </div>
      </div>
    </div>
    <div v-else-if="!loading" class="shop-empty">暂无商品</div>
    <div v-if="loading" class="shop-loading">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { shopBanners, shopCategorys } from '@/api/shop';
import { searchProduct } from '@/api/search';

defineOptions({ name: 'Shop' });

const route = useRoute();
const shopId = ref(route.query.id || '');
const banners = ref([]);
const categorys = ref([]);
const products = ref([]);
const loading = ref(true);

onMounted(async () => {
  if (!shopId.value) return;
  try {
    const [b, c, p] = await Promise.all([
      shopBanners({ shopId: shopId.value }),
      shopCategorys({ shopId: shopId.value }),
      searchProduct({ sid: shopId.value, page: 1, size: 50 })
    ]);
    banners.value = (b && b.data) || [];
    categorys.value = (c && c.data) || [];
    const pdata = (p && p.data) || {};
    products.value = pdata.list || [];
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
});
</script>

<style lang="scss" scoped>
.shop {
  padding: $gap-md;

  .banner {
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
          height: 140px;
          object-fit: cover;
        }
      }
    }
  }

  .category-section {
    margin-top: $gap-lg;
  }

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: $text-primary;
    margin: $gap-md 0;
  }

  .category-scroll {
    display: flex;
    gap: $gap-sm;
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;

    .category-chip {
      flex-shrink: 0;
      padding: 6px 14px;
      background: $bg-card;
      border-radius: $radius-lg;
      font-size: 13px;
      color: $text-regular;
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
        .product-price {
          margin-top: 4px;
          font-size: 15px;
          font-weight: 600;
          color: $danger;
        }
      }
    }
  }

  .shop-empty,
  .shop-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }
}
</style>
