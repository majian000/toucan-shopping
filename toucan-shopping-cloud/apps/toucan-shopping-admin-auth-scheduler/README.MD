### 权限中台调度端
    
    每隔1个小时 清空es缓存,进行刷新权限数据(账号角色关联、角色功能项关联、功能项列表)
    端口号:9001
    注:这个项目要只启动一个
   

#### 依赖服务
    1.toucan-shopping-config-server

    配置中心
    
    2.nacos-server-2.0.4
    
    服务注册中心


###### 配置ElasticSearch

    1.设置用户返回最大记录数
    PUT http://localhost:9200/user_index/_settings
    请求头
    Content-Type:application/json
    请求体
    {
      "index": {
        "max_result_window": 10000
      }
    }



#### Elasticsearch查询

    1.查询权限相关
    GET http://localhost:9200/admin_auth_admin_role_index/_search
    GET http://localhost:9200/admin_auth_role_function_index/_search
    GET http://localhost:9200/admin_auth_function_index/_search
    
    
### 手动刷新缓存
    1.刷新账号角色缓存
    GET http://localhost:9001/adminRoleToESCache/flush/all
    2.刷新角色功能项缓存    
    GET http://localhost:9001/roleFunctionToESCache/flush/all
    3.刷新功能项缓存
    GET http://localhost:9001/functionToESCache/flush/all
    
    