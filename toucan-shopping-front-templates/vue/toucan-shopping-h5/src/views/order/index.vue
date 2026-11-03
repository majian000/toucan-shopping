<template>
  <div class="order page-scroll safe-top">
    <div class="order-head">
      <h2 class="order-title">我的订单</h2>
    </div>

    <div v-if="list.length" class="order-list">
      <div v-for="order in list" :key="order.orderNo" class="order-card">
        <div class="order-head-row">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <span class="order-status" :class="statusClass(order)">{{ statusText(order) }}</span>
        </div>
        <div v-if="order.orderItems && order.orderItems.length" class="order-items">
          <div v-for="item in order.orderItems" :key="item.id" class="order-item">
            <img :src="item.httpProductPreviewPath" class="item-img" />
            <div class="item-info">
              <div class="item-name">{{ item.productSkuName }}</div>
              <div class="item-price">¥{{ item.productPrice }} × {{ item.productNum }}</div>
            </div>
          </div>
        </div>
        <div class="order-foot">
          <span class="order-time">{{ order.createDate }}</span>
          <span class="order-total">合计 ¥{{ order.totalAmount }}</span>
        </div>
      </div>
    </div>

    <div v-else-if="!loading" class="order-empty">暂无订单</div>
    <div v-if="loading" class="order-loading">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { orderList } from '@/api/order';

defineOptions({ name: 'Order' });

const list = ref([]);
const loading = ref(true);

function statusText(order) {
  if (order.tradeStatus === 2) return '已取消';
  if (order.tradeStatus === 3) return '已完成';
  if (order.payStatus === 0) return '待付款';
  if (order.tradeStatus === 4) return '待发货';
  if (order.tradeStatus === 1) return '待收货';
  return '';
}

function statusClass(order) {
  if (order.payStatus === 0) return 'danger';
  return '';
}

onMounted(async () => {
  try {
    const res = await orderList({ page: 1, limit: 20 });
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
.order {
  padding: $gap-md;

  .order-head {
    .order-title {
      margin: 0 0 $gap-md;
      font-size: 18px;
      color: $text-primary;
    }
  }

  .order-list {
    .order-card {
      padding: $gap-md;
      margin-bottom: $gap-sm;
      background: $bg-card;
      border-radius: $radius-md;

      .order-head-row {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .order-no {
          font-size: 13px;
          color: $text-secondary;
        }
        .order-status {
          font-size: 14px;
          font-weight: 600;
          color: $success;

          &.danger {
            color: $danger;
          }
        }
      }

      .order-items {
        margin-top: $gap-sm;

        .order-item {
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

      .order-foot {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: $gap-sm;
        padding-top: $gap-sm;
        border-top: 1px solid $border-color;

        .order-time {
          font-size: 12px;
          color: $text-placeholder;
        }
        .order-total {
          font-size: 15px;
          font-weight: 600;
          color: $danger;
        }
      }
    }
  }

  .order-empty,
  .order-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }
}
</style>
