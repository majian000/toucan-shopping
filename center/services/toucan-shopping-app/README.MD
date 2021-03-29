###应用中心
    
    给各个应用提供应用服务
    端口号:8091
   

#### 依赖服务
1.toucan-shopping-config-server

    配置中心
    
2.toucan-shopping-eureka
    
    服务注册中心


#### 使用说明



### 应用服务

1.添加应用
    
    POST http://localhost:8087/app/save
    HEADER
        bbs-user-center-atuh:adminId=bb155acbf5ef43dcac9aa892274fadd5;loginToken=8b84093057c14d569fa94eb0bbdb19ac
        content-type:application/json;charset=UTF-8
    REQUEST BODY
    {"sign":"9bd190fac50df8d7212ee12df82cbd67","appCode":"10000001", "entityJson":"{ \"name":"商品中心02\","code\":\"10001003\",\"updateAdminId\":\"123456\",\"remark\": \"商品中心\" }"}
    
2.修改应用
    
    POST http://localhost:8087/app/update
    HEADER
        bbs-user-center-atuh:adminId=bb155acbf5ef43dcac9aa892274fadd5;loginToken=8b84093057c14d569fa94eb0bbdb19ac
        
    REQUEST BODY
    {
    	"sign": "9bd190fac50df8d7212ee12df82cbd67",
    	"appCode": "10000001",
    	"entityJson": "{ \"id\":\"11\", \"name\": \"toucan-shopping\",\"code\":\"10001003\",\"createAdminId\":\"123456\",\"remark\": \"商城应用\" }"
    }


3.添加管理员账号到指定应用

    POST http://localhost:8087/app/connect/admin
    { "username":"sa",
      "adminApps":[
        {"adminId":"d10590b318544049ba2104d1f5517a7d","appCode":"10001001","createAdminId":"d10590b318544049ba2104d1f5517a7d"},
        {"adminId":"d10590b318544049ba2104d1f5517a7d","appCode":"10001002","createAdminId":"d10590b318544049ba2104d1f5517a7d"}
      ] }
   
   
4.添加用户账号到指定应用

    POST http://localhost:8087/app/connect/user
    { "userId":"b0ec72880b894b09b5c803dd8fa78518",
      "userApps":[
        {"userId":"b0ec72880b894b09b5c803dd8fa78518","appCode":"10001001"},
        {"userId":"b0ec72880b894b09b5c803dd8fa78518","appCode":"10001002"}
      ] }
    
    
