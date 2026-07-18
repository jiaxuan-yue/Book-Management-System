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
BACKEND_PORT=9090
DB_PASSWORD="root123456"
DB_NAME="free_system"
DB_USER="root"
LOG_FILE="$DEPLOY_PATH/app.log"
# 旧版 Nginx 站点配置（项目重构前路径）；脚本会优先把它切到 FRONTEND_PATH
BOOK_NGINX_CONF="/etc/nginx/sites-available/book-management"
MOCK_SQL="$DEPLOY_PATH/mock_data.sql"

echo "=============================="
echo " 开始部署 $(date '+%Y-%m-%d %H:%M:%S')"
echo " 部署目录: $DEPLOY_PATH"
echo "=============================="

# === 解析 Nginx 当前生效的 root ===
detect_nginx_root() {
    local conf root
    if [ -f "$BOOK_NGINX_CONF" ]; then
        root=$(grep -E '^\s*root\s+' "$BOOK_NGINX_CONF" | head -1 | awk '{print $2}' | tr -d ';')
        if [ -n "$root" ]; then
            echo "$root"
            return
        fi
    fi
    for conf in /etc/nginx/sites-enabled/* /etc/nginx/conf.d/*.conf; do
        [ -f "$conf" ] || continue
        case "$conf" in
            *default*) continue ;;
        esac
        root=$(grep -E '^\s*root\s+' "$conf" | head -1 | awk '{print $2}' | tr -d ';')
        if [ -n "$root" ]; then
            echo "$root"
            return
        fi
    done
    echo ""
}

mysql_exec() {
    mysql -u"$DB_USER" -p"$DB_PASSWORD" "$@"
}

# === 0. 数据库结构兼容 + 演示数据 ===
echo "[1/6] 导入数据库演示数据..."
if ! command -v mysql &> /dev/null; then
    echo "  [错误] 未找到 mysql 客户端"
    exit 1
fi
mysql_exec -e "CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
# 旧库缺列时补齐（已存在则忽略报错）
mysql_exec "$DB_NAME" -e "ALTER TABLE \`user\` ADD COLUMN \`account\` double(10,2) DEFAULT 0.00 COMMENT '钱包余额';" 2>/dev/null || true
mysql_exec "$DB_NAME" -e "ALTER TABLE \`book\` ADD COLUMN \`price\` double(10,2) DEFAULT NULL COMMENT '价格';" 2>/dev/null || true
mysql_exec "$DB_NAME" -e "ALTER TABLE \`book\` ADD COLUMN \`num\` int DEFAULT NULL COMMENT '库存';" 2>/dev/null || true

if [ -f "$MOCK_SQL" ]; then
    mysql_exec "$DB_NAME" < "$MOCK_SQL"
    echo "  演示数据导入完成: $MOCK_SQL"
else
    echo "  [警告] 未找到 $MOCK_SQL，跳过灌数"
fi

# === 1. 部署后端 ===
echo "[2/6] 停止旧的后端服务..."
OLD_PIDS=$(ps aux | grep "$JAR_NAME" | grep -v grep | awk '{print $2}' || true)
if [ -n "$OLD_PIDS" ]; then
    echo "$OLD_PIDS" | xargs -r kill
    echo "  已停止旧进程: $OLD_PIDS"
    sleep 3
else
    echo "  没有找到运行中的后端进程"
fi

echo "[3/6] 启动新的后端服务..."
if [ ! -f "$JAR_PATH" ]; then
    echo "  [错误] 找不到 JAR 文件: $JAR_PATH"
    exit 1
fi
nohup java -jar "$JAR_PATH" \
    --server.port=$BACKEND_PORT \
    --spring.datasource.password=$DB_PASSWORD \
    > "$LOG_FILE" 2>&1 &
echo "  后端已启动，日志: $LOG_FILE"

# === 2. 部署前端 ===
echo "[4/6] 部署前端静态文件..."
if [ ! -d "$FRONTEND_PATH" ]; then
    echo "  [错误] 前端构建目录不存在: $FRONTEND_PATH"
    exit 1
fi

NEW_INDEX_JS=$(grep -oE '/assets/index-[a-f0-9]+\.js' "$FRONTEND_PATH/index.html" | head -1 | xargs basename)
echo "  本次构建入口: $NEW_INDEX_JS"

if ! command -v nginx &> /dev/null; then
    echo "  [错误] 未检测到 Nginx"
    exit 1
fi

# 优先：把 book-management 站点 root 切到 CI 部署目录，避免再拷到错误路径
if [ -f "$BOOK_NGINX_CONF" ]; then
    echo "  检测到 $BOOK_NGINX_CONF，将 root 指向: $FRONTEND_PATH"
    sudo sed -i "s|^\([[:space:]]*root[[:space:]]\+\).*|\1$FRONTEND_PATH;|" "$BOOK_NGINX_CONF"
    # 确保站点已启用
    if [ -d /etc/nginx/sites-enabled ] && [ ! -e /etc/nginx/sites-enabled/book-management ]; then
        sudo ln -sf "$BOOK_NGINX_CONF" /etc/nginx/sites-enabled/book-management
    fi
    NGINX_HTML_PATH="$FRONTEND_PATH"
else
    NGINX_HTML_PATH="$(detect_nginx_root)"
    if [ -z "$NGINX_HTML_PATH" ]; then
        NGINX_HTML_PATH="/usr/share/nginx/html"
        echo "  [警告] 未解析到站点 root，回退到: $NGINX_HTML_PATH"
    else
        echo "  解析到 Nginx root: $NGINX_HTML_PATH"
    fi

    # root 不是部署目录时，需要拷贝
    if [ "$(realpath "$NGINX_HTML_PATH" 2>/dev/null || echo "$NGINX_HTML_PATH")" != "$(realpath "$FRONTEND_PATH")" ]; then
        echo "  拷贝前端到 Nginx root..."
        sudo mkdir -p "$NGINX_HTML_PATH"
        sudo rm -rf "${NGINX_HTML_PATH:?}/"*
        sudo cp -r "$FRONTEND_PATH"/* "$NGINX_HTML_PATH"/
    fi
fi

sudo nginx -t
sudo nginx -s reload 2>/dev/null || sudo systemctl reload nginx
echo "  Nginx 已重载，对外目录: $NGINX_HTML_PATH"

# === 3. 校验前端是否真的更新 ===
echo "[5/6] 校验前端部署结果..."
SERVED_INDEX="$NGINX_HTML_PATH/index.html"
if [ ! -f "$SERVED_INDEX" ]; then
    echo "  [错误] 找不到对外 index.html: $SERVED_INDEX"
    exit 1
fi
SERVED_JS=$(grep -oE '/assets/index-[a-f0-9]+\.js' "$SERVED_INDEX" | head -1 | xargs basename)
if [ "$SERVED_JS" != "$NEW_INDEX_JS" ]; then
    echo "  [错误] 前端未更新成功！构建=$NEW_INDEX_JS，对外=$SERVED_JS"
    exit 1
fi
if [ ! -f "$NGINX_HTML_PATH/assets/$NEW_INDEX_JS" ]; then
    echo "  [错误] 对外目录缺少入口 JS: $NGINX_HTML_PATH/assets/$NEW_INDEX_JS"
    exit 1
fi
echo "  前端校验通过: $SERVED_JS"

# === 4. 后端健康检查（失败则让 CI 变红） ===
echo "[6/6] 后端健康检查..."
HTTP_CODE="000"
for i in 1 2 3 4 5 6 7 8 9 10; do
    sleep 3
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "http://127.0.0.1:$BACKEND_PORT/" 2>/dev/null || echo "000")
    echo "  第 ${i} 次检查: HTTP $HTTP_CODE"
    if [ "$HTTP_CODE" = "200" ]; then
        break
    fi
done

if [ "$HTTP_CODE" != "200" ]; then
    echo "  [错误] 后端未能在预期时间内就绪 (HTTP $HTTP_CODE)"
    echo "  ---- app.log 最后 80 行 ----"
    tail -80 "$LOG_FILE" || true
    exit 1
fi
echo "  后端服务正常运行 (HTTP $HTTP_CODE)"

BOOK_COUNT=$(mysql_exec -Nse "SELECT COUNT(*) FROM \`$DB_NAME\`.book" 2>/dev/null || echo "?")
echo "  当前图书数量: $BOOK_COUNT"

echo "=============================="
echo " 部署完成 $(date '+%Y-%m-%d %H:%M:%S')"
echo "=============================="
