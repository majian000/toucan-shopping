###类别服务
    
    类别增删改查服务
    端口号:8090
    

#### 依赖服务
1.toucan-shopping-config-server

    配置中心
    
2.toucan-shopping-eureka
    
    服务注册中心



#### 使用说明
    
##### 保存类别
    
    POST http://localhost:8090/category/admin/save
    请求头
    Content-Type:application/json;charset=UTF-8
    {"appCode":"10001001","entityJson":"{\"parentId\":\"-1\",\"name\":\"零食\",\"category_sort\":\"0\",\"type\":\"1\",\"remark\":\"类别备注\",\"createUserId\":\"-1\"}"}


##### 根据ID查询

    POST http://localhost:8090/category/admin/query/id
    {"appCode":"10001001","entityJson":"{\"id\":\"14\"}"}

##### 根据ID删除(并删除子节点)

    POST http://localhost:8090/category/admin/delete/id
    {"appCode":"10001001","entityJson":"{\"id\":\"14\"}"}
    
##### 修改类别

    POST http://localhost:8090/category/admin/update
    {"appCode":"10001001","entityJson":"{\"id\":\"23\",\"parentId\":\"-1\",\"name\":\"零食002\",\"category_sort\":\"0\",\"remark\":\"类别备注\",\"createUserId\":\"-1\"}"}
    
##### 根据地区编码查询类别
    
    POST http://localhost:8090/category/user/query/tree/area/code
    {"appCode":"10001001","entityJson":"{\"areaCode\":\"110000\"}"}

    

