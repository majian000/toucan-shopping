import { apiUrl } from './_baseUrl'

// 模拟当前登录用户角色（可修改为 'admin' | 'user' | 'auditor' 测试不同权限）
const CURRENT_ROLE = 'admin'

// 所有权限标识（与后端 /index/permissions 格式一致）
const allPermissions = [
  'dashboard', 'dashboard:view',
  'system', 'sysatem:user', 'system:admin:list',
  'system:admin:add', 'system:admin:edit', 'system:admin:delete',
  'system:admin:batchDelete', 'system:admin:resetPwd', 'system:admin:export',
  'system:admin:status', 'system:admin:search',
  'sysatem', 'system:role:list',
  'system:role:add', 'system:role:edit', 'system:role:delete',
  'system:role:assignPerm', 'system:role:search',
  'system:menu:list', 'system:menu:add', 'system:menu:edit', 'system:menu:delete',
  'user:profile', 'user:profile:password',
  'user:password:change'
]

// 不同角色对应的权限标识
const rolePermissions = {
  admin: allPermissions,
  user: [
    'dashboard', 'dashboard:view',
    'system', 'sysatem:user', 'system:admin:list',
    'system:admin:add', 'system:admin:edit', 'system:admin:delete',
    'system:admin:batchDelete', 'system:admin:resetPwd', 'system:admin:export',
    'system:admin:status', 'system:admin:search',
    'user:profile', 'user:profile:password', 'user:password:change'
  ],
  auditor: [
    'dashboard', 'dashboard:view',
    'system', 'sysatem', 'system:role:list',
    'system:role:add', 'system:role:edit', 'system:role:delete',
    'system:role:assignPerm', 'system:role:search',
    'system:menu:list', 'system:menu:add', 'system:menu:edit', 'system:menu:delete',
    'user:profile', 'user:profile:password', 'user:password:change'
  ]
}

export default [
  // 登录
  {
    url: apiUrl('/login/submit'),
    method: 'post',
    response: () => ({
      code: 1,
      msg: '操作成功',
      success: true,
      data: { loginToken: 'mock-login-token-' + Date.now() }
    })
  },
  // 退出
  {
    url: apiUrl('/logout/out'),
    method: 'post',
    response: () => ({ code: 1, msg: '退出成功' })
  },
  // 获取用户登录状态（loginStatus: 1=已登录 0=未登录）
  {
    url: apiUrl('/index/getInfo'),
    method: 'get',
    response: () => ({
      code: 1,
      msg: '操作成功',
      data: { loginStatus: 1 }
    })
  },
  // 获取当前用户权限标识列表
  {
    url: apiUrl('/index/permissions'),
    method: 'get',
    response: () => ({
      code: 1,
      msg: '操作成功',
      data: rolePermissions[CURRENT_ROLE] || []
    })
  },
  // 验证码
  {
    url: apiUrl('/captchaImage'),
    method: 'get',
    response: () => ({
      code: 1,
      msg: '操作成功',
      data: { captchaEnabled: false, uuid: '', img: '' }
    })
  },
  // 获取路由
  {
    url: apiUrl('/getRouters'),
    method: 'get',
    response: () => ({ code: 1, msg: '操作成功', data: [] })
  }
]
