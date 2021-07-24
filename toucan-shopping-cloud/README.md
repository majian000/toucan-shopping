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
    参考resources/上线相关/微服务版/生产环境.md
    

##### 依赖第三方
    1.mysql8
    2.redis 3.2
    3.apache-zookeeper-3.6.2
    4.kafka_2.12-2.7.0
    5.elasticsearch-7.10.2
    6.FastDFS_v5.08

##### 第三方配置

###### 配置ElasticSearch

    1.创建索引
    PUT http://localhost:9200/user_index?pretty
    
    PUT http://localhost:9200/admin_role_index?pretty

    2.设置用户返回最大记录数
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
 | 服务名                                        | 端口号                 | 说明                                             |
 | --------------------------------------------- | -------------------- | ---------------------------------------------------|
 | base/toucan-shopping-config-server           | 9090                  | 配置中心服务                                       |
 | base/toucan-shopping-eureka                  | 8081                   | 服务中心                                           |
 | base/toucan-shopping-gateway                 | 8089                   | 服务网关                                           |
 
   
    
##### 业务服务层
    
 | 服务名                                        | 端口号                 | 说明                                             |
 | --------------------------------------------- | -------------------- | ---------------------------------------------------|
 | services/toucan-shopping-product             | 8082                   | 商品服务                                          |
 | services/toucan-shopping-order               | 8084                   | 订单服务、支付服务                                |
 | services/toucan-shopping-second-kill         | 8085                   | 秒杀服务,使用请参考该项目目录下的README.MD         |
 | services/toucan-shopping-category            | 8090                   | 类别服务                                         |
 | services/toucan-shopping-stock               | 8093                   | 库存服务                                          |
 | services/toucan-shopping-area                | 8094                   | 地区编码、收货地区服务                            |
 | services/toucan-shopping-content             | 8095                   | 新闻资讯服务                                      |
 | services/toucan-shopping-column              | 8096                   | 栏目服务                                          |
 | services/toucan-shopping-user-center         | 8087                   | 用户中心服务                                      |
 | services/toucan-shopping-app-center          | 8099                   | 权限中台服务                                      |
 | services/toucan-shopping-seller              | 8100                   | 卖家服务                                          |

    

##### 应用层  
    
 | 服务名                                        | 端口号                 | 说明                                             |
 | --------------------------------------------- | -------------------- | ---------------------------------------------------|
 | apps/toucan-shopping-web                      | 8083                 | 商城C端界面                                       |
 | apps/toucan-shopping-admin                    | 8088                 | 商城管理界面                                       |
 | apps/toucan-shopping-admin-auth-web           | 8098                 | 权限中台界面                                       |
 | apps/toucan-shopping-scheduler                | 8092                 | 商城调度任务端                                     |
 | apps/toucan-shopping-user-scheduler           | 8097                 | 用户中心任务调度                                   |
 | apps/toucan-shopping-seller-web               | 8101                 | 卖家WEB端                                         |
 
    

#### 应用编码

    10001001 商城应用
    10001002 商城后台
    10001003 权限中台
    10001004 卖家应用端
    

#### 参与贡献
    
    1.majian
    2.niuyuxiao