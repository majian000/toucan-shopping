=============================================创建单节点

拉取镜像
docker  pull  redis:5.0.5

创建Redis容器
docker create --name redis-node1 -v /data/redis-data/node1:/data -p 6379:6379 redis:5.0.5 --cluster-enabled no --cluster-config-file nodes-node-1.conf

启动容器
docker start redis-node1




==============================================创建集群


拉取镜像
docker  pull  redis:5.0.5
创建Redis容器(端口映射模式)
docker create --name redis-node1 -v /data/redis-data/node1:/data -p 6380:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-1.conf
docker create --name redis-node2 -v /data/redis-data/node2:/data -p 6381:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-2.conf
docker create --name redis-node3 -v /data/redis-data/node3:/data -p 6382:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-3.conf

创建redis容器(host共享端口模式) 注:windows、macos系统下 这个模式无效
docker create --name redis-node1 --net host -v /data/redis-data/node1:/data redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-1.conf --port 6380
docker create --name redis-node2 --net host -v /data/redis-data/node2:/data redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-2.conf --port 6381
docker create --name redis-node3 --net host -v /data/redis-data/node3:/data redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-3.conf --port 6382

创建redis容器(指定IP)
docker create --name redis-node1 --network redis-cluster-net --ip 172.26.208.5 -v /data/redis-data/node1:/data -p 6380:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-1.conf
docker create --name redis-node2 --network redis-cluster-net --ip 172.26.208.6 -v /data/redis-data/node2:/data -p 6381:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-2.conf
docker create --name redis-node3 --network redis-cluster-net --ip 172.26.208.7 -v /data/redis-data/node3:/data -p 6382:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-3.conf


创建网络
docker network create --subnet=192.168.8.0/24 redis-cluster-net

查看网络
docker network ls

查看网络
docker network inspect redis-cluster-net

删除网络
docker network rm redis-cluster-net


查看创建的容器
docker ps -a

停止容器
docker stop redis-node1 redis-node2 redis-node3

删除容器
docker rm redis-node1 redis-node2 redis-node3

启动容器
docker start redis-node1 redis-node2 redis-node3


查看启动的容器
docker ps 

查看日志
docker logs redis-node1

可以查看容器信息
docker inspect redis-node1
查看,这个地址就是docker容器内分配的IP
"IPAddress": "172.17.0.2",


docker inspect redis-node2
查看,这个地址就是docker容器内分配的IP
"IPAddress": "172.17.0.3",


docker inspect redis-node3
查看,这个地址就是docker容器内分配的IP
"IPAddress": "172.17.0.4",



进入容器连接各个redis形成集群
docker exec -it redis-node1 /bin/bash
连接集群
redis-cli --cluster create 172.26.208.5:6379  172.26.208.6:6379 172.26.208.7:6379 --cluster-replicas 0
输入yes
结果如下:
C:\Users\Administrator>docker exec -it redis-node1 /bin/bash
root@de27a476b730:/data# redis-cli --cluster create 192.168.8.210:6380  192.168.8.210:6381 192.168.8.210:6382 --cluster-replicas 0
>>> Performing hash slots allocation on 3 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
M: fc269ac465693d60886e320e01dc8f1a2db69932 172.17.0.2:6379
   slots:[0-5460] (5461 slots) master
M: 70b20812b3650470231962bad26208c0e10fde12 172.17.0.3:6379
   slots:[5461-10922] (5462 slots) master
M: 92163a26a2877deff7c06305c66245162c4890be 172.17.0.4:6379
   slots:[10923-16383] (5461 slots) master
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
..
>>> Performing Cluster Check (using node 172.17.0.2:6379)
M: fc269ac465693d60886e320e01dc8f1a2db69932 172.17.0.2:6379
   slots:[0-5460] (5461 slots) master
M: 92163a26a2877deff7c06305c66245162c4890be 172.17.0.4:6379
   slots:[10923-16383] (5461 slots) master
M: 70b20812b3650470231962bad26208c0e10fde12 172.17.0.3:6379
   slots:[5461-10922] (5462 slots) master
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
root@de27a476b730:/data#


通过客户端连接集群
 docker exec -it redis-node1 /bin/bash
 redis-cli -c -p 6379
 
 



删除容器
docker rm redis-node1 redis-node2 redis-node3



查看日志
docker logs redis-node1


====================异常
[ERR] Node 192.168.8.210:6380 is not empty. Either the node already knows other nodes (check with CLUSTER NODES) or contains some key in database 0.
出现这个异常,删除redis下的集群各个节点
依次进入这三个IP
redis-cli -h 192.168.8.210 -p 6380
执行flushdb
执行cluster nodes
a88533adfc5ff1b8010c81dd8413cb6fa40850a4 172.17.0.4:6379@16379 master - 0 1624607156373 3 connected 10923-16383
22a0d04f3539bf3a004d5b39d79a2588ec9f43f2 172.17.0.3:6379@16379 master - 0 1624607155370 2 connected 5461-10922
44ead3cddf5b1e70ccb954dbb8894d9f66589d01 172.17.0.2:6379@16379 myself,master - 0 1624607154000 1 connected 0-5460
执行
cluster forget 22a0d04f3539bf3a004d5b39d79a2588ec9f43f2
cluster forget 44ead3cddf5b1e70ccb954dbb8894d9f66589d01



