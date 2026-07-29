#!/bin/bash
# ==========================================
# 犀鸟电商 - 权限服务 重启脚本
# 服务器: 8.140.187.184
# ==========================================

SERVICE="toucan-shopping-admin-auth"
DEPLOY_DIR="/usr/toucan_shopping"
LOG_DIR="/usr/toucan_shopping/logs"
mkdir -p ${LOG_DIR}

echo "===== 重启 ${SERVICE} (权限服务) ====="

# ===== 1. 停服 =====
# 用JAR包名精确匹配，避免 admin 误匹配 admin-auth / admin-auth-web 等
OLD_PID=$(ps aux | grep "toucan-shopping-admin-auth-[0-9]" | grep -v grep | awk '{print $2}')
if [ -n "${OLD_PID}" ]; then
    echo "[1/3] 停止旧进程 PID: ${OLD_PID}"
    kill ${OLD_PID} 2>/dev/null || true
    for i in $(seq 1 10); do
        sleep 2
        STATE=$(ps -o state= -p ${OLD_PID} 2>/dev/null || echo "")
        if [ -z "${STATE}" ] || [ "${STATE}" = "Z" ]; then
            echo "      已停止"
            break
        fi
        if [ $i -ge 3 ]; then
            echo "      强杀..."
            kill -9 ${OLD_PID} 2>/dev/null || true
        fi
        if [ $i -eq 10 ]; then
            echo "      停止失败! 状态: ${STATE}, 请手动 kill -9 ${OLD_PID}"
            exit 1
        fi
    done
else
    echo "[1/3] 服务未运行，跳过停服"
fi

cd ${DEPLOY_DIR}

# ===== 2. 启动 =====
echo "[2/3] 启动..."
nohup java -Xmx600m  -XX:+UseG1GC -XX:SoftRefLRUPolicyMSPerMB=0 -Djava.awt.headless=true -Dfile.encoding=UTF-8 -XX:ParallelGCThreads=4 -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=80 -XX:MaxTenuringThreshold=15 -XX:+HeapDumpOnOutOfMemoryError -Xlog:gc*:file=./gc.log:time,level,tags:filecount=5,filesize=10M \
    -jar toucan-shopping-admin-auth-1.0-SNAPSHOT.jar \
    --spring.profiles.active=prod --toucan.ip=8.140.187.184 --toucan.port=8099 --toucan.config.server.ip=8.140.187.184 --toucan.nacos.ip=8.140.187.184 --toucan.nacos.username=nacos --toucan.nacos.password=nacos --toucan.workerId=0 --toucan.datacenterId=0 \
    > ${LOG_DIR}/admin-auth.log 2>&1 &

NEW_PID=$!
echo "      新进程 PID: ${NEW_PID}"

# ===== 3. 检查 =====
sleep 3
if ps -p ${NEW_PID} > /dev/null 2>&1; then
    echo "[3/3] 启动成功! PID: ${NEW_PID}"
else
    echo "[3/3] 启动失败! 查看日志: tail -20 ${LOG_DIR}/admin-auth.log"
    tail -20 "${LOG_DIR}/admin-auth.log" 2>/dev/null || echo "(日志为空)"
    exit 1
fi
echo ""
