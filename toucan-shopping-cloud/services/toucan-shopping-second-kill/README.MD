###秒杀服务
    能抗住QPS 5000左右的并发,可以根据具体情况选择是否开启这个秒杀服务
    端口号:8085
    

#### 依赖服务
1.toucan-shopping-config-server

    配置中心
    
2.toucan-shopping-eureka
    
    服务注册中心

3.toucan-shopping-product

    商品服务
    
4.toucan-shopping-order

    订单服务


#### 使用说明
1.初始化接口

    初始化商品库存、商品活动状态
    POST http://localhost:8085/sk/init
    
2.秒杀接口

    使用前请调用初始化接口
    POST http://localhost:8085/sk/sk
    { "userId":"1", "entityJson":"{\"id\":1}" }


#### Redis Key介绍
1.商品活动状态(0结束 1进行中)

    格式:{appCode}_product_{skuId}_activity
    例如:10001002_product_1_activity

1.商品库存数量

    格式:{appCode}_product_{skuId}_stock
    例如:10001002_product_1_stock