# 微服务版

    犀鸟电商平台致力于快速搭建属于自己的电商网站

#### 介绍
    采用技术spring cloud、spring config、lombok、eureka、hystrix、elasticsearch、kafka、shardingsphere、redis、mysql

#### 软件架构

##### 公共基础模块
    toucan-shopping-modules 目录下包含订单编号生成、redis分布式锁、各种工具类等

##### 基础服务层
    base目录下包含配置中心、服务注册中心、网关等
    
##### 业务服务层
    services目录下包含商品服务、订单服务、秒杀服务、类别服务等
    api目录下包含商品服务、订单服务等feign接口
    starter目录下包含权限中台的插件
    
##### 应用层
    apps目录下包含商城pc端接口、商城管理端等


#### 安装教程
    
    建议java jdk 1.8.0_181及以上
    按顺序依次启动下面项目

    xshell linux
##### 配置中心
    源码:base/toucan-shopping-config-server
    启动:nohup java -jar toucan-shopping-config-server-1.0-SNAPSHOT.jar &
    查看:ps aux|grep toucan-shopping-config-server

##### 服务中心
    源码:base/toucan-shopping-eureka
    启动:nohup java -jar toucan-shopping-eureka-1.0-SNAPSHOT.jar  &
    查看:ps aux|grep toucan-shopping-eureka
    
##### 网关服务
    源码:base/toucan-shopping-gateway
    启动:nohup java -jar toucan-shopping-gateway-1.0-SNAPSHOT.jar &
    查看:ps aux|grep toucan-shopping-gateway
    
    
##### 用户服务
    源码:services/toucan-shopping-user
    启动:nohup java -jar toucan-shopping-user-1.0-SNAPSHOT.jar  &
    查看:ps aux|grep toucan-shopping-user
    
    

##### 依赖第三方
    1.mysql8
    2.redis 3.2
    3.apache-zookeeper-3.6.2
    4.kafka_2.12-2.7.0
    5.elasticsearch-7.10.2
    6.FastDFS_v5.08

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

    备份路径 resources/数据库备份/微服务版
    

##### 基础服务层
    1.base/toucan-shopping-config-server
    端口号:9090
    配置中心服务

    2.base/toucan-shopping-eureka
    端口号:8081
    服务中心

    3.base/toucan-shopping-gateway
    端口号:8089
    服务网关
    
##### 业务服务层
    
    1.services/toucan-shopping-product
    端口号:8082
    商品服务

    2.services/toucan-shopping-order
    端口号:8084
    订单服务、支付服务

    3.services/toucan-shopping-second-kill
    端口号:8085
    秒杀服务,使用请参考该项目目录下的README.MD
    
    4.services/toucan-shopping-category
    端口号:8090
    类别服务
    
    5.services/toucan-shopping-stock
    端口号:8093
    库存服务
    
    6.services/toucan-shopping-area
    端口号:8094
    地区编码、收货地区服务
    
    
    7.services/toucan-shopping-content
    端口号:8095
    新闻资讯服务
    
    8.services/toucan-shopping-column
    端口号:8096
    栏目服务
    
    9.services/toucan-shopping-user-center
    端口号:8087
    用户中心服务
    
    11.services/toucan-shopping-app-center
    端口号:8099
    权限中台服务


##### 应用层  
    
    1.apps/toucan-shopping-web
    端口号:8083
    前端界面服务

    2.apps/toucan-shopping-admin
    端口号:8086
    后台管理端

    3.apps/toucan-shopping-admin
    端口号:8088
    用户中心界面
    
    4.apps/toucan-shopping-admin-auth-web
    端口号:8098
    权限中台界面

    5.apps/toucan-shopping-scheduler
    端口号:8092
    商城调度任务端

    6.apps/toucan-shopping-user-scheduler
    端口号:8097
    用户中心任务调度

    
    
    

#### 应用编码

    10001001 商城应用
    10001002 商城后台
    10001003 权限中台
    

#### 参与贡献
    
    1.majian
    2.niuyuxiao