###地区编码服务
    
    地区服务、轮播图服务
    端口号:8094
    

#### 依赖服务

    1.toucan-shopping-config-server

    配置中心
    
    2.toucan-shopping-eureka
    
    服务注册中心


### API

#### 轮播图服务

    1.保存轮播图
    POST http://localhost:8094/banner/admin/save
    请求头
    Content-Type:application/json;charset=UTF-8
    请求体
    {
    	"appCode": "10001001",
    	"entityJson": "{\"title\":\"测试轮播图\",\"appCode\": \"10001001\",\"areaCodeArray\":[\"110000\"]}"
    }
    
    2.查询轮播图
    POST http://localhost:8094/banner/user/query/list
    请求头
    Content-Type:application/json;charset=UTF-8
    请求体
    {     	"appCode": "10001001",     	"entityJson": "{\"appCode\": \"10001001\",\"areaCodeArray\":[\"110000\"]}"     }
    
    