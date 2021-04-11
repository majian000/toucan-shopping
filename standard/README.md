# 标准版

    犀鸟电商平台致力于快速搭建属于自己的电商网站
    
#### 介绍
    采用技术spring boot、spring config、mybatis、lombok、elasticsearch、kafka、shardingsphere、redis、mysql

#### 软件架构

##### 公共基础模块
    modules目录下包含订单编号生成、redis分布式锁、各种工具类等

    

##### 依赖第三方
    1.mysql8
    2.redis 3.2
    3.apache-zookeeper-3.6.2
    4.kafka_2.12-2.7.0
    5.elasticsearch-7.10.2

##### 第三方配置

###### 配置ElasticSearch

    1.设置用户返回最大记录数
    PUT http://localhost:9200/user_index/_settings
    {
      "index": {
        "max_result_window": 10000
      }
    }

###### 配置redis
    
    1.配置redis RDB

##### 数据库

    
    

#### 应用编码

    10001001 商城应用
    10001002 用户中心
    10001003 权限中心
    

#### 参与贡献
1.majian
