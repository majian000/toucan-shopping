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
    permission: 'toucan:dashboard:welcome',
    children: [
      { path: '/dashboard', name: 'Dashboard', label: '工作台', icon: 'Monitor', component: () => import('@/views/dashboard/index.vue') }
    ]
  },
  // ==================== 系统管理 ====================
  {
    key: 'permission',
    label: '系统管理',
    icon: 'Lock',
    permission: 'toucan:admin:permission',
    children: [
      // ----- 系统管理 -----
      {
        key: 'system',
        label: '系统管理',
        icon: 'Setting',
        permission: 'toucan:admin:system',
        children: [
          { path: '/system/admin/list', name: 'AdminList', label: '账号管理', icon: 'User', permission: 'toucan:admin:admin', component: () => import('@/views/system/admin/index.vue') },
          { path: '/system/role/list', name: 'RoleList', label: '角色管理', icon: 'UserFilled', permission: 'toucan:admin:role', component: () => import('@/views/system/role/index.vue') },
          { path: '/system/menu/list', name: 'MenuList', label: '菜单管理', icon: 'Menu', permission: 'toucan:admin:function', component: () => import('@/views/system/menu/index.vue') },
          { path: '/system/orgnazition/list', name: 'OrgnazitionList', label: '组织机构', icon: 'Share', permission: 'toucan:admin:orgnazition:list', component: () => import('@/views/system/orgnazition/index.vue') }
        ]
      },
      // ----- 字典管理 -----
      {
        key: 'dict',
        label: '字典管理',
        icon: 'Collection',
        permission: 'toucan:admin:dictManage',
        children: [
          { path: '/system/dictCategory/list', name: 'DictCategoryList', label: '字典分类', icon: 'Menu', permission: 'toucan:admin:dictCategory', component: () => import('@/views/system/dictCategory/index.vue') },
          { path: '/system/dict/list', name: 'DictList', label: '字典管理', icon: 'Menu', permission: 'toucan:admin:dict', component: () => import('@/views/system/dict/index.vue') }
        ]
      },
      // ----- 日志管理 -----
      {
        key: 'log',
        label: '日志管理',
        icon: 'Document',
        permission: 'pms:log',
        children: [
          { path: '/system/operateLog/list', name: 'OperateLogList', label: '操作日志', icon: 'Menu', permission: 'pms:log:operate', component: () => import('@/views/system/operateLog/index.vue') },
          { path: '/system/loginHistory/list', name: 'LoginHistoryList', label: '登录日志', icon: 'Menu', permission: 'pms:log:login', component: () => import('@/views/system/loginHistory/index.vue') }
        ]
      },
      // ----- 消息管理 -----
      {
        key: 'message',
        label: '消息管理',
        icon: 'ChatDotRound',
        permission: 'toucan:content:message',
        children: [
          { path: '/message/messageType/list', name: 'MessageTypeList', label: '消息类型', icon: 'Menu', permission: 'toucan:content:messageType:list', component: () => import('@/views/message/messageType/index.vue') },
          { path: '/message/messageUser/list', name: 'MessageUserList', label: '用户消息', icon: 'Menu', permission: 'toucan:content:messageUser:list', component: () => import('@/views/message/messageUser/index.vue') }
        ]
      },
      // ----- 接口监控 -----
      {
        key: 'monitor',
        label: '接口监控',
        icon: 'Monitor',
        permission: 'toucan:monitor:apiMonitor',
        children: [
          { path: '/monitor/dashboard', name: 'ApiMonitorDashboard', label: '接口监控', icon: 'DataAnalysis', permission: 'toucan:monitor:dashboard', component: () => import('@/views/monitor/dashboard/index.vue') },
          { path: '/monitor/requestLog', name: 'ApiMonitorRequestLog', label: '请求日志', icon: 'Document', permission: 'toucan:monitor:requestLog', component: () => import('@/views/monitor/requestLog/index.vue') }
        ]
      }
    ]
  },
  // ==================== 首页管理 ====================
  {
    key: 'content',
    label: '首页管理',
    icon: 'HomeFilled',
    permission: 'toucan:index',
    children: [
      // ----- 首页管理 -----
      {
        key: 'content-index',
        label: '首页管理',
        icon: 'House',
        permission: 'toucan:index:page',
        children: [
          { path: '/content/hotProduct', name: 'ContentHotProduct', label: '热门商品', icon: 'Star', permission: 'toucan:dashboard:hotProduct', component: () => import('@/views/content/hotProduct/index.vue') },
          { path: '/content/area', name: 'ContentArea', label: '地区列表', icon: 'Location', permission: 'toucan:area:area', component: () => import('@/views/content/area/index.vue') },
          { path: '/content/banner', name: 'ContentBanner', label: '轮播图列表', icon: 'Picture', permission: 'toucan:content:banner:list', component: () => import('@/views/content/banner/index.vue') },
          { path: '/content/category', name: 'ContentCategory', label: '类别列表', icon: 'Grid', permission: 'toucan:category:categoryList', component: () => import('@/views/content/category/index.vue') }
        ]
      },
      // ----- 栏目管理 -----
      { path: '/content/column', name: 'ContentColumn', label: '栏目管理', icon: 'Menu', permission: 'toucan:content:columnType:list', component: () => import('@/views/content/column/index.vue') },
      // ----- 文章管理 -----
      { path: '/content/article', name: 'ContentArticle', label: '文章管理', icon: 'Notebook', permission: 'toucan:content:article', component: () => import('@/views/content/article/index.vue') },
      // ----- 静态化管理 -----
      { path: '/content/htmlPage', name: 'ContentHtmlPage', label: '静态化管理', icon: 'Files', permission: 'toucan:static:manager', component: () => import('@/views/content/htmlPage/index.vue') }
    ]
  }
]
