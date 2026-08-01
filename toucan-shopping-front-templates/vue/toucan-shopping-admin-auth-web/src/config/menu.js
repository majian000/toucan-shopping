/**
 * 权限中台 前端菜单配置
 *
 * 菜单层级与数据库 t_sa_function 表对应
 * permission 字段使用 pms 前缀格式
 */

export const menuConfig = [
  // ==================== 工作台 ====================
  {
    key: 'dashboard',
    label: '工作台',
    icon: 'HomeFilled',
    permission: 'pms:home:welcome',
    children: [
      { path: '/dashboard', name: 'Dashboard', label: '工作台', icon: 'Monitor', component: () => import('@/views/dashboard/index.vue') }
    ]
  },
  // ==================== 系统管理 ====================
  {
    key: 'system',
    label: '系统管理',
    icon: 'Menu',
    permission: 'pms:system',
    children: [
      { path: '/system/app/list', name: 'AppList', label: '应用管理', icon: 'Menu', permission: 'pms:system:app', component: () => import('@/views/system/app/index.vue') },
      { path: '/system/admin/list', name: 'AdminList', label: '账号管理', icon: 'Menu', permission: 'pms:system:user', component: () => import('@/views/system/admin/index.vue') },
      { path: '/system/role/list', name: 'RoleList', label: '角色管理', icon: 'Menu', permission: 'pms:system:role', component: () => import('@/views/system/role/index.vue') },
      { path: '/system/menu/list', name: 'MenuList', label: '菜单管理', icon: 'Menu', permission: 'pms:system:menu', component: () => import('@/views/system/menu/index.vue') },
      { path: '/system/orgnazition/list', name: 'OrgnazitionList', label: '组织机构', icon: 'Menu', permission: 'pms:system:org', component: () => import('@/views/system/orgnazition/index.vue') },
      {
        key: 'dict',
        label: '字典管理',
        icon: 'Menu',
        permission: 'pms:dict',
        children: [
          { path: '/system/dictCategory/list', name: 'DictCategoryList', label: '字典分类', icon: 'Menu', permission: 'pms:dict:category', component: () => import('@/views/system/dictCategory/index.vue') },
          { path: '/system/dict/list', name: 'DictList', label: '字典管理', icon: 'Menu', permission: 'pms:dict:item', component: () => import('@/views/system/dict/index.vue') }
        ]
      },
      { path: '/system/onlineAdmin/list', name: 'OnlineAdminList', label: '在线管理', icon: 'Menu', permission: 'pms:system:online', component: () => import('@/views/system/onlineAdmin/index.vue') },
      {
        key: 'log',
        label: '日志管理',
        icon: 'Menu',
        permission: 'pms:log',
        children: [
          { path: '/system/operateLog/list', name: 'OperateLogList', label: '操作日志', icon: 'Menu', permission: 'pms:log:operate', component: () => import('@/views/system/operateLog/index.vue') },
          { path: '/system/loginHistory/list', name: 'LoginHistoryList', label: '登录日志', icon: 'Menu', permission: 'pms:log:login', component: () => import('@/views/system/loginHistory/index.vue') }
        ]
      }
    ]
  }
]
