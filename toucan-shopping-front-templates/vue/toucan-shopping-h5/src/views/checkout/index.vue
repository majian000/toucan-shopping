<template>
  <div class="checkout page-scroll safe-top">
    <div class="checkout-head">
      <h2 class="checkout-title">确认订单</h2>
    </div>

    <!-- 收货地址 -->
    <div class="section">
      <div class="section-title">收货地址</div>
      <div v-if="address && address.id" class="address-card" @click="$router.push('/address')">
        <div class="addr-line1">
          <span class="addr-name">{{ address.name }}</span>
          <span class="addr-phone">{{ address.phone }}</span>
        </div>
        <div class="addr-full">{{ address.provinceName }}{{ address.cityName }}{{ address.areaName }} {{ address.address }}</div>
      </div>
      <div v-else class="address-empty" @click="$router.push('/address')">请先添加收货地址 ›</div>
    </div>

    <!-- 商品列表 -->
    <div class="section">
      <div class="section-title">商品清单</div>
      <div class="items">
        <div v-for="item in cartItems" :key="item.id" class="item">
          <img :src="item.httpProductImgPath" class="item-img" />
          <div class="item-info">
            <div class="item-name">{{ item.productSkuName }}</div>
            <div class="item-price">¥{{ item.productPrice }} × {{ item.buyCount }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 合计 -->
    <div class="section total-row">
      <span>合计</span>
      <span class="total-amount">¥{{ totalAmount }}</span>
    </div>

    <!-- 提交 -->
    <div class="submit-bar safe-bottom">
      <button class="submit-btn" :disabled="submitting" @click="handleSubmit">
        {{ submitting ? '提交中...' : '提交订单' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { cartList } from '@/api/cart';
import { defaultAddress } from '@/api/address';
import { createOrder } from '@/api/order';
import { toast, toastSuccess } from '@/utils/toast';

defineOptions({ name: 'Checkout' });

const router = useRouter();
const cartItems = ref([]);
const address = ref({});
const submitting = ref(false);

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + Number(item.productPrice || 0) * Number(item.buyCount || 0), 0).toFixed(2);
});

onMounted(async () => {
  try {
    const [cartRes, addrRes] = await Promise.all([cartList(), defaultAddress()]);
    cartItems.value = (cartRes && cartRes.data) || [];
    address.value = (addrRes && addrRes.data) || {};
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  }
});

async function handleSubmit() {
  if (!cartItems.value.length) {
    toast('购物车为空', 'warning');
    return;
  }
  if (!address.value || !address.value.id) {
    toast('请先选择收货地址', 'warning');
    return;
  }
  submitting.value = true;
  try {
    const buyCarItems = cartItems.value.map(item => ({
      id: item.id,
      buyCount: item.buyCount,
      selectTransportModel: item.selectTransportModel
    }));
    await createOrder({
      buyCarItems,
      consigneeAddress: { id: address.value.id },
      payType: 1,
      srcType: 3,
      remark: ''
    });
    toastSuccess('下单成功');
    router.replace('/order');
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    submitting.value = false;
  }
}
</script>

<style lang="scss" scoped>
.checkout {
  padding: $gap-md;
  padding-bottom: 80px;

  .checkout-head {
    .checkout-title {
      margin: 0 0 $gap-md;
      font-size: 18px;
      color: $text-primary;
    }
  }

  .section {
    margin-bottom: $gap-md;
    background: $bg-card;
    border-radius: $radius-md;
    padding: $gap-md;

    .section-title {
      font-size: 15px;
      font-weight: 600;
      color: $text-primary;
      margin-bottom: $gap-sm;
    }

    .address-card {
      .addr-line1 {
        display: flex;
        gap: $gap-sm;

        .addr-name {
          font-size: 15px;
          font-weight: 600;
          color: $text-primary;
        }
        .addr-phone {
          font-size: 14px;
          color: $text-regular;
        }
      }
      .addr-full {
        margin-top: 4px;
        font-size: 14px;
        color: $text-regular;
      }
    }
    .address-empty {
      color: $primary;
      font-size: 14px;
    }

    .items {
      .item {
        display: flex;
        gap: $gap-sm;
        padding: $gap-sm 0;

        .item-img {
          width: 56px;
          height: 56px;
          object-fit: cover;
          border-radius: $radius-sm;
        }
        .item-info {
          flex: 1;
          min-width: 0;

          .item-name {
            font-size: 14px;
            color: $text-primary;
            display: -webkit-box;
            -webkit-line-clamp: 1;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }
          .item-price {
            margin-top: 4px;
            font-size: 13px;
            color: $text-regular;
          }
        }
      }
    }

    .total-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 15px;
      color: $text-primary;

      .total-amount {
        font-size: 18px;
        font-weight: 600;
        color: $danger;
      }
    }
  }

  .submit-bar {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: $gap-sm $gap-md;
    background: $bg-card;
    border-top: 1px solid $border-color;

    .submit-btn {
      width: 100%;
      height: 44px;
      border: none;
      border-radius: $radius-md;
      background: $danger;
      color: #fff;
      font-size: 16px;
      cursor: pointer;

      &:disabled {
        opacity: 0.6;
      }
    }
  }
}
</style>
