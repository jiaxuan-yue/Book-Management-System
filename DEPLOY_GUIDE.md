# =============================================
# GitHub Actions Secrets 配置指南
# =============================================
#
# 在 GitHub 仓库中添加 Secrets：
# Settings → Secrets and variables → Actions → New repository secret
#
# 警告：永远不要把私钥写进仓库或本文件。私钥只放在 GitHub Secrets / 本机 ~/.ssh。
#
# =============================================

# =============================================
# Secret 1: SERVER_HOST
# =============================================
# Name:  SERVER_HOST
# Value: （填你的服务器公网 IP）

# =============================================
# Secret 2: SERVER_USER
# =============================================
# Name:  SERVER_USER
# Value: root

# =============================================
# Secret 3: SERVER_PORT
# =============================================
# Name:  SERVER_PORT
# Value: 22

# =============================================
# Secret 4: SERVER_SSH_KEY
# =============================================
# Name:  SERVER_SSH_KEY
# Value: （本机私钥全文，含 BEGIN/END 行。用 gh 上传，不要粘贴到 git）
#
# 推荐命令：
#   gh secret set SERVER_SSH_KEY -R jiaxuan-yue/Book-Management-System < ~/.ssh/deploy_key_book_mgmt

# =============================================
# Secret 5: DEPLOY_PATH
# =============================================
# Name:  DEPLOY_PATH
# Value: /home/app

# =============================================
