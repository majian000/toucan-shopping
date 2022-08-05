###用户中心服务

    用户中心服务
    端口号:8087

#### 依赖服务
1.toucan-shopping-config-server

    配置中心
    
2.nacos-server-2.0.4
    
    服务注册中心


#### 使用说明

### 用户服务

    1.发送注册验证码
    POST http://localhost:8087/sms/send
    {
        "appCode": "10001001",
        "entityJson": " { \"mobilePhone\":\"18701601892\",\"type\": 1 }"
    }
     
     

     
    
    2.注册
    POST http://localhost:8087/user/regist/mobile/phone
    HEADER Content-Type:application/json;charset=UTF-8
    {
    	"appCode": "10001001",
    	"entityJson": "{\"mobilePhone\":\"18701601892\",\"password\":\"123456\"}"
    }
    
    3.登录
    POST http://localhost:8087/user/login/password
    HEADER Content-Type:application/json;charset=UTF-8
    {
    	"appCode": "10001001",
    	"entityJson": "{\"loginUserName\":\"18701601892\",\"password\":\"123456\"}"
    }
    
    4.查询用户列表
    POST http://localhost:8087/user/list
    请求头
    Content-Type:application/json;charset=UTF-8
    请求体
    {         "appCode": "10000001",         "entityJson": "{\"page\":1,\"limit\":10 }"     }
    

### 用户详情服务
    
    1.修改用户详情
    POST http://localhost:8087/user/detail/update
    HEADER Content-Type:application/json;charset=UTF-8 
    {
    	"appCode": "10001001",
    	"entityJson": "{\"userMainId\":\"827213823088787470\",\"nickName\":\"一念之间\",\"sex\":\"1\"}"
    }


#### kafka消息
    
    1.用户详情修改
    组名称:user_group
    话题:user_detail_modify
    
    
    


#### Redis Key介绍



