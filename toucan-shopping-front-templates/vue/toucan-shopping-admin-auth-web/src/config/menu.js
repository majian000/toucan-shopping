/**
 * 前端固化菜单配置（路由 + 侧边栏的唯一数据源）
 *
 * 菜单结构写死在前端，通过 /index/permissions 返回的权限标识控制显隐。
 * permission 字段与后端接口返回的权限字符串一一对应。
 *
 * 节点格式：
 *   容器 — { key, label, icon, permission?, children: [...] }
 *   叶子 — { path, name, label, icon, permission, component: () => import(...) }
 */

export const menuConfig = [
  // ==================== 工作台 ====================
  {
    key: 'dashboard',
    label: '工作台',
    icon: 'HomeFilled',
    permission: 'dashboard',
    children: [
      { path: '/dashboard', name: 'Dashboard', label: '工作台', icon: 'Monitor', permission: 'dashboard:view', component: () => import('@/views/dashboard/index.vue') },
      { path: '/adminMessage/myMessage', name: 'MyMessages', label: '我的消息', icon: 'Bell',  component: () => import('@/views/adminMessage/myMessage/index.vue') }
    ]
  },
  // ==================== 系统管理 ====================
  {
    key: 'system',
    label: '系统管理',
    icon: 'Menu',
    permission: 'system',
    children: [

      { path: '/system/admin/list', name: 'AdminList', label: '管理员列表', icon: 'Menu', permission: 'system:admin:list', component: () => import('@/views/system/admin/index.vue') },
      { path: '/system/role/list', name: 'RoleList', label: '角色管理', icon: 'Menu', permission: 'system:role:list', component: () => import('@/views/system/role/index.vue') },
      { path: '/system/post/list', name: 'PostList', label: '岗位管理', icon: 'Menu', permission: 'system:post:list', component: () => import('@/views/system/post/index.vue') },
      { path: '/system/menu/list', name: 'MenuList', label: '菜单管理', icon: 'Menu', permission: 'system:menu:list', component: () => import('@/views/system/menu/index.vue') },
      { path: '/system/orgnazition/list', name: 'OrgnazitionList', label: '组织机构', icon: 'Menu', permission: 'system:org:list', component: () => import('@/views/system/orgnazition/index.vue') },
      {
        key: 'dict',
        label: '字典管理',
        icon: 'Menu',
        permission: 'system:dict',
        children: [
          { path: '/system/dictCategory/list', name: 'DictCategoryList', label: '字典分类', icon: 'Menu', permission: 'system:dictCategory:list', component: () => import('@/views/system/dictCategory/index.vue') },
          { path: '/system/dict/list', name: 'DictList', label: '字典管理', icon: 'Menu', permission: 'system:dict:list', component: () => import('@/views/system/dict/index.vue') }
        ]
      },
      { path: '/system/onlineAdmin/list', name: 'OnlineAdminList', label: '在线管理员', icon: 'Menu', permission: 'system:admin:onlineList', component: () => import('@/views/system/onlineAdmin/index.vue') },
      { path: '/system/lockedAccount/list', name: 'LockedAccountList', label: '锁定账号', icon: 'Menu', permission: 'system:adminLock:list', component: () => import('@/views/system/lockedAccount/index.vue') },
      { path: '/system/operateLog/list', name: 'OperateLogList', label: '操作日志', icon: 'Menu', permission: 'system:operateLog:list', component: () => import('@/views/system/operateLog/index.vue') },
      // ==================== 管理员消息 ====================
      {
        key: 'adminMessage',
        label: '管理员消息',
        icon: 'Menu',
        permission: 'system:adminMessage',
        children: [
          { path: '/system/adminMessageType/list', name: 'AdminMessageTypeList', label: '消息分类', icon: 'Menu', permission: 'system:adminMessageCategory:list', component: () => import('@/views/system/adminMessageType/index.vue') },
          { path: '/system/adminMessage/list', name: 'AdminMessageList', label: '消息内容', icon: 'Menu', permission: 'system:adminMessage:list', component: () => import('@/views/system/adminMessage/index.vue') },
          { path: '/system/adminMessageRecord/list', name: 'AdminMessageRecordList', label: '消息记录', icon: 'Menu', permission: 'system:adminMessageRecord:list', component: () => import('@/views/system/adminMessageRecord/index.vue') }
        ]
      },
      // ==================== 系统配置 ====================
      {
        key: 'config',
        label: '系统配置',
        icon: 'Menu',
        permission: 'system:config',
        children: [
          { path: '/system/configCategory/list', name: 'ConfigCategoryList', label: '配置分类', icon: 'Menu', permission: 'system:configCategory:list', component: () => import('@/views/system/configCategory/index.vue') },
          { path: '/system/config/list', name: 'ConfigList', label: '配置管理', icon: 'Menu', permission: 'system:config:list', component: () => import('@/views/system/config/index.vue') }
        ]
      },
      // ==================== 模板管理 ====================
      {
        key: 'template',
        label: '模板管理',
        icon: 'Menu',
        permission: 'system:template',
        children: [
          { path: '/system/template/sms/list', name: 'SmsTemplateList', label: '短信模板', icon: 'Menu', permission: 'system:smsTemplate:list', component: () => import('@/views/system/template/sms/index.vue') },
          { path: '/system/template/email/list', name: 'EmailTemplateList', label: '邮件模板', icon: 'Menu', permission: 'system:emailTemplate:list', component: () => import('@/views/system/template/email/index.vue') }
        ]
      },
      // ==================== 地区管理 ====================
      { path: '/system/area/list', name: 'AreaList', label: '地区管理', icon: 'Menu', permission: 'system:area:list', component: () => import('@/views/system/area/index.vue') }
    ]
  }
]
