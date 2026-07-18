#!/bin/bash
# =============================================
# 服务器初始化脚本（只需在服务器上运行一次）
# =============================================
# 用法：
#   1. 上传此脚本到服务器：scp server_setup.sh root@你的IP:/tmp/
#   2. SSH 登录服务器：ssh root@你的IP
#   3. 执行脚本：bash /tmp/server_setup.sh
# =============================================

set -e

DEPLOY_PATH="/home/app"
SSH_PUB_KEY="ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIDnnSACEzLacO92/xk+eWPrv/8XM4tt2FE2zKzktdvBI github-actions-deploy"

echo "======================================"
echo " 开始初始化服务器部署环境"
echo "======================================"

# 1. 安装 Java 21
echo "[1/5] 安装 Java 21..."
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
echo "[2/5] 安装 Nginx..."
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

# 3. 配置 Nginx（与 CI 部署目录 /home/app/vue/dist 保持一致）
echo "[3/5] 配置 Nginx..."
cat > /etc/nginx/sites-available/book-management << NGINX
server {
    listen 80;
    server_name _;

    # 前端静态文件（直接指向 CI 上传的构建产物，避免拷错目录）
    location / {
        root $DEPLOY_PATH/vue/dist;
        index index.html;
        try_files \$uri \$uri/ /index.html;
    }

    # 后端 API 反向代理
    location /api/ {
        proxy_pass http://localhost:9090/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
    }

    client_max_body_size 100m;
}
NGINX
ln -sfn /etc/nginx/sites-available/book-management /etc/nginx/sites-enabled/book-management
rm -f /etc/nginx/sites-enabled/default
# 兼容旧的 conf.d 配置，避免抢端口
rm -f /etc/nginx/conf.d/free_system.conf
nginx -t 2>/dev/null && systemctl reload nginx
echo "  Nginx 配置完成（root=$DEPLOY_PATH/vue/dist）"

# 4. 创建部署目录
echo "[4/5] 创建部署目录..."
mkdir -p "$DEPLOY_PATH"
echo "  目录已创建: $DEPLOY_PATH"

# 5. 配置 SSH 密钥
echo "[5/5] 配置 SSH 密钥..."
mkdir -p ~/.ssh
chmod 700 ~/.ssh
if ! grep -q "github-actions-deploy" ~/.ssh/authorized_keys 2>/dev/null; then
    echo "$SSH_PUB_KEY" >> ~/.ssh/authorized_keys
    chmod 600 ~/.ssh/authorized_keys
    echo "  SSH 公钥已添加"
else
    echo "  SSH 公钥已存在"
fi

echo "======================================"
echo " 服务器初始化完成！"
echo ""
echo " 部署目录: $DEPLOY_PATH"
echo " Java 版本: $(java -version 2>&1 | head -1)"
echo " Nginx 状态: $(systemctl is-active nginx)"
echo ""
echo " 接下来："
echo " 1. 在 GitHub 仓库配置 Secrets（见 DEPLOY_GUIDE.md）"
echo " 2. 在 MySQL 中执行 free_system.sql 创建 user 表"
echo " 3. git push 到 main 分支触发自动部署"
echo "======================================"
