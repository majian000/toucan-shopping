### 用户中心调度端
    
    启动的时候将用户中心的各个库同步到es中,用户信息变更的时候也会同步到es
    端口号:8097
    注:这个项目要只启动一个
   

#### 依赖服务
    1.toucan-shopping-config-server

    配置中心
    
    2.toucan-shopping-eureka
    
    服务注册中心


###### 配置ElasticSearch

    1.设置用户返回最大记录数
    PUT http://localhost:9200/user_index/_settings
    {
      "index": {
        "max_result_window": 10000
      }
    }



#### Elasticsearch查询

    1.查询所有初始化的用户
    GET http://localhost:9200/user_index/_search
    
    
    2.查询用户数量
    GET http://localhost:9200/user_index/_count
    
    
#### kafka消息
    
    1.用户修改消息
    组名称:user_group
    话题:user_modify
    
    
    
    
    