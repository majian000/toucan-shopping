<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>

    <div class="login-card">
      <div class="card-header">
        <div class="logo-icon">
          <el-icon :size="28"><Stamp /></el-icon>
        </div>
        <h2>权限中台</h2>
        <p>Permission Management System</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入登录账号"
            :prefix-icon="User"
            clearable
            maxlength="24"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
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
              style="flex:1"
            />
            <div class="captcha-box" @click="refreshCaptcha" title="点击刷新">
              <img v-if="captchaUrl" :src="captchaUrl" class="captcha-img" />
              <el-icon v-else :size="20" class="loading-icon"><Loading /></el-icon>
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            {{ loading ? '登录中...' : '立即登录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="card-footer">
        <span>© 2020-2025 犀鸟商城</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Key, Stamp, Loading } from '@element-plus/icons-vue'
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
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 40%, #0f3460 100%);
  overflow: hidden;
}

// 背景装饰形状
.bg-shapes {
  position: absolute;
  inset: 0;
  pointer-events: none;
  .shape {
    position: absolute;
    border-radius: 50%;
    opacity: 0.06;
    background: #fff;
  }
  .shape-1 { width: 500px; height: 500px; top: -200px; right: -100px; }
  .shape-2 { width: 300px; height: 300px; bottom: -80px; left: -80px; }
  .shape-3 { width: 200px; height: 200px; top: 40%; left: 60%; }
}

.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.97);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.3);
  z-index: 1;
  overflow: hidden;
}

.card-header {
  text-align: center;
  padding: 40px 40px 0;
  .logo-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 60px;
    height: 60px;
    border-radius: 16px;
    background: linear-gradient(135deg, $primary 0%, #66b1ff 100%);
    color: #fff;
    margin-bottom: 20px;
    box-shadow: 0 8px 24px rgba($primary, 0.3);
  }
  h2 {
    margin: 0;
    font-size: 22px;
    font-weight: 700;
    color: $text-primary;
    letter-spacing: 2px;
  }
  p {
    margin: 6px 0 0;
    font-size: 12px;
    color: $text-secondary;
    letter-spacing: 1px;
    text-transform: uppercase;
  }
}

:deep(.el-form) {
  padding: 32px 44px 16px;
  .el-form-item { margin-bottom: 24px; }
  .el-input__wrapper {
    box-shadow: 0 0 0 1px #e4e7ed inset;
    border-radius: 8px;
    padding: 0 12px;
    transition: all 0.2s;
    &:hover { box-shadow: 0 0 0 1px $primary inset; }
  }
  .el-input__wrapper.is-focus { box-shadow: 0 0 0 2px rgba($primary, 0.2) inset; }
}

.vcode-row {
  display: flex;
  align-items: center;
  gap: 12px;
  .captcha-box {
    width: 120px;
    height: 40px;
    border-radius: 8px;
    overflow: hidden;
    cursor: pointer;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f0f2f5;
    border: 1px solid #e4e7ed;
    transition: border-color 0.2s;
    &:hover { border-color: $primary; }
    .captcha-img { width: 100%; height: 100%; object-fit: cover; }
    .loading-icon { color: $text-secondary; animation: spin 1s linear infinite; }
  }
}

@keyframes spin { to { transform: rotate(360deg); } }

.login-btn {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  font-size: 15px;
  letter-spacing: 2px;
  background: linear-gradient(135deg, $primary 0%, #66b1ff 100%);
  border: none;
  box-shadow: 0 4px 16px rgba($primary, 0.3);
  transition: all 0.3s;
  &:hover { box-shadow: 0 6px 24px rgba($primary, 0.4); transform: translateY(-1px); }
}

.card-footer {
  text-align: center;
  padding: 12px 0 24px;
  font-size: 12px;
  color: $text-placeholder;
}
</style>
