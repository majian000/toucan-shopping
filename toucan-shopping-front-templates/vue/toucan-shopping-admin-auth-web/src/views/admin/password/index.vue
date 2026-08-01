<template>
  <div class="page user-password">
    <h2 class="page-title">修改密码</h2>

    <el-card shadow="never">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="password-form"
      >
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input
            v-model="form.oldPassword"
            type="password"
            placeholder="请输入当前密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
          <div class="password-strength" v-if="form.newPassword">
            <span class="strength-label">密码强度：</span>
            <el-progress
              :percentage="strengthPercent"
              :color="strengthColor"
              :stroke-width="8"
              style="width: 200px"
            />
            <span class="strength-text" :style="{ color: strengthColor }">{{ strengthText }}</span>
          </div>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>

        <el-divider />

        <el-alert
          title="密码安全提示"
          type="info"
          :closable="false"
          show-icon
          class="password-tips"
        >
          <ul class="tips-list">
            <li :class="{ passed: hasMinLength }">至少8位字符</li>
            <li :class="{ passed: hasUpper }">包含大写字母</li>
            <li :class="{ passed: hasLower }">包含小写字母</li>
            <li :class="{ passed: hasDigit }">包含数字</li>
            <li :class="{ passed: hasSpecial }">包含特殊字符（推荐）</li>
          </ul>
        </el-alert>

        <el-form-item style="margin-top: 20px">
          <el-button type="primary" :loading="saveLoading" :icon="Check" v-permission="'pms:system:user:change-password'" @click="handleSave">
            确认修改
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Check } from '@element-plus/icons-vue'

const formRef = ref(null)
const saveLoading = ref(false)

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度在8到20位之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 密码强度检测
const hasMinLength = computed(() => form.newPassword.length >= 8)
const hasUpper = computed(() => /[A-Z]/.test(form.newPassword))
const hasLower = computed(() => /[a-z]/.test(form.newPassword))
const hasDigit = computed(() => /\d/.test(form.newPassword))
const hasSpecial = computed(() => /[!@#$%^&*(),.?":{}|<>]/.test(form.newPassword))

const strengthScore = computed(() => {
  let score = 0
  if (hasMinLength.value) score++
  if (hasUpper.value) score++
  if (hasLower.value) score++
  if (hasDigit.value) score++
  if (hasSpecial.value) score++
  return score
})

const strengthPercent = computed(() => (strengthScore.value / 5) * 100)
const strengthColor = computed(() => {
  if (strengthScore.value <= 2) return '#f56c6c'
  if (strengthScore.value <= 3) return '#e6a23c'
  return '#67c23a'
})
const strengthText = computed(() => {
  if (strengthScore.value <= 2) return '弱'
  if (strengthScore.value <= 3) return '中'
  if (strengthScore.value <= 4) return '强'
  return '非常强'
})

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saveLoading.value = true
  setTimeout(() => {
    saveLoading.value = false
    ElMessage.success('密码修改成功，请重新登录')
    form.oldPassword = ''
    form.newPassword = ''
    form.confirmPassword = ''
  }, 1000)
}

function handleReset() {
  formRef.value?.resetFields()
}
</script>

<style lang="scss" scoped>
.user-password {
  .password-form {
    max-width: 600px;
  }

  .password-strength {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-top: 8px;

    .strength-label {
      font-size: 12px;
      color: $text-secondary;
    }

    .strength-text {
      font-size: 12px;
      font-weight: 600;
    }
  }

  .password-tips {
    max-width: 600px;
  }

  .tips-list {
    margin: 8px 0 0 16px;
    padding: 0;
    list-style: disc;
    font-size: 13px;
    color: $text-secondary;

    li {
      margin-bottom: 4px;
      &.passed {
        color: #67c23a;
        text-decoration: line-through;
      }
    }
  }
}
</style>
