<template>
  <div class="login-page">
    <!-- 装饰元素 -->
    <div class="decor-layer">
      <div class="decor-circle c1"></div>
      <div class="decor-circle c2"></div>
      <div class="decor-circle c3"></div>
      <div class="decor-grid"></div>
    </div>

    <div class="login-container">
      <!-- 左侧品牌区 -->
      <div class="brand-panel">
        <div class="brand-content">
          <div class="brand-logo">
            <svg viewBox="0 0 48 48" width="48" height="48" fill="none">
              <rect x="6" y="8" width="36" height="32" rx="4" fill="white" opacity="0.95"/>
              <path d="M16 26l5 6 10-15" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h1 class="brand-name">犀鸟商城</h1>
          <p class="brand-desc">全渠道电商管理平台</p>
        </div>
        <div class="brand-pattern"></div>
      </div>

      <!-- 右侧登录区 -->
      <div class="login-panel">
        <div class="login-header">
          <h2>欢迎回来</h2>
          <p>请登录您的管理账号</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleLogin">
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="登录账号"
              :prefix-icon="User"
              maxlength="24"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="登录密码"
              :prefix-icon="Lock"
              show-password
              maxlength="25"
            />
          </el-form-item>
          <el-form-item prop="vcode">
            <div class="vcode-row">
              <el-input
                v-model="form.vcode"
                placeholder="验证码"
                maxlength="4"
                :prefix-icon="Key"
              />
              <div class="captcha-box" @click="refreshCaptcha" title="点击刷新验证码">
                <img v-if="captchaUrl" :src="captchaUrl" class="captcha-img" />
                <el-icon v-else :size="20"><Loading /></el-icon>
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button class="login-btn" :loading="loading" @click="handleLogin" round>
              {{ loading ? '验证中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          <span>© 2020 - 2025 犀鸟商城 · 粤ICP备16006642号</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Key, Loading } from '@element-plus/icons-vue'
import { login } from '@/api/login'
import { setToken } from '@/utils/auth'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const loading = ref(false)
const captchaKey = ref(Date.now())
const captchaUrl = ref('')
const baseApi = import.meta.env.VITE_APP_BASE_API || ''

function refreshCaptcha() {
  captchaKey.value = Date.now()
  captchaUrl.value = `${baseApi}/login/vcode?t=${captchaKey.value}`
}

onMounted(refreshCaptcha)

const form = reactive({ username: '', password: '', vcode: '' })
const rules = {
  username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  vcode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await login(form.username, form.password, form.vcode)
    setToken(res.data.adminId + ':' + res.data.loginToken)
    ElMessage({ message: '登录成功', type: 'success', duration: 1500 })
    router.replace(route.query.redirect || '/dashboard')
  } catch {
    form.vcode = ''
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  overflow: hidden;
}

// 装饰层
.decor-layer {
  position: absolute;
  inset: 0;
  pointer-events: none;
}
.decor-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.06;
  background: $primary;
}
.c1 { width: 600px; height: 600px; top: -280px; right: -200px; }
.c2 { width: 400px; height: 400px; bottom: -160px; left: -100px; }
.c3 { width: 200px; height: 200px; top: 50%; left: 55%; }
.decor-grid {
  position: absolute; inset: 0;
  background-image:
    linear-gradient(rgba(64,158,255,0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(64,158,255,0.03) 1px, transparent 1px);
  background-size: 80px 80px;
}

// 主容器 — 左右分栏
.login-container {
  position: relative;
  z-index: 1;
  display: flex;
  width: 880px;
  min-height: 520px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1), 0 0 0 1px rgba(0,0,0,0.04);
}

// 左侧品牌面板
.brand-panel {
  flex: 1;
  background: linear-gradient(150deg, #1e3c72 0%, #2a5298 40%, #1e5399 70%, #152c52 100%);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  color: #fff;
}
.brand-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 40px;
}
.brand-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px; height: 80px;
  background: rgba(255,255,255,0.12);
  border-radius: 20px;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
  color: #fff;
}
.brand-name {
  font-size: 26px;
  font-weight: 700;
  margin: 0 0 8px;
  letter-spacing: 4px;
}
.brand-desc {
  font-size: 13px;
  opacity: 0.7;
  margin: 0 0 32px;
  letter-spacing: 2px;
}
.brand-features {
  text-align: left;
  display: inline-block;
}
.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
  font-size: 13px;
  opacity: 0.8;
  .feature-dot {
    width: 6px; height: 6px;
    border-radius: 50%;
    background: #60a5fa;
    margin-right: 12px;
  }
}
// 品牌面板底纹
.brand-pattern {
  position: absolute;
  inset: 0;
  background-image:
    radial-gradient(circle at 20% 80%, rgba(255,255,255,0.03) 1px, transparent 1px),
    radial-gradient(circle at 80% 20%, rgba(255,255,255,0.04) 1px, transparent 1px);
  background-size: 40px 40px, 60px 60px;
}

// 右侧登录面板
.login-panel {
  width: 440px;
  background: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 52px 48px;
}

.login-header {
  margin-bottom: 36px;
  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 700;
    color: $text-primary;
    letter-spacing: 1px;
  }
  p {
    margin: 6px 0 0;
    font-size: 13px;
    color: $text-secondary;
  }
}

:deep(.el-form) {
  .el-form-item { margin-bottom: 20px; }
  .el-input__wrapper {
    border-radius: 8px;
    box-shadow: 0 0 0 1px $border-light inset;
    padding-left: 12px;
    transition: all 0.2s;
    &:hover { box-shadow: 0 0 0 1px $primary inset; }
  }
  .el-input__wrapper.is-focus {
    box-shadow: 0 0 0 1px $primary inset, 0 0 0 3px rgba($primary, 0.1);
  }
}

.vcode-row {
  display: flex;
  align-items: center;
  gap: 10px;
  .el-input { flex: 1; }
  .captcha-box {
    width: 110px;
    height: 40px;
    border-radius: 8px;
    overflow: hidden;
    cursor: pointer;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f7fa;
    border: 1px solid $border-light;
    transition: border-color 0.2s;
    &:hover { border-color: $primary; }
    .captcha-img { width: 100%; height: 100%; object-fit: cover; }
  }
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  border: none;
  color: #fff;
  transition: all 0.3s;
  &:hover {
    background: linear-gradient(135deg, #2a5298 0%, #1e3c72 100%);
    box-shadow: 0 6px 24px rgba(30, 60, 114, 0.35);
    transform: translateY(-1px);
  }
}

.login-footer {
  text-align: center;
  margin-top: 12px;
  font-size: 12px;
  color: $text-placeholder;
}

// 响应式
@media screen and (max-width: 920px) {
  .login-container { flex-direction: column; width: 92%; min-height: auto; }
  .brand-panel { padding: 36px 24px; min-height: 180px;
    .brand-features { display: none; }
    .brand-desc { margin-bottom: 0; }
    .brand-logo { width: 56px; height: 56px; margin-bottom: 12px; }
    .brand-name { font-size: 20px; }
  }
  .login-panel { width: 100%; padding: 32px 28px; }
}
</style>
