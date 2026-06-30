### 商城客户端
    
    购买商品、查询商品、商城首页,端口号 8083
   

#### 依赖服务
    1.toucan-shopping-config-server
    配置中心
    
    2.nacos-server-2.0.4
    服务注册中心
    
    3.toucan-shopping-product
    商品服务
    
    3.toucan-shopping-order
    订单服务


#### 使用说明

    1.先去生成首页缓存
    访问:localhost:8088
    后台管理端》首页静态生成

    2.不生成缓存
    访问:localhost:8083/dynamic/index

### 访问界面

    1.访问首页
    GET http://localhost:8083/index

### API

#### 用户服务
    
    1.用户登录
    POST http://localhost:8083/api/user/login/password
    请求头 Content-Type:application/json;charset=UTF-8
    请求内容
    {
        "loginUserName":"18701601892",
        "password":"123456"
    }
    响应
    {
      "code": 1,
      "msg": "操作成功",
      "data": {
        "loginUserName": "18701601892",
        "password": null,
        "loginToken": "355edd51285d4188a42c70c108683b7e",
        "appCode": "10001001",
        "email": null,
        "mobilePhone": null,
        "username": null
      }
    }
    响应头
    Set-Cookie: uid=177898789778;lt=355edd51285d4188a42c70c108683b7e

#### 访问
    直接访问首页(不使用页面缓存)
    http://localhost:8083/dynamic/index
    
    需要启动以下服务:
    base/toucan-shopping-cloud-config-server
    base/toucan-shopping-gateway
    apps/toucan-shopping-web
    services/toucan-shopping-user
    services/toucan-shopping-stock
    services/toucan-shopping-seller
    services/toucan-shopping-product
    services/toucan-shopping-order
    services/toucan-shopping-common-data

#### 页面缓存
    
    
    需要启动以下服务:
    apps/toucan-shopping-admin
    base/toucan-shopping-cloud-config-server
    base/toucan-shopping-gateway
    services/toucan-shopping-admin-auth
    services/toucan-shopping-common-data
    services/toucan-shopping-product
    services/toucan-shopping-content
    services/toucan-shopping-user
    
    然后进入首页管理》静态页面生成



#### 商品服务

    1.购买商品

    POST http://localhost:8083/product/buy
    HEADER Content-Type:application/json;charset=UTF-8
    BODY
    { "userId":1,"productSkuList":[{"id":1,"uuid":"0000163b10f067dbf11eb85a380a5893fe2d8"},{"id":2,"uuid":"0000165c2b8b77dbf11eb85a380a5893fe2d8"}],"buyMap":{"1":{"uuid":"000016d60a39cf4cf4bccb1159fec61ba147b","buyNum":1},"2":{"uuid":"000016d60a39cf4cf4bccb1159fec61ba147b","buyNum":1} } }
     
    2.调用支付宝

    POST http://localhost:8083/order/alipay
    HEADER Content-Type:application/json;charset=UTF-8
    BODY
    { "userId":1,"orderNo":"22011614674382822000000000058771" }
    
    
    3.支付宝回调

    POST http://localhost:8083/order/alipay/callback
    FORM DATA
        userId=1
        orderNo=22011614680037758000000000008562


### HTTP请求头
    
    1.登录cookie
    cookie:uid=177898789778&lt=355edd51285d4188a42c70c108683b7e