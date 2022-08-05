### 卖家客户端
    
    卖家申请,端口号8101
   

#### 依赖服务
    1.toucan-shopping-config-server
    配置中心
    
    2.nacos-server-2.0.4
    服务注册中心
    
    3.toucan-shopping-product
    商品服务
    
    3.toucan-shopping-order
    订单服务





#### 首页静态化
    
    1.生成预览版静态化
    http://localhost:8101/api/html/freeShop/generate/preview
    
    2.生成最终版静态化
    http://localhost:8101/api/html/freeShop/generate/release




#### 使用说明

    1.先去生成首页缓存
    访问:localhost:8088
    后台管理端》卖家端免费开店页静态生成
    
### 访问界面

    1.访问首页
    GET http://localhost:8083/index
