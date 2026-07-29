#!/bin/bash
# 根据.md文件中的启动命令生成所有重启脚本
# 用法: bash gen-restart-scripts.sh

set -e
cd "$(dirname "$0")"

OUTPUT_BASE="."
mkdir -p "${OUTPUT_BASE}/8.140.187.184"
mkdir -p "${OUTPUT_BASE}/123.56.127.178"

JVM_COMMON="-XX:+UseG1GC -XX:SoftRefLRUPolicyMSPerMB=0 -Djava.awt.headless=true -Dfile.encoding=UTF-8 -XX:ParallelGCThreads=4 -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=80 -XX:MaxTenuringThreshold=15 -XX:+HeapDumpOnOutOfMemoryError -Xlog:gc*:file=./gc.log:time,level,tags:filecount=5,filesize=10M"

write_script() {
  local SERVER_IP="$1"
  local SVC_NAME="$2"
  local SVC_TITLE="$3"
  local XMX="$4"
  local EXTRA_JVM="$5"
  local APP_ARGS="$6"
  local DIR="${OUTPUT_BASE}/${SERVER_IP}"
  local FILE="${DIR}/restart-${SVC_NAME}.sh"

  cat > "${FILE}" << SCRIPTEOF
#!/bin/bash
# ==========================================
# 犀鸟电商 - ${SVC_TITLE} 重启脚本
# 服务器: ${SERVER_IP}
# ==========================================

SERVICE="toucan-shopping-${SVC_NAME}"
DEPLOY_DIR="/usr/toucan_shopping"
LOG_DIR="/usr/toucan_shopping/logs"
mkdir -p \${LOG_DIR}

echo "===== 重启 \${SERVICE} (${SVC_TITLE}) ====="

# ===== 1. 停服 =====
# 用JAR包名精确匹配，避免 admin 误匹配 admin-auth / admin-auth-web 等
OLD_PID=\$(ps aux | grep "toucan-shopping-${SVC_NAME}-[0-9]" | grep -v grep | awk '{print \$2}')
if [ -n "\${OLD_PID}" ]; then
    echo "[1/3] 停止旧进程 PID: \${OLD_PID}"
    kill \${OLD_PID} 2>/dev/null || true
    for i in \$(seq 1 10); do
        sleep 2
        STATE=\$(ps -o state= -p \${OLD_PID} 2>/dev/null || echo "")
        if [ -z "\${STATE}" ] || [ "\${STATE}" = "Z" ]; then
            echo "      已停止"
            break
        fi
        if [ \$i -ge 3 ]; then
            echo "      强杀..."
            kill -9 \${OLD_PID} 2>/dev/null || true
        fi
        if [ \$i -eq 10 ]; then
            echo "      停止失败! 状态: \${STATE}, 请手动 kill -9 \${OLD_PID}"
            exit 1
        fi
    done
else
    echo "[1/3] 服务未运行，跳过停服"
fi

cd \${DEPLOY_DIR}

# ===== 2. 启动 =====
echo "[2/3] 启动..."
nohup java -Xmx${XMX} ${EXTRA_JVM} ${JVM_COMMON} \\
    -jar toucan-shopping-${SVC_NAME}-1.0-SNAPSHOT.jar \\
    ${APP_ARGS} \\
    > \${LOG_DIR}/${SVC_NAME}.log 2>&1 &

NEW_PID=\$!
echo "      新进程 PID: \${NEW_PID}"

# ===== 3. 检查 =====
sleep 3
if ps -p \${NEW_PID} > /dev/null 2>&1; then
    echo "[3/3] 启动成功! PID: \${NEW_PID}"
else
    echo "[3/3] 启动失败! 查看日志: tail -20 \${LOG_DIR}/${SVC_NAME}.log"
    tail -20 "\${LOG_DIR}/${SVC_NAME}.log" 2>/dev/null || echo "(日志为空)"
    exit 1
fi
echo ""
SCRIPTEOF
  chmod +x "${FILE}"
  echo "  OK ${SERVER_IP}/restart-${SVC_NAME}.sh"
}

# =============================================
# 8.140.187.184 (主服务器)
# =============================================
echo "=== 8.140.187.184 (主服务器) ==="

write_script "8.140.187.184" "cloud-config-server" "配置中心" "100m" "-Xss256k" \
  "--toucan.config.server.log.path=/log/prod/toucan_shopping/toucan_shopping_config_server"

write_script "8.140.187.184" "gateway" "网关服务" "400m" "" \
  "--spring.profiles.active=prod --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.ip=8.140.187.184 --toucan.port=8089 --toucan.config.server.ip=8.140.187.184"

write_script "8.140.187.184" "admin-auth-web" "权限中台" "300m" "" \
  "--spring.profiles.active=cloud_prod --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.config.server.ip=8.140.187.184 --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "web" "商城WEB端" "300m" "" \
  "--spring.profiles.active=cloud_prod --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.config.server.ip=8.140.187.184 --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "admin" "商城管理端" "400m" "" \
  "--spring.profiles.active=cloud_prod --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.config.server.ip=8.140.187.184 --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "seller-web" "卖家WEB端" "200m" "" \
  "--spring.profiles.active=cloud_prod --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.config.server.ip=8.140.187.184 --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "message-web" "消息WEB端" "200m" "" \
  "--spring.profiles.active=cloud_prod --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.config.server.ip=8.140.187.184 --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "admin-auth-scheduler" "权限中台任务调度" "200m" "" \
  "--spring.profiles.active=cloud_prod --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.config.server.ip=8.140.187.184 --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "user" "用户服务" "800m" "" \
  "--spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8087 --toucan.config.server.ip=8.140.187.184 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "common-data" "公共数据服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8104 --toucan.config.server.ip=8.140.187.184 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "content" "内容服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8105 --toucan.config.server.ip=8.140.187.184 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "admin-auth" "权限服务" "600m" "" \
  "--spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8099 --toucan.config.server.ip=8.140.187.184 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "8.140.187.184" "seller" "卖家服务" "100m" "" \
  "--spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8100 --toucan.config.server.ip=8.140.187.184 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

# =============================================
# 123.56.127.178 (服务节点)
# =============================================
echo ""
echo "=== 123.56.127.178 (服务节点) ==="

write_script "123.56.127.178" "message" "消息服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=8103 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "123.56.127.178" "user" "用户服务" "300m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=8087 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "123.56.127.178" "product" "商品服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=8082 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "123.56.127.178" "stock" "库存服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=8093 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "123.56.127.178" "order" "订单服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=8084 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "123.56.127.178" "search" "搜索服务" "200m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=8106 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "123.56.127.178" "api-monitor" "接口监控服务" "100m" "" \
  "--spring.profiles.active=prod --toucan.ip=123.56.127.178 --toucan.port=8107 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0"

write_script "123.56.127.178" "scheduler" "商城任务调度" "200m" "" \
  "--spring.profiles.active=cloud_prod --toucan.nacos.ip=8.140.187.184 --toucan.config.server.ip=8.140.187.184 --toucan.config.server.port=9090 --toucan.workerId=0 --toucan.datacenterId=0"

echo ""
echo "====== 完成 ======"
echo "8.140.187.184: $(ls "${OUTPUT_BASE}/8.140.187.184/" | wc -l) 个脚本"
echo "123.56.127.178: $(ls "${OUTPUT_BASE}/123.56.127.178/" | wc -l) 个脚本"
