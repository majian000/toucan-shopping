<template>
  <div class="register page-scroll safe-top">
    <div class="register-head">
      <h1 class="register-title">注册</h1>
      <p class="register-sub">注册功能将在后续里程碑补齐</p>
    </div>

    <div class="register-actions">
      <button class="btn-plain" @click="$router.replace('/login')">返回登录</button>
    </div>
  </div>
</template>

<script setup>
defineOptions({ name: 'Register' });
</script>

<style lang="scss" scoped>
.register {
  padding: $gap-lg;
  text-align: center;

  .register-head {
    padding-top: 16vh;

    .register-title {
      margin: 0;
      font-size: 24px;
      color: $text-primary;
    }
    .register-sub {
      margin: $gap-sm 0 0;
      color: $text-secondary;
      font-size: 14px;
    }
  }

  .register-actions {
    margin-top: $gap-lg;

    .btn-plain {
      width: 70%;
      max-width: 300px;
      height: 44px;
      border: 1px solid $border-color;
      border-radius: $radius-md;
      background: $bg-card;
      color: $text-regular;
      font-size: 15px;
      cursor: pointer;
    }
  }
}
</style>
