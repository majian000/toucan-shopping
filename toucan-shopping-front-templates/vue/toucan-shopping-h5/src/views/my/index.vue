<template>
  <div class="my page-scroll safe-top has-tabbar">
    <!-- 用户信息 -->
    <div class="user-card">
      <template v-if="isLogin">
        <img v-if="userInfo.httpHeadSculpture" :src="userInfo.httpHeadSculpture" class="avatar" />
        <span v-else class="avatar-placeholder">👤</span>
        <div class="user-meta">
          <div class="user-name">{{ userInfo.nickName || userInfo.userName || userInfo.mobilePhone || ('用户' + uid) }}</div>
          <div class="user-sub">欢迎回来</div>
        </div>
      </template>
      <template v-else>
        <span class="avatar-placeholder">👤</span>
        <div class="user-meta">
          <div class="user-name">未登录</div>
          <div class="user-sub">登录后可查看收藏、消息等</div>
        </div>
      </template>
    </div>

    <!-- 菜单 -->
    <div class="menu-list">
      <div v-if="isLogin" class="menu-item" @click="$router.push('/collect')">
        <span>❤️ 我的收藏</span>
        <span class="menu-arrow">›</span>
      </div>
      <div v-if="isLogin" class="menu-item" @click="$router.push('/message')">
        <span>📮 我的消息</span>
        <span class="menu-arrow">›</span>
      </div>
      <div v-if="isLogin" class="menu-item" @click="$router.push('/address')">
        <span>📍 收货地址</span>
        <span class="menu-arrow">›</span>
      </div>
      <div v-if="isLogin" class="menu-item" @click="$router.push('/cart')">
        <span>🛒 购物车</span>
        <span class="menu-arrow">›</span>
      </div>
      <div v-if="isLogin" class="menu-item" @click="$router.push('/order')">
        <span>📦 我的订单</span>
        <span class="menu-arrow">›</span>
      </div>
      <div v-if="!isLogin" class="menu-item" @click="$router.push('/login')">
        <span>登录 / 注册</span>
        <span class="menu-arrow">›</span>
      </div>
      <div v-if="isLogin" class="menu-item" @click="handleLogout">
        <span>退出登录</span>
        <span class="menu-arrow">›</span>
      </div>
    </div>

    <TabBar active="my" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { isLogin, getUid, clearLogin } from '@/utils/auth';
import { getLoginInfo, logout } from '@/api/user';
import TabBar from '@/components/TabBar.vue';
import { toastSuccess } from '@/utils/toast';

defineOptions({ name: 'My' });

const router = useRouter();
const uid = ref(getUid() || '');
const userInfo = ref({});

onMounted(async () => {
  if (isLogin()) {
    try {
      const res = await getLoginInfo();
      userInfo.value = (res && res.data) || {};
    } catch (e) {
      // 错误提示已在 request.js 统一处理
    }
  }
});

async function handleLogout() {
  try {
    await logout();
  } catch (e) {
    // 忽略
  }
  clearLogin();
  toastSuccess('已退出登录');
  router.replace('/');
}
</script>

<style lang="scss" scoped>
.my {
  .user-card {
    display: flex;
    align-items: center;
    gap: $gap-md;
    margin: $gap-md;
    padding: $gap-lg $gap-md;
    background: $bg-card;
    border-radius: $radius-md;

    .avatar {
      width: 56px;
      height: 56px;
      border-radius: 50%;
      object-fit: cover;
    }
    .avatar-placeholder {
      font-size: 40px;
    }
    .user-meta {
      .user-name {
        font-size: 17px;
        font-weight: 600;
        color: $text-primary;
      }
      .user-sub {
        margin-top: 4px;
        font-size: 13px;
        color: $text-secondary;
      }
    }
  }

  .menu-list {
    margin: $gap-md;
    background: $bg-card;
    border-radius: $radius-md;
    overflow: hidden;

    .menu-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 14px $gap-md;
      font-size: 15px;
      color: $text-primary;
      border-bottom: 1px solid $border-color;

      &:last-child {
        border-bottom: none;
      }
      .menu-arrow {
        color: $text-placeholder;
        font-size: 20px;
      }
    }
  }
}
</style>
