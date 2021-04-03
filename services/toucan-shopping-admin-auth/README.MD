###权限中心
    
    给各个后台提供权限服务
    端口号:未定义
   

#### 依赖服务
1.toucan-shopping-config-server

    配置中心
    
2.toucan-shopping-eureka
    
    服务注册中心


### 管理权限服务
1.管理员登录
    
    POST http://localhost:8087/admin/login
    {"appCode":"10000001","entityJson":"{\"password\":\"123456\",\"username\":\"sa\"}","sign":"c10bc10b5ba92053797b2ae000d1d452"}

2.添加管理员

    POST http://localhost:8087/admin/save
    HEADER bbs-user-center-atuh:adminId=bb155acbf5ef43dcac9aa892274fadd5;loginToken=ef8ec700a3f04badae5e3cd100162d9e
    {"sign":"9bd190fac50df8d7212ee12df82cbd67","appCode":"10000001", "entityJson":"{\"username\":\"sa\",\"password\":\"123456\" }"}
   