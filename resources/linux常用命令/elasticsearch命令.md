###部署elasticsearch

    1.创建新用户
    useradd testuser
    passwd testuser
    
    2.输入密码:
    toucp_5432

    3.授予权限
    chown -R testuser:testuser /usr/elasticsearch-7.11.2
    
    4.授予es数据存储目录权限
    chown -R testuser:testuser /usr/path/to/data
    chown -R testuser:testuser /usr/path/to/logs
    
    5.切换用户
    su testuser
    
    6.设置
    elasticsearch文件权限属性为777
    
    7.启动es
    nohup  ./elasticsearch  &
    
    注意:linux内存起码要3个G以上 要不然运行不起来elasticsearch



###设置elasticsearch密码

    1.配置elasticsearch-7.11.2\config\elasticsearch.yml,最下面添加
    xpack.security.enabled: true
    xpack.license.self_generated.type: basic
    xpack.security.transport.ssl.enabled: true

    2.进入elasticsearch-7.11.2\bin目录
    elasticsearch-setup-passwords interactive
    
    用户名:elastic
    密码:toucp_5432
