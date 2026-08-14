/**
 * 权限中台 前端菜单配置
 *
 * 菜单层级与数据库 t_sa_function 表对应
 * permission 字段使用 toucan 前缀格式
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
  // ==================== 卖家管理 ====================
  {
    key: 'seller',
    label: '卖家管理',
    icon: 'Shop',
    permission: 'toucan:seller:seller',
    children: [
      // ----- 掌柜管理 -----
      {
        key: 'seller-shopkeeper',
        label: '掌柜管理',
        icon: 'Avatar',
        permission: 'toucan:seller:shop:list',
        children: [
          { path: '/seller/loginHistory', name: 'SellerLoginHistory', label: '登录历史列表', icon: 'Document', permission: 'toucan:seller:shop:login:history:list', component: () => import('@/views/seller/loginHistory/index.vue') }
        ]
      },
      // ----- 店铺管理 -----
      {
        key: 'seller-shop',
        label: '店铺管理',
        icon: 'OfficeBuilding',
        permission: 'toucan:seller:shop',
        children: [
          { path: '/seller/shopBanner', name: 'SellerShopBanner', label: '轮播图列表', icon: 'Picture', permission: 'toucan:seller:shopBanner:list', component: () => import('@/views/seller/shopBanner/index.vue') },
          { path: '/seller/freightTemplate', name: 'SellerFreightTemplate', label: '运费模板列表', icon: 'Van', permission: 'toucan:seller:freightTemplate:list', component: () => import('@/views/seller/freightTemplate/index.vue') },
          { path: '/seller/shop', name: 'SellerShop', label: '店铺列表', icon: 'OfficeBuilding', permission: 'toucan:seller:shop:list', component: () => import('@/views/seller/shop/index.vue') }
        ]
      },
      // ----- 静态化管理 -----
      {
        key: 'seller-static',
        label: '静态化管理',
        icon: 'Files',
        permission: 'toucan:seller:staticManager',
        children: [
          { path: '/seller/freeShop', name: 'SellerFreeShop', label: '免费开店页', icon: 'Document', permission: 'toucan:seller:indexGenerator', component: () => import('@/views/seller/freeShop/index.vue') }
        ]
      },
      // ----- 页面装修 -----
      {
        key: 'seller-decorate',
        label: '页面装修',
        icon: 'Postcard',
        permission: 'toucan:seller:pageDecorate',
        children: [
          { path: '/seller/designerImage', name: 'SellerDesignerImage', label: '装修图片', icon: 'Picture', permission: 'shopping:seller:pageDecorate:designerImage', component: () => import('@/views/seller/designerImage/index.vue') },
          { path: '/seller/pageModel', name: 'SellerPageModel', label: '页面列表', icon: 'Grid', permission: 'shopping:seller:pageDecorate:pageModel', component: () => import('@/views/seller/pageModel/index.vue') }
        ]
      }
    ]
  },
  // ==================== 用户管理 ====================
  {
    key: 'user',
    label: '用户管理',
    icon: 'User',
    permission: 'toucan:user:user',
    children: [
      // ----- 用户管理 -----
      {
        key: 'user-list',
        label: '用户管理',
        icon: 'UserFilled',
        permission: 'toucan:user:user',
        children: [
          { path: '/user/user', name: 'UserList', label: '用户列表', icon: 'User', permission: 'toucan:user:list', component: () => import('@/views/user/user/index.vue') }
        ]
      },
      { path: '/user/trueNameApprove', name: 'UserTrueNameApprove', label: '实名审核', icon: 'Stamp', permission: 'toucan:user:trueNameApprove', component: () => import('@/views/user/trueNameApprove/index.vue') },
      { path: '/user/headSculptureApprove', name: 'UserHeadSculptureApprove', label: '头像审核', icon: 'Picture', permission: 'toucan:user:headSculptureApprove', component: () => import('@/views/user/headSculptureApprove/index.vue') },
      { path: '/user/loginHistory', name: 'UserLoginHistory', label: '登录历史', icon: 'Document', permission: 'toucan:user:loginHistory:list', component: () => import('@/views/user/loginHistory/index.vue') },
      { path: '/user/collectProduct', name: 'UserCollectProduct', label: '收藏商品', icon: 'Star', permission: 'toucan:user:collectProduct', component: () => import('@/views/user/collectProduct/index.vue') }
    ]
  },
  // ==================== 商品管理 ====================
  {
    key: 'product',
    label: '商品管理',
    icon: 'Goods',
    permission: 'toucan:product:product',
    children: [
      // ----- 库存管理 -----
      {
        key: 'stock',
        label: '库存管理',
        icon: 'Box',
        permission: 'toucan:stock:stock',
        children: [
          { path: '/stock/productSkuStockLock', name: 'StockProductSkuStockLock', label: '商品库存锁定', icon: 'Lock', permission: 'toucan:stock:productSkuStockLock:list', component: () => import('@/views/stock/productSkuStockLock/index.vue') }
        ]
      },
      // ----- 商品统计 -----
      {
        key: 'product-statistic',
        label: '商品统计',
        icon: 'TrendCharts',
        permission: 'toucan:product:statistic',
        children: [
          { path: '/product/hotSellStatistic', name: 'ProductHotSellStatistic', label: '热销统计', icon: 'Histogram', permission: 'toucan:product:statistic:hotSellStatistic', component: () => import('@/views/product/hotSellStatistic/index.vue') },
          { path: '/product/skuStatistic', name: 'ProductSkuStatistic', label: '商品SKU统计', icon: 'DataAnalysis', permission: 'toucan:product:statistic:categoryProductStatistic', component: () => import('@/views/product/skuStatistic/index.vue') }
        ]
      },
      // ----- 商品管理 -----
      {
        key: 'product-list',
        label: '商品管理',
        icon: 'Goods',
        permission: 'toucan:product:product',
        children: [
          { path: '/product/skuSearch', name: 'ProductSkuSearch', label: '商品搜索', icon: 'Search', permission: 'toucan:product:sku:search:list', component: () => import('@/views/product/skuSearch/index.vue') },
          { path: '/product/productSku', name: 'ProductProductSku', label: '店铺SKU列表', icon: 'List', permission: 'toucan:product:sku:list', component: () => import('@/views/product/productSku/index.vue') },
          { path: '/product/shopProduct', name: 'ProductShopProduct', label: '店铺商品列表', icon: 'List', permission: 'toucan:product:sku:list', component: () => import('@/views/product/shopProduct/index.vue') },
          { path: '/product/shopProductApprove', name: 'ProductShopProductApprove', label: '商品审核', icon: 'Checked', permission: 'toucan:product:approve:list', component: () => import('@/views/product/shopProductApprove/index.vue') },
          { path: '/product/productSpu', name: 'ProductProductSpu', label: '平台SPU管理', icon: 'Files', permission: 'toucan:product:spu:list', component: () => import('@/views/product/productSpu/index.vue') },
          { path: '/product/attributeKey', name: 'ProductAttributeKey', label: '属性管理', icon: 'Filter', permission: 'toucan:product:attribute:key:menu', component: () => import('@/views/product/attributeKey/index.vue') },
          { path: '/product/brand', name: 'ProductBrand', label: '品牌管理', icon: 'PriceTag', permission: 'toucan:product:brand', component: () => import('@/views/product/brand/index.vue') },
          { path: '/product/colorTable', name: 'ProductColorTable', label: '颜色表管理', icon: 'Brush', permission: 'toucan:colorTable:colorTable', component: () => import('@/views/product/colorTable/index.vue') }
        ]
      }
    ]
  },
  // ==================== 订单管理 ====================
  {
    key: 'order',
    label: '订单管理',
    icon: 'Tickets',
    permission: 'toucan:order:order',
    children: [
      // ----- 订单管理 -----
      {
        key: 'order-list',
        label: '订单管理',
        icon: 'Tickets',
        permission: 'toucan:order:order',
        children: [
          { path: '/order/order', name: 'OrderOrder', label: '订单列表', icon: 'List', permission: 'toucan:order:order:menu', component: () => import('@/views/order/order/index.vue') }
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
      // ----- 静态化管理 -----
      {
        key: 'content-static',
        label: '静态化管理',
        icon: 'Files',
        permission: 'toucan:static:manager',
        children: [
          { path: '/content/htmlPage/index', name: 'ContentHtmlPageIndex', label: 'PC端首页', icon: 'Monitor', permission: 'toucan:dashboard:htmlGenerator', component: () => import('@/views/content/htmlPage/index.vue') },
          { path: '/content/htmlPage/area', name: 'ContentHtmlPageArea', label: '选择地区组件', icon: 'Location', permission: 'toucan:area:htmlGenerator', component: () => import('@/views/content/htmlPage/areaGenerator.vue') }
        ]
      },
      // ----- 文章管理 -----
      {
        key: 'content-article',
        label: '文章管理',
        icon: 'Notebook',
        permission: 'toucan:content:article',
        children: [
          { path: '/content/articleImage', name: 'ContentArticleImage', label: '文章图片列表', icon: 'Picture', permission: 'toucan:content:articleImage:list', component: () => import('@/views/content/articleImage/index.vue') },
          { path: '/content/article', name: 'ContentArticle', label: '文章列表', icon: 'Document', permission: 'toucan:content:article:list', component: () => import('@/views/content/article/index.vue') }
        ]
      },
      // ----- 栏目管理 -----
      {
        key: 'column',
        label: '栏目管理',
        icon: 'Menu',
        permission: 'toucan:content:columnType:list',
        children: [
          { path: '/content/columnType', name: 'ContentColumnType', label: '栏目分类', icon: 'Collection', permission: 'toucan:content:columnType:list', component: () => import('@/views/content/columnType/index.vue') },
          { path: '/content/column', name: 'ContentColumn', label: '栏目列表', icon: 'Menu', permission: 'toucan:content:column:list', component: () => import('@/views/content/column/index.vue') },
          { path: '/content/indexRecommendColumn', name: 'ContentIndexRecommendColumn', label: '首页推荐栏目', icon: 'Star', permission: 'toucan:content:column:list', component: () => import('@/views/content/indexRecommendColumn/index.vue') }
        ]
      },
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
      }
    ]
  }
]
