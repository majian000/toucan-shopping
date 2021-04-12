###商城后台管理端
    
    管理商城商品SPU、商品SKU、商品类别、商品属性等
    

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

1.保存商品SKU接口
    
    POST http://localhost:8086/toucan-shopping-admin/productSku/save
    { "productId":1, "price":999.99,"appCode": "10001001", "attributes": "{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}",    "stockNum": 10,    "remark": "小米手机备注",    "status": 0      }

    
    

