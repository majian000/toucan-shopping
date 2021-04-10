# 性能调优

#### 阿里云ECS linux 64位 CPU单核/内存1G

    xshell linux
    1.1.配置中心
    启动:nohup java -jar -Xmn20M -Xms10m toucan-shopping-config-server-1.0-SNAPSHOT.jar  &
    说明:堆最大20MB 初始化10MB
    
    2.服务中心
    启动:nohup java -jar -Xmn50M -Xms20m toucan-shopping-eureka-1.0-SNAPSHOT.jar  &
    说明:堆最大50MB 初始化20MB
    
    
    3.网关服务
    启动:nohup java -jar -Xmn30M -Xms10m toucan-shopping-gateway-1.0-SNAPSHOT.jar  &
    说明:堆最大30MB 初始化10MB
    
    
    4.用户服务
    启动:nohup java -jar -Xmn100M -Xms50m toucan-shopping-user-1.0-SNAPSHOT.jar  &
    说明:堆最大100MB 初始化50MB
    

#### 参与贡献
1.majian
