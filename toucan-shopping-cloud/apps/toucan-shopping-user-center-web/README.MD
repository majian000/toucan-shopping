# 介绍
    
## 用户中心
    管理所有商城注册用户
    端口号:8088
        
### 用户中心界面
    
    需要启动center/services/toucan-shopping-user
    访问地址 localhost:8088/login
    
    
    
### API

#### 用户服务
    
    1.查询用户列表
    POST http://localhost:8088/user/list
    第一个请求参数
    limit=500
    第二页请求参数,sortValues[0]值为上一页返回的值,设置进去即可
    limit=500&sortValues[0]=827214101934505996