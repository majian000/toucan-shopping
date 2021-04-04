# 大嘴鸟电商平台

    大嘴鸟电商平台(一款开源免费的电商网站,前身是黑鸟电商平台,这个电商平台的设计是目标是千万级的用户量) 采用技术spring cloud、spring config、lombok、eureka、hystrix、elasticsearch、kafka、shardingsphere、redis、mysql

#### 介绍
    采用技术spring cloud、spring config、lombok、eureka、hystrix、elasticsearch、kafka、shardingsphere、redis、mysql

#### 软件架构

##### 公共基础模块
    modules目录下包含订单编号生成、redis分布式锁、各种工具类等

##### 基础服务层
    base目录下包含配置中心、服务注册中心、网关等
    
##### 业务服务层
    services目录下包含商品服务、订单服务、秒杀服务、类别服务等
    export目录下包含商品服务导出类、订单服务导出类等实体以及VO相关
    api目录下包含商品服务、订单服务等feign接口
    center目录下包含用户中心、权限中心等
    starter目录下包含权限中心的插件
    
##### 应用层
    apps目录下包含商城pc端接口、商城管理端等


#### 安装教程
    
    要求java jdk 1.8.0_181及以上
    按顺序依次启动下面项目


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

    
    商城库 resources/ddl/toucan_shopping.sql
    商城首页、轮播图相关等
    
    订单库 resources/ddl/toucan_shopping_order.sql
    主订单、子订单等
    
    商品库 resources/ddl/toucan_shopping_product.sql
    商品SPU、商品SKU等
    
    库存   resources/ddl/toucan_shopping_stock.sql
    商品库存等
    
    用户中心库 resources/用户中心
    记录用户信息、用户密码等
    
    地区服务库 resources/地区服务
    地区编码、轮播图等
    

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
    
    10.services/toucan-shopping-app-center
    端口号:8091
    应用中心服务
    
    11.services/toucan-shopping-app-center
    端口号:8092
    应用中心服务


##### 应用层  
    
    1.apps/toucan-shopping-web
    端口号:8083
    前端界面服务,使用请参考该项目目录下的README.MD



    2.apps/toucan-shopping-admin
    端口号:8086
    后台管理端,使用请参考该项目目录下的README.MD


    3.apps/toucan-shopping-user-center-web
    端口号:8088
    用户中心界面
    
    4.apps/toucan-shopping-admin-auth-web
    端口号:8098
    权限中心界面

    5.apps/toucan-shopping-scheduler
    端口号:8092
    商城调度任务端

    6.apps/toucan-shopping-user-scheduler
    端口号:8097
    用户中心任务调度

    
    
    

#### 应用编码

    10001001 商城应用
    10001002 用户中心
    10001003 权限中心
    

#### 参与贡献
1.majian
