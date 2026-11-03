<template>
  <div class="login page-scroll safe-top">
    <div class="login-head">
      <h1 class="login-title">{{ ENV.title }}</h1>
    </div>

    <form class="login-form" @submit.prevent="handleLogin">
      <div class="field">
        <input
          v-model.trim="form.loginUserName"
          type="text"
          placeholder="请输入手机号或邮箱"
          autocomplete="username"
        />
      </div>
      <div class="field">
        <input
          v-model="form.password"
          type="password"
          placeholder="请输入密码"
          autocomplete="current-password"
        />
      </div>

      <button type="submit" class="btn-submit" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>

      <div class="login-foot">
        <a class="link" @click="$router.push('/register')">没有账号？去注册</a>
      </div>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ENV from '@/utils/env';
import { setLogin } from '@/utils/auth';
import { login } from '@/api/user';
import { toast } from '@/utils/toast';

defineOptions({ name: 'Login' });

const router = useRouter();
const route = useRoute();
const form = reactive({ loginUserName: '', password: '' });
const loading = ref(false);

async function handleLogin() {
  if (!form.loginUserName) {
    toast('请输入手机号或邮箱', 'warning');
    return;
  }
  if (!form.password) {
    toast('请输入密码', 'warning');
    return;
  }
  loading.value = true;
  try {
    const res = await login({ loginUserName: form.loginUserName, password: form.password });
    // 后端已种 h5_uid/h5_lt/h5_lat cookie；这里再按返回 data 落一遍，保证 isLogin() 立即可用
    if (res && res.data) {
      setLogin(res.data);
    }
    toast('登录成功', 'success');
    const redirect = route.query.redirect ? decodeURIComponent(route.query.redirect) : '/';
    router.replace(redirect || '/');
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
}
</script>

<style lang="scss" scoped>
.login {
  padding: $gap-lg;

  .login-head {
    text-align: center;
    padding-top: 10vh;

    .login-title {
      margin: 0;
      font-size: 24px;
      color: $text-primary;
    }
  }

  .login-form {
    margin-top: $gap-lg;

    .field {
      margin-bottom: $gap-md;

      input {
        width: 100%;
        height: 46px;
        padding: 0 14px;
        border: 1px solid $border-color;
        border-radius: $radius-md;
        background: $bg-card;
        font-size: 15px;
        color: $text-primary;
        outline: none;

        &:focus {
          border-color: $primary;
        }
      }
    }

    .btn-submit {
      width: 100%;
      height: 46px;
      margin-top: $gap-sm;
      border: none;
      border-radius: $radius-md;
      background: $primary;
      color: #fff;
      font-size: 16px;
      cursor: pointer;

      &:disabled {
        opacity: 0.6;
      }
    }

    .login-foot {
      margin-top: $gap-md;
      text-align: center;

      .link {
        color: $primary;
        font-size: 14px;
        cursor: pointer;
      }
    }
  }
}
</style>
