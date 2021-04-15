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
    
    POST http://localhost:8090/category/admin/save
    请求头
    Content-Type:application/json;charset=UTF-8
    {"appCode":"10001001","entityJson":"{\"parentId\":\"-1\",\"name\":\"零食\",\"category_sort\":\"0\",\"type\":\"1\",\"remark\":\"类别备注\",\"createUserId\":\"-1\"}"}

