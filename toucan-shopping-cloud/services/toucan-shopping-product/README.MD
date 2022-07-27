###商品服务
    涉及到对商品SPU、商品SKU、商品属性等操作
    端口号:8082
    

#### 依赖服务
    1.toucan-shopping-config-server

    配置中心
    
    2.nacos-server-2.0.4
    
    服务注册中心



#### 使用说明

    1.查询上架商品
    
    POST http://localhost:8082/productSku/shelves/list
    {"appCode": "10001001"}
    
    2.保存商品SKU
    
    POST http://localhost:8082/productSku/admin/save
    { "productId":1, "price":999.99,"appCode": "10001001", "attributes": "{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}",    "stockNum": 10,    "remark": "小米手机备注 主库",    "status": 0  ,"createUserId": 1    }  

