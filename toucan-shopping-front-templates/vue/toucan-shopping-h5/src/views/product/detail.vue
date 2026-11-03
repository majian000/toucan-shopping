<template>
  <div class="product-detail page-scroll safe-top">
    <!-- 返回 -->
    <div class="back-bar" @click="$router.back()">
      <span class="back-icon">‹</span>
      <span>返回</span>
    </div>

    <!-- 主图 + 预览图 -->
    <div class="gallery">
      <img v-if="detail.httpMainPhotoFilePath || detail.httpProductPreviewPath" :src="detail.httpMainPhotoFilePath || detail.httpProductPreviewPath" class="main-img" />
      <div v-if="detail.httpPreviewPhotoPaths && detail.httpPreviewPhotoPaths.length" class="preview-scroll">
        <img v-for="(p, i) in detail.httpPreviewPhotoPaths" :key="i" :src="p" class="preview-img" />
      </div>
    </div>

    <!-- 价格 + 名称 -->
    <div class="info-card">
      <div class="price-row">
        <span class="price">¥{{ detail.price }}</span>
        <span class="stock">库存 {{ detail.stockNum }}</span>
      </div>
      <div class="name">{{ detail.name }}</div>
      <div v-if="detail.categoryBrands && detail.categoryBrands.length" class="breadcrumb">
        <span v-for="(cb, i) in detail.categoryBrands" :key="i">
          {{ cb.name }}<span v-if="i < detail.categoryBrands.length - 1"> / </span>
        </span>
      </div>
      <div v-if="detail.shopId" class="shop-link" @click="$router.push('/shop?id=' + detail.shopId)">进入店铺 ›</div>
    </div>

    <!-- 商品介绍 -->
    <div v-if="detail.shopProductDescriptionVO && detail.shopProductDescriptionVO.productDescriptionImgs && detail.shopProductDescriptionVO.productDescriptionImgs.length" class="desc-card">
      <div class="section-title">商品介绍</div>
      <div class="desc-imgs">
        <img v-for="(img, i) in detail.shopProductDescriptionVO.productDescriptionImgs" :key="i" :src="img.httpFilePath" class="desc-img" />
      </div>
    </div>

    <div v-if="loading" class="detail-loading">加载中...</div>

    <!-- 底部操作栏 -->
    <div class="bottom-bar safe-bottom">
      <button class="btn-cart" @click="handleAddCart">加入购物车</button>
      <button class="btn-buy" disabled>立即购买</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getProductDetail } from '@/api/product';
import { saveCart } from '@/api/cart';
import { isLogin } from '@/utils/auth';
import { toast, toastSuccess } from '@/utils/toast';

defineOptions({ name: 'ProductDetail' });

const route = useRoute();
const router = useRouter();
const detail = ref({});
const loading = ref(true);

onMounted(async () => {
  const id = route.query.id;
  if (!id) {
    toast('缺少商品参数', 'warning');
    loading.value = false;
    return;
  }
  try {
    const res = await getProductDetail({ id });
    detail.value = (res && res.data) || {};
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
});

async function handleAddCart() {
  if (!isLogin()) {
    router.push('/login?redirect=' + encodeURIComponent(route.fullPath));
    return;
  }
  if (!detail.value.id) {
    toast('缺少商品参数', 'warning');
    return;
  }
  try {
    await saveCart({ shopProductSkuId: detail.value.id, buyCount: 1 });
    toastSuccess('已加入购物车');
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  }
}
</script>

<style lang="scss" scoped>
.product-detail {
  padding-bottom: 60px;

  .back-bar {
    display: flex;
    align-items: center;
    gap: $gap-xs;
    padding: $gap-sm $gap-md;
    font-size: 15px;
    color: $text-regular;

    .back-icon {
      font-size: 22px;
      line-height: 1;
    }
  }

  .gallery {
    .main-img {
      width: 100%;
      height: 320px;
      object-fit: contain;
      background: $bg-card;
    }
    .preview-scroll {
      display: flex;
      gap: $gap-sm;
      padding: $gap-sm $gap-md;
      overflow-x: auto;
      -webkit-overflow-scrolling: touch;

      .preview-img {
        width: 64px;
        height: 64px;
        object-fit: cover;
        border-radius: $radius-sm;
        border: 1px solid $border-color;
      }
    }
  }

  .info-card {
    margin: $gap-md;
    padding: $gap-md;
    background: $bg-card;
    border-radius: $radius-md;

    .price-row {
      display: flex;
      align-items: baseline;
      gap: $gap-md;

      .price {
        font-size: 24px;
        font-weight: 600;
        color: $danger;
      }
      .stock {
        font-size: 13px;
        color: $text-secondary;
      }
    }
    .name {
      margin-top: $gap-sm;
      font-size: 16px;
      color: $text-primary;
      line-height: 1.5;
    }
    .breadcrumb {
      margin-top: $gap-sm;
      font-size: 13px;
      color: $text-secondary;
    }
    .shop-link {
      margin-top: $gap-sm;
      font-size: 14px;
      color: $primary;
    }
  }

  .desc-card {
    margin: 0 $gap-md $gap-md;
    padding: $gap-md;
    background: $bg-card;
    border-radius: $radius-md;

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: $text-primary;
      margin-bottom: $gap-md;
    }
    .desc-imgs {
      display: flex;
      flex-direction: column;
      gap: $gap-sm;

      .desc-img {
        width: 100%;
        border-radius: $radius-sm;
      }
    }
  }

  .detail-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }

  .bottom-bar {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    gap: $gap-sm;
    padding: $gap-sm $gap-md;
    background: $bg-card;
    border-top: 1px solid $border-color;

    .btn-cart,
    .btn-buy {
      flex: 1;
      height: 44px;
      border: none;
      border-radius: $radius-md;
      font-size: 15px;
      color: #fff;
      cursor: pointer;

      &:disabled {
        opacity: 0.5;
      }
    }
    .btn-cart {
      background: $primary-light;
    }
    .btn-buy {
      background: $danger;
    }
  }
}
</style>
