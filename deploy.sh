#!/bin/bash
# ===========================================
# 服务器部署脚本 - 由 GitHub Actions 自动调用
# ===========================================

set -e

# === 配置区域（根据你的服务器实际情况修改） ===
DEPLOY_PATH="$(cd "$(dirname "$0")" && pwd)"
JAR_NAME="springboot-0.0.1-SNAPSHOT.jar"
JAR_PATH="$DEPLOY_PATH/springboot/target/$JAR_NAME"
FRONTEND_PATH="$DEPLOY_PATH/vue/dist"
NGINX_HTML_PATH="/usr/share/nginx/html"  # Nginx 静态文件目录，按实际修改
BACKEND_PORT=9090
LOG_FILE="$DEPLOY_PATH/app.log"

echo "=============================="
echo " 开始部署 $(date '+%Y-%m-%d %H:%M:%S')"
echo "=============================="

# === 1. 部署后端 ===
echo "[1/4] 停止旧的后端服务..."
OLD_PID=$(ps aux | grep "$JAR_NAME" | grep -v grep | awk '{print $2}' || true)
if [ -n "$OLD_PID" ]; then
    kill $OLD_PID
    echo "  已停止旧进程 PID=$OLD_PID"
    sleep 2
else
    echo "  没有找到运行中的后端进程"
fi

echo "[2/4] 启动新的后端服务..."
if [ -f "$JAR_PATH" ]; then
    nohup java -jar "$JAR_PATH" \
        --server.port=$BACKEND_PORT \
        > "$LOG_FILE" 2>&1 &
    echo "  后端已启动，日志: $LOG_FILE"
else
    echo "  [错误] 找不到 JAR 文件: $JAR_PATH"
    exit 1
fi

# === 2. 部署前端 ===
echo "[3/4] 部署前端静态文件..."
if [ -d "$FRONTEND_PATH" ]; then
    # 如果 Nginx 已安装，复制到 Nginx 目录
    if command -v nginx &> /dev/null; then
        sudo rm -rf "$NGINX_HTML_PATH"/*
        sudo cp -r "$FRONTEND_PATH"/* "$NGINX_HTML_PATH"/
        sudo nginx -s reload 2>/dev/null || sudo systemctl reload nginx
        echo "  前端文件已部署到 Nginx: $NGINX_HTML_PATH"
    else
        echo "  [提示] 未检测到 Nginx，前端文件保留在: $FRONTEND_PATH"
        echo "  请手动配置 Web 服务器指向该目录"
    fi
else
    echo "  [警告] 前端构建目录不存在: $FRONTEND_PATH"
fi

# === 3. 健康检查 ===
echo "[4/4] 后端健康检查..."
sleep 5
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:$BACKEND_PORT/" 2>/dev/null || echo "000")
if [ "$HTTP_CODE" = "200" ]; then
    echo "  后端服务正常运行 (HTTP $HTTP_CODE)"
else
    echo "  [警告] 后端返回 HTTP $HTTP_CODE，请检查日志: $LOG_FILE"
fi

echo "=============================="
echo " 部署完成 $(date '+%Y-%m-%d %H:%M:%S')"
echo "=============================="
