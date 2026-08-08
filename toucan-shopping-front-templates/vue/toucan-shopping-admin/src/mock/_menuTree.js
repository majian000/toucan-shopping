// 共享菜单树数据，role 和 menu 共用
// type: directory（顶级目录）| menu（菜单/分组）| button（按钮权限）
// path 有值 → 可导航的菜单项；path 为空 → 仅用于分组的容器节点
// status: 1启用 0禁用，禁用后菜单/按钮都不显示
export const menuTreeData = [
  // ==================== 工作台 ====================
  { id: 1, name: '工作台', type: 'directory', icon: 'HomeFilled', path: '/dashboard', sort: 1, status: 1, children: [
    { id: 11, name: '工作台', type: 'menu', icon: 'Monitor', path: '/dashboard', component: 'dashboard/index', permission: 'dashboard:view', sort: 1, status: 1 }
  ]},

  // ==================== 系统管理 ====================
  { id: 2, name: '系统管理', type: 'directory', icon: 'Setting', path: '/system', sort: 2, status: 1, children: [

    // ---- 用户管理（分组） ----
    { id: 210, name: '用户管理', type: 'menu', icon: 'User', path: '', sort: 1, status: 1, children: [
      { id: 211, name: '用户列表', type: 'menu', icon: 'List', path: '/system/user/list', component: 'system/user/index', permission: 'system:admin:list', sort: 1, status: 1, children: [
        { id: 2111, name: '新增用户', type: 'button', permission: 'system:admin:add', sort: 1, status: 1 },
        { id: 2112, name: '编辑用户', type: 'button', permission: 'system:admin:edit', sort: 2, status: 1 },
        { id: 2113, name: '删除用户', type: 'button', permission: 'system:admin:delete', sort: 3, status: 1 },
        { id: 2114, name: '批量删除', type: 'button', permission: 'system:admin:batchDelete', sort: 4, status: 1 },
        { id: 2115, name: '重置密码', type: 'button', permission: 'system:admin:resetPwd', sort: 5, status: 1 },
        { id: 2116, name: '导出用户', type: 'button', permission: 'system:admin:export', sort: 6, status: 1 },
        { id: 2117, name: '切换状态', type: 'button', permission: 'system:admin:status', sort: 7, status: 1 },
        { id: 2118, name: '搜索', type: 'button', permission: 'system:admin:search', sort: 8, status: 1 }
      ]}
    ]},

    // ---- 权限管理（分组） ----
    { id: 220, name: '权限管理', type: 'menu', icon: 'Lock', path: '', sort: 2, status: 1, children: [
      { id: 221, name: '角色管理', type: 'menu', icon: 'Avatar', path: '/system/role/list', component: 'system/role/index', permission: 'system:role:list', sort: 1, status: 1, children: [
        { id: 2211, name: '新增角色', type: 'button', permission: 'system:role:add', sort: 1, status: 1 },
        { id: 2212, name: '编辑角色', type: 'button', permission: 'system:role:edit', sort: 2, status: 1 },
        { id: 2213, name: '删除角色', type: 'button', permission: 'system:role:delete', sort: 3, status: 1 },
        { id: 2214, name: '权限分配', type: 'button', permission: 'system:role:assignPerm', sort: 4, status: 1 },
        { id: 2215, name: '搜索', type: 'button', permission: 'system:role:search', sort: 5, status: 1 }
      ]},
      { id: 222, name: '菜单管理', type: 'menu', icon: 'Menu', path: '/system/menu/list', component: 'system/menu/index', permission: 'system:menu:list', sort: 2, status: 1, children: [
        { id: 2221, name: '新增菜单', type: 'button', permission: 'system:menu:add', sort: 1, status: 1 },
        { id: 2222, name: '编辑菜单', type: 'button', permission: 'system:menu:edit', sort: 2, status: 1 },
        { id: 2223, name: '删除菜单', type: 'button', permission: 'system:menu:delete', sort: 3, status: 1 }
      ]}
    ]}
  ]},

  // ==================== 个人中心 ====================
  { id: 3, name: '个人中心', type: 'directory', icon: 'UserFilled', path: '/user', sort: 3, status: 1, children: [
    { id: 31, name: '修改密码', type: 'menu', icon: 'Key', path: '/user/password', component: 'user/password/index', permission: 'user:password:view', sort: 1, status: 1, children: [
      { id: 311, name: '确认修改', type: 'button', permission: 'user:password:change', sort: 1, status: 1 }
    ]}
  ]}
]
