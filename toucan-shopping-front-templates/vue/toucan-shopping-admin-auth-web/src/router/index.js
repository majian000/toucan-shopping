import { createRouter, createWebHashHistory } from 'vue-router'
import AdminLayout from '@/layouts/AdminLayout.vue'
import { isLoggedIn } from '@/utils/auth'
import { usePermissionStore } from '@/store/modules/permission'
import { menuConfig } from '@/config/menu'

/**
 * 从 menuConfig 递归提取叶子路由
 */
function extractLeafRoutes(menuItems) {
  const routes = []
  for (const item of menuItems) {
    if (item.children) {
      routes.push(...extractLeafRoutes(item.children))
    } else if (item.path && item.component) {
      routes.push({
        path: item.path,
        name: item.name,
        component: item.component,
        meta: { title: item.label, permission: item.permission }
      })
    }
  }
  return routes
}

const routes = [
  // ========== 登录页（独立布局） ==========
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  // ========== 主布局（需要登录） ==========
  {
    path: '/',
    component: AdminLayout,
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      // 由 menuConfig 动态生成
      ...extractLeafRoutes(menuConfig),
      // 不在侧边栏中的独立页面
      { path: 'admin/password', name: 'AdminPassword', component: () => import('@/views/admin/password/index.vue'), meta: { title: '修改密码' } },
      { path: 'admin/info', name: 'AdminInfo', component: () => import('@/views/admin/info/index.vue'), meta: { title: '完善个人信息' } }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 路由守卫：登录校验 + 权限校验
const whiteList = ['/login']
router.beforeEach((to, from, next) => {
  if (isLoggedIn()) {
    if (to.path === '/login') {
      next('/dashboard')
    } else {
      const requiredPerm = to.meta.permission
      if (requiredPerm) {
        const permStore = usePermissionStore()
        if (!permStore.hasPermission(requiredPerm)) {
          // 已经在工作台就不再跳转，避免死循环
          if (to.path === '/dashboard') {
            next()
            return
          }
          next('/dashboard')
          return
        }
      }
      next()
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router
