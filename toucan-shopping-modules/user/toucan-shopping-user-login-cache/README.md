# 犀鸟电商平台

    犀鸟电商平台致力于快速搭建属于自己的电商网站

## 模块介绍
    用户登录缓存模块,可通过用户主ID 路由到不同的redis集群,实现大数据量下用户登录的会话
    
## 配置介绍


    userLoginCache:
      cache-type: redis
      redis-type: default  #实现方式类型 default 默认采用官方配置 customSharding 自定义分片
      default-redis:
        select: single # 可选值 single:单实例 cluster:集群
        timeout: 10000
        password: mj_tocan_redis_7612 #多个集群要求每个密码统一
        #单实例
        host: 8.140.187.184
        port: 6379
        database: 0
        #集群配置
        hosts: 192.168.80.129:6380,192.168.80.129:6381,192.168.80.129:6382 #多个用,分割
        maxRedirects: 3 #最大连接转移数
        #jedis pool配置
        maxActive: 50 #最大活跃数
        maxWaitMillis: 15 #最大等待数
        maxIdle: 50 #最大核心线程数
        minIdle: 0 #最小核心线程数
      custom-sharding-redis:
        login-cache-redis-list[0]:
          index: 0 #通过用户ID取余得到索引位置
          dbCount: 16 #这个节点的db数量
          select: single # 可选值 single:单实例 cluster:集群
          timeout: 10000
          password: mj_tocan_redis_7612 #多个集群要求每个密码统一
          #单实例
          host: 8.140.187.184
          port: 6379
          maxRedirects: 3 #最大连接转移数
          #jedis pool配置
          maxActive: 50 #最大活跃数
          maxWaitMillis: 15 #最大等待数
          maxIdle: 50 #最大核心线程数
          minIdle: 0 #最小核心线程数
        login-cache-redis-list[1]:
          index: 1 #通过用户ID取余得到索引位置
          dbCount: 16 #这个节点的db数量
          select: single # 可选值 single:单实例 cluster:集群
          timeout: 10000
          password:  #多个集群要求每个密码统一
          #单实例
          host: 127.0.0.1
          port: 6379
          maxRedirects: 3 #最大连接转移数
          #jedis pool配置
          maxActive: 50 #最大活跃数
          maxWaitMillis: 15 #最大等待数
          maxIdle: 50 #最大核心线程数
          minIdle: 0 #最小核心线程数


