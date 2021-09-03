# 介绍
    
## 商城后台管理
    管理所有商城注册用户、轮播图、商品、订单等
    端口号:8088

#### 依赖模块及服务
    
    用户服务
    1.toucan-shopping-cloud/services/toucan-shopping-user
    2.toucan-shopping-modules/user/toucan-shopping-user-entity-vo
    3.toucan-shopping-modules/user/toucan-shopping-user-service-dao
    
    权限服务
    1.toucan-shopping-cloud/services/toucan-shopping-admin-auth
    2.toucan-shopping-modules/adminAuth/toucan-shopping-admin-auth-entity-vo
    3.toucan-shopping-modules/adminAuth/toucan-shopping-admin-auth-service-dao
        
### 管理界面
    
    访问地址 localhost:8088/login
    
    
    
### API

#### 用户服务
    
    1.查询用户列表
    POST http://localhost:8088/user/list
    第一个请求参数
    limit=500
    第二页请求参数,sortValues[0]值为上一页返回的值,设置进去即可
    limit=500&sortValues[0]=827214101934505996