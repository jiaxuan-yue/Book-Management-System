# =============================================
# GitHub Actions Secrets 配置指南
# =============================================
#
# 在 GitHub 仓库中添加 Secrets：
# Settings → Secrets and variables → Actions → New repository secret
#
# =============================================

# =============================================
# Secret 1: SERVER_HOST
# =============================================
# Name:  SERVER_HOST
# Value: （填你的服务器公网 IP，例如：123.45.67.89）

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
# Value: （把下面的私钥内容完整复制进去，包含 BEGIN 和 END 行）

-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW
QyNTUxOQAAACA550gAhMy2nDvdv8ZPnlj67//FzOLbdhRNsys5LXbwSAAAAJhXAaxLVwGs
SwAAAAtzc2gtZWQyNTUxOQAAACA550gAhMy2nDvdv8ZPnlj67//FzOLbdhRNsys5LXbwSA
AAAECjZMZjIUaC1UIujGtxS616ZyzwbQfcf2z7Yun41JTrhTnnSACEzLacO92/xk+eWPrv
/8XM4tt2FE2zKzktdvBIAAAAFWdpdGh1Yi1hY3Rpb25zLWRlcGxveQ==
-----END OPENSSH PRIVATE KEY-----

# =============================================
# Secret 5: DEPLOY_PATH
# =============================================
# Name:  DEPLOY_PATH
# Value: /home/app

# =============================================
