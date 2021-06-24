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
创建Redis容器
docker create --name redis-node1 -v /data/redis-data/node1:/data -p 6380:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-1.conf
docker create --name redis-node2 -v /data/redis-data/node2:/data -p 6381:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-2.conf
docker create --name redis-node3 -v /data/redis-data/node3:/data -p 6382:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-3.conf




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


可以查看容器信息
docker inspect redis-node1
找到
"IPAddress": "172.17.0.2",


docker inspect redis-node2
找到
"IPAddress": "172.17.0.3",


docker inspect redis-node3
找到
"IPAddress": "172.17.0.4",




进入容器连接各个redis形成集群
docker exec -it redis-node1 /bin/bash
连接集群
redis-cli --cluster create 172.17.0.2:6379  172.17.0.3:6379 172.17.0.4:6379 --cluster-replicas 0
输入yes
结果如下:
C:\Users\Administrator>docker exec -it redis-node1 /bin/bash
root@de27a476b730:/data# redis-cli --cluster create 172.17.0.2:6379  172.17.0.3:6379 172.17.0.4:6379 --cluster-replicas 0
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


