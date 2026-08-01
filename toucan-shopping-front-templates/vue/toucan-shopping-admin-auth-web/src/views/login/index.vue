<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <el-icon :size="36" color="#409eff"><Stamp /></el-icon>
        <h2>Admin Framework</h2>
        <p>后台管理系统</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%" :loading="loading" @click="handleLogin">
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span>测试账号：admin / admin123</span>
      </div>
    </div>

    <div class="login-bg-text">
      <span>Admin Framework</span>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '@/api/login'
import { setToken } from '@/utils/auth'

const router = useRouter()
const route = useRoute()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: 'admin',
  password: 'admin123'
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login(form.username, form.password)
    setToken(res.data.adminId+":"+res.data.loginToken)
    ElMessage({ message: '登录成功', type: 'success', duration: 1500 })
    // 跳转到 redirect 参数指定的页面，或默认首页
    const redirect = route.query.redirect || '/dashboard'
    router.replace(redirect)
  } catch {
    // 错误信息已在 request.js 拦截器中统一处理
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
}

.login-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;

  h2 {
    margin: 12px 0 4px;
    font-size: 22px;
    color: $text-primary;
    font-weight: 700;
  }

  p {
    margin: 0;
    font-size: 13px;
    color: $text-secondary;
  }
}

.login-footer {
  text-align: center;
  margin-top: 8px;
  font-size: 12px;
  color: $text-placeholder;
}

.login-bg-text {
  position: absolute;
  bottom: 40px;
  right: 60px;
  font-size: 48px;
  font-weight: 900;
  color: rgba(255, 255, 255, 0.08);
  letter-spacing: 8px;
  user-select: none;
  pointer-events: none;
}
</style>
