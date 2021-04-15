###栏目服务
    
    首页栏目、各个子站栏目相关的服务
    端口号:8096
    

#### 依赖服务

##### toucan-shopping-config-server

    配置中心
    
##### toucan-shopping-eureka
    
    服务注册中心



#### 使用说明

##### 查询指定区域下栏目列表
    
    POST http://localhost:8096/column/user/area/query/list
    请求头
    Content-Type:application/json;charset=UTF-8
    {"appCode":"10001001","entityJson":"{\"areaCode\":\"110000\",\"type\":\"1\",\"position\":\"1\"}"}

