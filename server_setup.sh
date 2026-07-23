#!/bin/bash
# =============================================
# 服务器初始化脚本（只需在服务器上运行一次）
# =============================================
# 用法：
#   1. 上传此脚本到服务器：scp server_setup.sh root@你的IP:/tmp/
#   2. 登录服务器执行：bash /tmp/server_setup.sh
# =============================================

set -e

DEPLOY_PATH="/home/app"

echo "======================================"
echo " 开始初始化服务器部署环境"
echo "======================================"

# 1. 安装 Java 21
echo "[1/4] 安装 Java 21..."
if command -v java &> /dev/null; then
    JAVA_VER=$(java -version 2>&1 | head -1)
    echo "  Java 已安装: $JAVA_VER"
else
    if command -v apt-get &> /dev/null; then
        apt-get update -qq
        apt-get install -y openjdk-21-jdk
    elif command -v yum &> /dev/null; then
        yum install -y java-21-openjdk
    fi
    echo "  Java 21 安装完成"
fi

# 2. 安装 Nginx
echo "[2/4] 安装 Nginx..."
if command -v nginx &> /dev/null; then
    echo "  Nginx 已安装"
else
    if command -v apt-get &> /dev/null; then
        apt-get install -y nginx
    elif command -v yum &> /dev/null; then
        yum install -y nginx
    fi
    systemctl enable nginx
    systemctl start nginx
    echo "  Nginx 安装完成并已启动"
fi

# 3. 配置 Nginx（HTTP + HTTPS，API 走 TLS，避免明文传输）
echo "[3/4] 配置 Nginx..."
mkdir -p /etc/nginx/ssl
if [ ! -f /etc/nginx/ssl/book.crt ]; then
  openssl req -x509 -nodes -days 3650 -newkey rsa:2048 \
    -keyout /etc/nginx/ssl/book.key \
    -out /etc/nginx/ssl/book.crt \
    -subj "/CN=localhost" >/dev/null 2>&1
  echo "  已生成自签证书 /etc/nginx/ssl/book.crt"
fi
cat > /etc/nginx/sites-available/book-management << NGINX
# HTTP → HTTPS
server {
    listen 80;
    server_name _;
    return 301 https://\$host\$request_uri;
}

server {
    listen 443 ssl;
    server_name _;
    ssl_certificate     /etc/nginx/ssl/book.crt;
    ssl_certificate_key /etc/nginx/ssl/book.key;

    location / {
        root $DEPLOY_PATH/vue/dist;
        index index.html;
        try_files \$uri \$uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:9090/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    client_max_body_size 100m;
}
NGINX
ln -sfn /etc/nginx/sites-available/book-management /etc/nginx/sites-enabled/book-management
rm -f /etc/nginx/sites-enabled/default
rm -f /etc/nginx/conf.d/free_system.conf
nginx -t 2>/dev/null && systemctl reload nginx
echo "  Nginx 配置完成（HTTPS root=$DEPLOY_PATH/vue/dist）"

# 4. 创建部署目录
echo "[4/4] 创建部署目录..."
mkdir -p "$DEPLOY_PATH"
echo "  目录已创建: $DEPLOY_PATH"

echo "======================================"
echo " 服务器初始化完成！"
echo ""
echo " 部署目录: $DEPLOY_PATH"
echo " Java 版本: $(java -version 2>&1 | head -1)"
echo " Nginx 状态: $(systemctl is-active nginx)"
echo ""
echo " 接下来："
echo " 1. 在 MySQL 中执行 free_system.sql"
echo " 2. 按 springboot/src/main/resources/application.yml 配置数据库后启动"
echo " 3. git push 到 main 分支触发自动部署"
echo "======================================"
