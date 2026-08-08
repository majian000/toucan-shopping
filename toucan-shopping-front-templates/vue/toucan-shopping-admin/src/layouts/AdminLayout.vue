<template>
  <div class="admin-layout" :class="'layout-' + appStore.layoutMode">
    <!-- ========== 顶部导航条 ========== -->
    <header class="al-topbar">
      <div class="topbar-left" :class="{ 'topbar-left--side': appStore.layoutMode === 'side' }">
        <div class="logo" @click="goHome">
          <el-icon :size="22" color="#409eff"><Stamp /></el-icon>
          <span class="logo-text" v-if="appStore.layoutMode === 'top'">权限中台</span>
          <span class="logo-text logo-text--side" v-if="appStore.layoutMode === 'side'">权限中台</span>
        </div>
        <TopMenu
          v-if="appStore.layoutMode === 'top'"
          :menu-items="appStore.menuData"
          :active-key="appStore.activeTopMenu"
          @select="onTopMenuSelect"
        />
      </div>
      <div class="topbar-right">
        <!-- 布局切换 -->
        <el-tooltip :content="appStore.layoutMode === 'top' ? '切换为左侧菜单' : '切换为顶部菜单'" placement="bottom">
          <span class="layout-toggle" @click="toggleLayout">
            <el-icon :size="18"><Fold v-if="appStore.layoutMode === 'top'" /><Expand v-else /></el-icon>
          </span>
        </el-tooltip>

        <!-- 用户下拉 -->
        <el-dropdown trigger="click" @command="handleUserCommand">
          <span class="user-info">
            <el-icon :size="20"><UserFilled /></el-icon>
            <span>{{ userName }}</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <!-- 修改密码弹窗 -->
      <el-dialog v-model="passwordVisible" title="修改密码" width="440px" destroy-on-close>
        <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px">
          <el-form-item label="新密码" prop="newPwd">
            <el-input v-model="pwdForm.newPwd" type="password" placeholder="请输入新密码" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPwd">
            <el-input v-model="pwdForm.confirmPwd" type="password" placeholder="请再次输入新密码" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="passwordVisible = false">取消</el-button>
          <el-button type="primary" :loading="pwdLoading" @click="handleChangePassword">确认修改</el-button>
        </template>
      </el-dialog>
    </header>

    <!-- ========== 顶部模式选项卡栏 ========== -->
    <TabBar v-if="appStore.layoutMode === 'top'" />

    <div class="al-body">
      <!-- ========== 左侧菜单（side 模式） ========== -->
      <SideMenu v-if="appStore.layoutMode === 'side'" />

      <div class="al-content">
        <TabBar v-if="appStore.layoutMode === 'side'" />

        <!-- ========== 内容区 ========== -->
        <main class="al-main">
      <div class="content-view">
        <router-view v-slot="{ Component }">
          <keep-alive :max="20">
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </div>
    </main>
      </div>
    </div>

    <!-- ========== 底部 ========== -->
    <footer class="al-footer">
      <span>Copyright &copy; {{ new Date().getFullYear() }} 权限中台. All Rights Reserved.</span>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import { useAppStore } from '@/store/modules/app'
import { useTabsStore } from '@/store/modules/tabs'
import { usePermissionStore } from '@/store/modules/permission'
import TopMenu from '@/components/TopMenu/index.vue'
import SideMenu from '@/components/SideMenu/index.vue'
import TabBar from '@/components/TabBar/index.vue'
import { Stamp, ArrowDown, UserFilled, Fold, Expand } from '@element-plus/icons-vue'
import { removeToken } from '@/utils/auth'
import { getPermissions } from '@/api/permission'
import { filterMenuByPerms } from '@/utils/menuFilter'
import { menuConfig } from '@/config/menu'
import { getInfo, logout as logoutApi } from '@/api/login'
import { updateMyPassword } from '@/api/system/admin'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const tabsStore = useTabsStore()
const permissionStore = usePermissionStore()
const userName = ref('管理员')

function onTopMenuSelect(item) {
  appStore.setActiveTopMenu(item.key)
}

function goHome() {
  appStore.setActiveTopMenu('dashboard')
  tabsStore.openTab('/dashboard', '工作台', 'Dashboard')
}

function toggleLayout() {
  const next = appStore.layoutMode === 'top' ? 'side' : 'top'
  appStore.setLayoutMode(next)
}

// ========== 修改密码 ==========
const passwordVisible = ref(false)
const pwdLoading = ref(false)
const pwdFormRef = ref(null)
const pwdForm = reactive({ newPwd: '', confirmPwd: '' })

const validateConfirmPwd = (rule, value, callback) => {
  if (value !== pwdForm.newPwd) callback(new Error('两次输入的密码不一致'))
  else callback()
}

const pwdRules = {
  newPwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度6-20位', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPwd, trigger: 'blur' }
  ]
}

async function handleChangePassword() {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return
  pwdLoading.value = true
  try {
    await updateMyPassword({ password: pwdForm.newPwd })
    ElMessage.success('密码修改成功，请重新登录')
    passwordVisible.value = false
    removeToken()
    router.replace('/login')
  } catch {
    // 错误由拦截器统一处理
  } finally {
    pwdLoading.value = false
  }
}

// ========== 用户菜单命令 ==========
function handleUserCommand(command) {
  if (command === 'changePassword') {
    passwordVisible.value = true
  } else if (command === 'logout') {
    ElMessageBox.confirm('确认退出登录吗？', '提示', {
      confirmButtonText: '确认退出',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      try { await logoutApi() } catch { /* ignore */ }
      removeToken()
      ElMessage.success('已退出登录')
      router.replace('/login')
    }).catch(() => {})
  }
}

