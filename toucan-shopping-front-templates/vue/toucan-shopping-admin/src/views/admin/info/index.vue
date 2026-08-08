<template>
  <div class="my-info">
    <h2 class="page-title">完善个人信息</h2>
    <el-card shadow="never" style="max-width:600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" v-loading="loading">
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="formData.nickName" placeholder="请输入昵称" maxlength="50" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" placeholder="请输入真实姓名" maxlength="50" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" maxlength="20" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" maxlength="100" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyInfo, saveMyInfo } from '@/api/system/adminInfo'

const formRef = ref(null)
const loading = ref(false)
const submitting = ref(false)

const formData = reactive({ nickName: '', realName: '', phone: '', email: '', gender: 1 })
const formRules = {
  realName: [{ required: true, message: '真实姓名不能为空', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getMyInfo()
    if (res.data) {
      formData.nickName = res.data.nickName || ''
      formData.realName = res.data.realName || ''
      formData.phone = res.data.phone || ''
      formData.email = res.data.email || ''
      formData.gender = res.data.gender != null ? res.data.gender : 1
    }
  } catch { /* ignore */ }
  finally { loading.value = false }
})

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    await saveMyInfo({ ...formData })
    ElMessage.success('保存成功')
  } finally { submitting.value = false }
}
</script>

<style lang="scss" scoped>
.my-info { padding: 0; }
</style>
