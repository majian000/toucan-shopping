# 微服务版

    犀鸟电商平台致力于快速搭建属于自己的电商网站

#### 介绍
    采用技术
    spring cloud
    spring config
    lombok
    nacos
    hystrix
    elasticsearch
    kafka
    shardingsphere
    redis
    mysql
    canal

#### 软件架构

##### 公共基础模块 
    toucan-shopping/toucan-shopping-modules
    包含订单编号生成、redis分布式锁、各种工具类等

##### API接入层
    toucan-shopping/toucan-shopping-cloud/api
    商品服务、订单服务等feign接口
    
##### 应用层
    toucan-shopping/toucan-shopping-cloud/apps
    包含商城pc端接口、商城管理端等

##### 基础服务层
    toucan-shopping/toucan-shopping-cloud/base
    包含配置中心、服务注册中心、网关等

##### 微服务架构下公共模块层
    toucan-shopping/toucan-shopping-cloud/modules
    包含微服务架构下的公共模块,和上面的公共基础模块层有区别(上面的是无关微服务架构还是spring Boot架构)
    
##### 微服务层
    toucan-shopping/toucan-shopping-cloud/services
    目录下包含商品服务、订单服务、秒杀服务、类别服务等
    
##### 插件层  
    包含权限中台、用户中心、日志邮件的插件等
    

#### 安装教程
    参考resources/上线相关/微服务版/生产环境.md
    注意:如果maven编译一次失败了,请尝试多编译几次(maven编译依赖顺序导致)

##### 依赖第三方
    1.mysql8
    2.redis 3.2
    3.apache-zookeeper-3.6.2
    4.kafka_2.12-2.7.0
    5.elasticsearch-7.10.2
    6.FastDFS_v5.08
    7.nacos-server-2.0.4 下载地址:https://github.com/alibaba/nacos/releases
    8.canal.deployer-1.1.7

##### 第三方配置

###### 配置ElasticSearch

    1.创建索引
    
    1.2.权限中台缓存
    PUT http://localhost:9200/admin_auth_admin_role_index?pretty    
    PUT http://localhost:9200/admin_auth_role_function_index?pretty
    PUT http://localhost:9200/admin_auth_function_index?pretty

    2.设置返回最大记录数
    
    PUT http://localhost:9200/admin_auth_admin_role_index/_settings
    Content-Type:application/json
    {
      "index": {
        "max_result_window": 10000
      }
    }
    
    PUT http://localhost:9200/admin_auth_role_function_index/_settings
    Content-Type:application/json
    {
      "index": {
        "max_result_window": 10000
      }
    }

    PUT http://localhost:9200/admin_auth_function_index/_settings
    Content-Type:application/json
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
 | base/toucan-shopping-eureka                  | 8081                   | 服务中心(替换成nacos,下载地址参考上面依赖第三方)  |
 | nacos-server-2.0.4                           | 8081                   | 服务中心                                           |
 | base/toucan-shopping-gateway                 | 8089                   | 服务网关                                           |
 
 
   
    
##### 业务服务层
    
 | 服务名                                        | 端口号                 | 说明                                             |
 | --------------------------------------------- | -------------------- | ---------------------------------------------------|
 | services/toucan-shopping-product             | 8082                   | 商品服务                                          |
 | services/toucan-shopping-order               | 8084                   | 订单服务、支付服务                                |
 | services/toucan-shopping-second-kill         | 8085                   | 秒杀服务                                          |
 | services/toucan-shopping-stock               | 8093                   | 库存服务                                          |
 | services/toucan-shopping-user                | 8087                   | 用户中心服务                                      |
 | services/toucan-shopping-admin-auth          | 8099                   | 权限中台服务                                      |
 | services/toucan-shopping-seller              | 8100                   | 卖家服务                                          |
 | services/toucan-shopping-message             | 8103                  | 消息服务                                           |
 | services/toucan-shopping-common-data         | 8104                  | 公共数据服务                                       |
 | services/toucan-shopping-content             | 8105                  | 内容服务                                           |
 | services/toucan-shopping-search             | 8106                  | 搜索服务                                           |

    

##### 应用层  
    
 | 服务名                                        | 端口号                 | 说明                                             |
 | --------------------------------------------- | -------------------- | ---------------------------------------------------|
 | apps/toucan-shopping-web                      | 8083                 | 商城C端界面                                        |
 | apps/toucan-shopping-admin                    | 8088                 | 商城管理界面                                       |
 | apps/toucan-shopping-admin-auth-web           | 8098                 | 权限中台界面                                       |
 | apps/toucan-shopping-scheduler                | 8092                 | 商城调度任务端                                     |
 | apps/toucan-shopping-user-scheduler           | 8097                 | 用户中心任务调度                                   |
 | apps/toucan-shopping-seller-web               | 8101                 | 商户WEB端                                          |
 | apps/toucan-shopping-admin-auth-scheduler     | 9001                 | 权限中台任务调度                                   |
 | apps/toucan-shopping-message-web              | 8102                 | 消息WEB应用                                        |
 
    
    

#### 应用编码
    注意:建议应用编码由字母、数字、下划线组成

    10001001 商城应用
    10001002 商城后台
    10001003 权限中台
    10001004 卖家应用端
    10001005 消息应用端
    

#### 参与贡献
    
    1.majian
    2.niuyuxiao