// 激活 tab 变化时同步顶部菜单高亮
watch(() => tabsStore.activeTabPath, (newPath) => {
  for (const menu of appStore.menuData) {
    if (pathBelongsToMenu(newPath, menu)) {
      appStore.setActiveTopMenu(menu.key)
      break
    }
  }
})

function pathBelongsToMenu(path, menuItem) {
  const children = menuItem.children
  if (!children) return false
  return children.some(item => {
    if (item.path === path) return true
    if (item.children) {
      return item.children.some(child => {
        if (child.path === path) return true
        if (child.children) {
          return child.children.some(gc => {
            if (gc.path === path) return true
            if (gc.children) {
              return gc.children.some(ggc => ggc.path === path)
            }
            return false
          })
        }
        return false
      })
    }
    return false
  })
}

// 根据用户权限过滤本地菜单
let userPermissionSet = new Set()

function applyMenuByPermissions() {
  appStore.setMenuData(filterMenuByPerms(menuConfig, userPermissionSet))
}

// 加载权限并刷新菜单
async function loadPermissions() {
  try {
    const res = await getPermissions()
    userPermissionSet = new Set(res.data || [])
    permissionStore.setPermissions([...userPermissionSet])
    applyMenuByPermissions()
  } catch {
    // 权限加载失败，清空菜单
    appStore.setMenuData([])
    permissionStore.clearPermissions()
  }
}

onMounted(async () => {
  tabsStore.initFromRoute(route)
  const loadingInstance = ElLoading.service({ fullscreen: true, text: '加载中...' })
  try {
    // 1. 检查登录状态
    const infoRes = await getInfo()
    userName.value = infoRes.data.nickName || '管理员'
    if (infoRes.data.loginStatus === 0) {
      removeToken()
      router.replace('/login')
      return
    }
    // 2. 获取权限 & 过滤菜单
    await loadPermissions()
  } catch { /* 忽略 */ } finally {
    loadingInstance.close()
  }
})

watch(() => route.path, (newPath) => {
  if (newPath === '/' || newPath === tabsStore.activeTabPath) return
  const title = route.meta?.title || ''
  const name = route.name || ''
  tabsStore.openTab(newPath, title, name)
  // 每次路由切换都重新加载权限，确保后台修改即时生效
  loadPermissions()
})
</script>

<style lang="scss" scoped>
.admin-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

// ========== 顶部导航条 ==========
.al-topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
  z-index: 300;
  flex-shrink: 0;
  height: $top-nav-height;

  .topbar-left {
    display: flex;
    align-items: center;
    flex: 1;
    min-width: 0;
    height: 100%;

    &--side {
      flex: 0 0 auto;
      .logo { border-right: none; padding: 0 16px; }
    }

    .logo {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 0 20px;
      height: 100%;
      cursor: pointer;
      flex-shrink: 0;
      border-right: 1px solid $border-light;
      .logo-text {
        font-size: 15px;
        font-weight: 600;
        color: $text-primary;
        white-space: nowrap;
      }
    }
  }

  .topbar-right {
    display: flex;
    align-items: center;
    gap: 20px;
    padding: 0 20px;
    flex-shrink: 0;

    .layout-toggle { cursor: pointer; color: $text-secondary; &:hover { color: $primary; } }
    .notice-btn { cursor: pointer; color: $text-secondary; &:hover { color: $primary; } }

    .user-info {
      display: flex;
      align-items: center;
      gap: 6px;
      cursor: pointer;
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 13px;
      color: $text-primary;
      &:hover { background: #f5f7fa; }
    }
  }
}

// ========== 底部 ==========
.al-footer {
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-top: 1px solid $border-light;
  flex-shrink: 0;
  font-size: 12px;
  color: $text-secondary;
}

// ========== 主体区域（side 模式下包含左侧菜单） ==========
.al-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

// ========== side 模式内容列 ==========
.al-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.side-breadcrumb {
  flex-shrink: 0;
  padding: 10px 20px;
  background: #fff;
  border-bottom: 1px solid $border-light;
}


// ========== 内容区 ==========
.al-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .content-view {
    flex: 1;
    overflow-y: auto;
    background: $bg-page;
    padding: 20px;
  }
}
</style>

<style lang="scss">
.notice-popover { padding: 0 !important; }
.notice-list {
  .notice-header { display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; border-bottom: 1px solid #ebeef5; font-size: 14px; font-weight: 600; .notice-header-actions { display: flex; gap: 8px; } }
  .notice-empty { padding: 32px 0; text-align: center; color: #c0c4cc; font-size: 13px; }
  .notice-item { display: flex; align-items: flex-start; gap: 10px; padding: 12px 16px; cursor: pointer; transition: background 0.2s; border-bottom: 1px solid #f2f3f5; &:last-child { border-bottom: none; } &:hover { background: #f5f7fa; } &.unread { background: #ecf5ff; }
    .notice-dot { width: 8px; height: 8px; border-radius: 50%; background: #f56c6c; margin-top: 5px; flex-shrink: 0; }
    .notice-content { flex: 1; min-width: 0; .notice-title.ellipsis { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 260px; } .notice-title { font-size: 13px; color: #303133; line-height: 1.5; } .notice-time { font-size: 12px; color: #c0c4cc; margin-top: 4px; } }
  }
  .notice-pagination { display: flex; justify-content: center; padding: 8px 0; border-top: 1px solid #ebeef5; }
}
.msg-detail { .detail-meta { display: flex; align-items: center; gap: 16px; font-size: 13px; color: #909399; span { display: flex; align-items: center; gap: 4px; } } .detail-content { font-size: 14px; color: #303133; line-height: 2; min-height: 80px; word-break: break-word; } }
</style>
