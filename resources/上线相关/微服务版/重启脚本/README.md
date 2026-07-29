## 重启脚本使用说明

### 文件结构

```
重启脚本/
├── README.md
├── 8.140.187.184/          # 主服务器脚本
│   ├── restart-cloud-config-server.sh
│   ├── restart-gateway.sh
│   ├── restart-admin-auth-web.sh
│   ├── restart-web.sh
│   ├── restart-admin.sh
│   ├── restart-seller-web.sh
│   ├── restart-message-web.sh
│   ├── restart-admin-auth-scheduler.sh
│   ├── restart-user.sh
│   ├── restart-common-data.sh
│   ├── restart-content.sh
│   ├── restart-admin-auth.sh
│   └── restart-seller.sh
│
└── 123.56.127.178/        # 服务节点脚本
    ├── restart-message.sh
    ├── restart-user.sh
    ├── restart-product.sh
    ├── restart-stock.sh
    ├── restart-order.sh
    ├── restart-search.sh
    ├── restart-api-monitor.sh
    └── restart-scheduler.sh
```

### 首次部署到服务器

把脚本一次性传到服务器的 JAR 包目录 `/usr/toucan_shopping/`：

```bash
# 主服务器（-p 保留文件权限）
scp -p 8.140.187.184/* root@8.140.187.184:/usr/toucan_shopping/

# 服务节点
scp -p 123.56.127.178/* root@123.56.127.178:/usr/toucan_shopping/
```

### 日常使用

```bash
# 方式一：SSH 远程执行
ssh root@8.140.187.184 "cd /usr/toucan_shopping && ./restart-user.sh"
ssh root@123.56.127.178 "cd /usr/toucan_shopping && ./restart-order.sh"

# 方式二：登录服务器后执行
cd /usr/toucan_shopping
./restart-user.sh

# 查看日志
tail -f /usr/toucan_shopping/logs/user.log
```

### 参数修改

**方式一：直接改脚本（推荐）**

打开对应的 `restart-xxx.sh`，修改参数后重新 scp 到服务器。

需要改的参数通常就这几个：
- `-Xmx800m` — 内存上限
- `--toucan.port=8087` — 服务端口
- `--spring.profiles.active=prod` — 激活的 profile

**方式二：改生成器重新生成**

如果参数变动很大，修改项目根目录的 `tools/gen-restart-scripts.sh`，找到对应服务的 `write_script` 调用行，改完参数后重新生成：

```bash
# 在项目根目录执行
bash tools/gen-restart-scripts.sh
```

`write_script` 的参数格式：

```
write_script "服务器IP" "服务名" "中文名" "Xmx" "额外JVM参数" "应用参数"
```

示例——给 user 服务加一个自定义参数：

```bash
# 改之前
write_script "8.140.187.184" "user" "用户服务" "800m" "" \
  "--spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8087 ..."

# 改之后（加了一个 --toucan.custom.flag=true）
write_script "8.140.187.184" "user" "用户服务" "800m" "" \
  "--spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8087 ... --toucan.custom.flag=true"
```

### 添加新服务

在 `tools/gen-restart-scripts.sh` 末尾对应服务器的区块里，加一行 `write_script` 调用，然后重新生成：

```bash
# 例如：给 123.56.127.178 新增一个服务
write_script "123.56.127.178" "new-service" "新服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=9000 ..."
```

### 脚本做了什么

1. `kill -9` 杀掉旧进程
2. `nohup java ...` 启动新进程
3. 等 3 秒确认进程存活，失败则打印日志

日志位置：`/usr/toucan_shopping/logs/<服务名>.log`
