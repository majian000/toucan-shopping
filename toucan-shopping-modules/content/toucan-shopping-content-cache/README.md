# 犀鸟电商平台

    犀鸟电商平台致力于快速搭建属于自己的电商网站

## 模块介绍
    地区服务缓存公共类
    
    
## 配置
    toucan:
      modules:
        areaCache:
          redis:
            select: single # 可选值 single:单实例 cluster:集群
            timeout: 10000
            password: #多个集群要求每个密码统一
            #单实例
            host: 127.0.0.1
            port: 6379
            database: 0
            #集群配置
            hosts: 127.0.0.1:6379 #多个用,分割
            maxRedirects: 3 #最大连接转移数
            #jedis pool配置
            maxActive: 50 #最大活跃数
            maxWait: 15 #最大等待数
            maxIdle: 50 #最大核心线程数
            minIdle: 0 #最小核心线程数