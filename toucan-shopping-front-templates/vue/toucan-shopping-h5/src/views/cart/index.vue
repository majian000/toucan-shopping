<template>
  <div class="cart page-scroll safe-top">
    <div class="cart-head">
      <h2 class="cart-title">购物车</h2>
      <button v-if="list.length" class="clear-btn" @click="handleClear">清空</button>
    </div>

    <div v-if="list.length" class="cart-list">
      <div v-for="item in list" :key="item.id" class="cart-card">
        <img :src="item.httpProductImgPath" class="cart-img" @click="$router.push('/product/detail?id=' + item.shopProductSkuId)" />
        <div class="cart-info">
          <div class="cart-name">{{ item.productSkuName }}</div>
          <div v-if="item.attributePreview" class="cart-attr">{{ item.attributePreview }}</div>
          <div class="cart-bottom">
            <span class="cart-price">¥{{ item.productPrice }}</span>
            <div class="qty">
              <button class="qty-btn" @click="changeQty(item, -1)">-</button>
              <span class="qty-num">{{ item.buyCount }}</span>
              <button class="qty-btn" @click="changeQty(item, 1)">+</button>
            </div>
          </div>
          <div v-if="!item.isAllowedBuy" class="cart-disabled">{{ item.noAllowedBuyDesc }}</div>
        </div>
        <button class="remove-btn" @click="handleRemove(item)">✕</button>
      </div>
    </div>

    <div v-else-if="!loading" class="cart-empty">购物车是空的</div>
    <div v-if="loading" class="cart-loading">加载中...</div>

    <!-- 去结算 -->
    <div v-if="list.length" class="cart-foot safe-bottom">
      <button class="checkout-btn" @click="$router.push('/checkout')">去结算</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { cartList, removeCart, clearCart, updateCart } from '@/api/cart';
import { toastSuccess } from '@/utils/toast';
import { confirm } from '@/utils/toast';

defineOptions({ name: 'Cart' });

const list = ref([]);
const loading = ref(true);

async function load() {
  try {
    const res = await cartList();
    list.value = (res && res.data) || [];
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
}

onMounted(load);

async function changeQty(item, delta) {
  const newCount = (item.buyCount || 1) + delta;
  if (newCount < 1) return;
  try {
    await updateCart({ id: item.id, buyCount: newCount });
    item.buyCount = newCount;
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  }
}

async function handleRemove(item) {
  try {
    await removeCart({ id: item.id });
    toastSuccess('已删除');
    load();
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  }
}

async function handleClear() {
  confirm({ title: '提示', message: '确定清空购物车吗？', danger: true }).then(async ok => {
    if (!ok) return;
    try {
      await clearCart();
      toastSuccess('已清空');
      load();
    } catch (e) {
      // 错误提示已在 request.js 统一处理
    }
  });
}
</script>

<style lang="scss" scoped>
.cart {
  padding: $gap-md;

  .cart-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $gap-md;

    .cart-title {
      margin: 0;
      font-size: 18px;
      color: $text-primary;
    }
    .clear-btn {
      border: none;
      background: none;
      color: $danger;
      font-size: 14px;
      cursor: pointer;
    }
  }

  .cart-list {
    .cart-card {
      display: flex;
      gap: $gap-md;
      padding: $gap-md 0;
      border-bottom: 1px solid $border-color;

      .cart-img {
        width: 80px;
        height: 80px;
        object-fit: cover;
        border-radius: $radius-sm;
        cursor: pointer;
      }
      .cart-info {
        flex: 1;
        min-width: 0;

        .cart-name {
          font-size: 15px;
          color: $text-primary;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
        .cart-attr {
          margin-top: 2px;
          font-size: 12px;
          color: $text-secondary;
        }
        .cart-bottom {
          margin-top: 8px;
          display: flex;
          justify-content: space-between;
          align-items: center;

          .cart-price {
            font-size: 16px;
            font-weight: 600;
            color: $danger;
          }
          .qty {
            display: flex;
            align-items: center;
            gap: 0;

            .qty-btn {
              width: 28px;
              height: 28px;
              border: 1px solid $border-color;
              background: $bg-card;
              color: $text-regular;
              font-size: 16px;
              cursor: pointer;
            }
            .qty-num {
              min-width: 36px;
              text-align: center;
              font-size: 14px;
              color: $text-primary;
            }
          }
        }
        .cart-disabled {
          margin-top: 4px;
          font-size: 12px;
          color: $danger;
        }
      }
      .remove-btn {
        border: none;
        background: none;
        color: $text-placeholder;
        font-size: 16px;
        cursor: pointer;
        padding: 0;
      }
    }
  }

  .cart-empty,
  .cart-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }

  .cart-foot {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: $gap-sm $gap-md;
    background: $bg-card;
    border-top: 1px solid $border-color;

    .checkout-btn {
      width: 100%;
      height: 44px;
      border: none;
      border-radius: $radius-md;
      background: $danger;
      color: #fff;
      font-size: 16px;
      cursor: pointer;
    }
  }
}
</style>
