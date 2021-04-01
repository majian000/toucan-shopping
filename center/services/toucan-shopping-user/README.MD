###用户中心服务

    用户中心服务
    端口号:8087

#### 依赖服务
1.toucan-shopping-config-server

    配置中心
    
2.toucan-shopping-eureka
    
    服务注册中心


#### 使用说明

### 用户服务

    1.发送注册验证码
    POST http://localhost:8087/sms/send
     { "mobile":"18701601892","type": 1 }
     
    
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

    


#### Redis Key介绍

    1.注册验证码


