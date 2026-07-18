-- =============================================
-- 演示数据种子脚本（可重复执行）
-- 用于服务器初始化 / CI 部署后灌入样例数据
-- =============================================

CREATE DATABASE IF NOT EXISTS `free_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `free_system`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ---------- 表结构（IF NOT EXISTS，不破坏已有库） ----------
CREATE TABLE IF NOT EXISTS `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `account` double(10,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `img` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `publishing` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `price` double(10,2) DEFAULT NULL,
  `num` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `borrow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `content` longtext,
  `views` int DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `collect` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `rel_id` int DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `rel_id` int DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `fid` int DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) DEFAULT NULL,
  `rel_id` int DEFAULT NULL,
  `num` int DEFAULT NULL,
  `price` double(10,2) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `chapter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `book_content` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `chapter_id` int DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------- 兼容旧库缺列（失败可忽略，由 deploy.sh 兜底） ----------
-- account / price / num 由 deploy.sh 中的 ALTER 语句处理

-- ---------- 清空业务演示数据（保留表结构） ----------
TRUNCATE TABLE `book_content`;
TRUNCATE TABLE `chapter`;
TRUNCATE TABLE `orders`;
TRUNCATE TABLE `comments`;
TRUNCATE TABLE `collect`;
TRUNCATE TABLE `borrow`;
TRUNCATE TABLE `article`;
TRUNCATE TABLE `book`;

-- ---------- 账号 ----------
INSERT INTO `admin` (`id`, `username`, `password`, `name`, `avatar`, `role`)
VALUES (1, 'admin', 'admin', '管理员', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', 'ADMIN')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `password`=VALUES(`password`), `avatar`=VALUES(`avatar`), `role`=VALUES(`role`);

INSERT INTO `user` (`id`, `username`, `password`, `name`, `avatar`, `role`, `account`) VALUES
(1, 'user', '123456', '张三', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 'USER', 888.00),
(2, 'lisi', '123456', '李四', 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png', 'USER', 520.50),
(3, 'wangwu', '123456', '王五', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', 'USER', 100.00)
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `password`=VALUES(`password`), `avatar`=VALUES(`avatar`),
  `role`=VALUES(`role`), `account`=VALUES(`account`);

-- ---------- 图书 ----------
INSERT INTO `book` (`id`, `img`, `name`, `publishing`, `author`, `price`, `num`) VALUES
(1, 'https://picsum.photos/seed/java/300/420', 'Java 核心技术', '机械工业出版社', 'Cay S. Horstmann', 99.00, 50),
(2, 'https://picsum.photos/seed/spring/300/420', 'Spring Boot 实战', '人民邮电出版社', '汪云飞', 79.00, 35),
(3, 'https://picsum.photos/seed/vue/300/420', 'Vue.js 设计与实现', '人民邮电出版社', '霍春阳', 89.00, 40),
(4, 'https://picsum.photos/seed/mysql/300/420', '高性能 MySQL', '电子工业出版社', 'Baron Schwartz', 128.00, 20),
(5, 'https://picsum.photos/seed/algo/300/420', '算法导论', '机械工业出版社', 'Thomas H. Cormen', 128.00, 15),
(6, 'https://picsum.photos/seed/clean/300/420', '代码整洁之道', '人民邮电出版社', 'Robert C. Martin', 69.00, 60);

-- ---------- 章节（在线阅读） ----------
INSERT INTO `chapter` (`id`, `name`, `book_id`) VALUES
(1, '第一章 初识 Java', 1),
(2, '第二章 面向对象', 1),
(3, '第一章 Spring Boot 入门', 2),
(4, '第二章 Web 开发', 2),
(5, '第一章 Vue 响应式原理', 3),
(6, '第二章 组件化设计', 3);

-- ---------- 章节内容（每章多页，便于翻页演示） ----------
INSERT INTO `book_content` (`id`, `content`, `chapter_id`, `book_id`) VALUES
(1, '<h2>欢迎学习 Java</h2><p>Java 是一门面向对象的编程语言，具有跨平台、安全、稳定等特点。</p><p>本章将带你快速建立对 Java 的整体认知。</p>', 1, 1),
(2, '<h2>安装与环境</h2><p>请先安装 JDK 21，并配置好 JAVA_HOME 环境变量。</p><p>然后使用 <code>java -version</code> 验证安装是否成功。</p>', 1, 1),
(3, '<h2>第一个程序</h2><pre>public class Hello {\n  public static void main(String[] args) {\n    System.out.println(\"Hello Java\");\n  }\n}</pre>', 1, 1),
(4, '<h2>类与对象</h2><p>类是对象的模板，对象是类的实例。封装、继承、多态是 OOP 三大特性。</p>', 2, 1),
(5, '<h2>继承与多态</h2><p>子类可以继承父类的属性和方法，并通过方法重写实现多态。</p>', 2, 1),
(6, '<h2>为什么选择 Spring Boot</h2><p>Spring Boot 约定优于配置，能快速搭建生产级应用。</p>', 3, 2),
(7, '<h2>创建项目</h2><p>使用 Spring Initializr 选择 Web、MyBatis、MySQL 依赖即可开始。</p>', 3, 2),
(8, '<h2>Controller 层</h2><p>使用 @RestController 暴露 HTTP 接口，统一返回 Result。</p>', 4, 2),
(9, '<h2>响应式数据</h2><p>Vue3 使用 Proxy 实现响应式，ref 与 reactive 是常用 API。</p>', 5, 3),
(10, '<h2>组件通信</h2><p>父子组件通过 props / emit 通信，跨层可用 provide/inject。</p>', 6, 3);

-- ---------- 帖子 ----------
INSERT INTO `article` (`id`, `title`, `img`, `description`, `time`, `user_id`, `content`, `views`) VALUES
(1, '我的毕业设计进度分享', 'https://picsum.photos/seed/a1/800/400', '图书管理系统已经完成收藏、评论、下单和在线阅读', '2026-07-10 10:00:00', 1,
 '<p>本周完成了收藏、评论、模拟支付和下单、章节在线阅读功能。</p><p>欢迎大家体验并提出建议！</p>', 128),
(2, 'Spring Boot + Vue 前后端分离心得', 'https://picsum.photos/seed/a2/800/400', '记录一下开发中的踩坑与收获', '2026-07-12 14:30:00', 2,
 '<p>接口统一用 Result 封装，前端 axios 拦截器处理错误提示，开发效率提升很多。</p>', 86),
(3, '如何设计图书章节与翻页阅读', 'https://picsum.photos/seed/a3/800/400', '一章多页：pageSize=1 就能做出翻页感', '2026-07-15 09:20:00', 1,
 '<p>章节表关联图书，内容表关联章节。阅读页每次只取 1 条内容，配合分页组件即可。</p>', 64);

-- ---------- 收藏 ----------
INSERT INTO `collect` (`id`, `user_id`, `rel_id`, `time`) VALUES
(1, 1, 1, '2026-07-16 11:00:00'),
(2, 1, 3, '2026-07-16 11:05:00'),
(3, 2, 1, '2026-07-17 08:30:00');

-- ---------- 评论（含回复） ----------
INSERT INTO `comments` (`id`, `user_id`, `rel_id`, `content`, `time`, `fid`) VALUES
(1, 2, 1, '写得很清晰，收藏了！', '2026-07-16 12:00:00', 0),
(2, 3, 1, '在线阅读那个翻页思路不错。', '2026-07-16 12:10:00', 0),
(3, 1, 1, '谢谢支持，后面还会继续优化。', '2026-07-16 12:20:00', 1),
(4, 1, 2, '前后端分离确实香。', '2026-07-17 09:00:00', 0),
(5, 2, 3, '准备按这个思路改我的阅读模块。', '2026-07-17 15:00:00', 0);

-- ---------- 借阅 ----------
INSERT INTO `borrow` (`id`, `user_id`, `book_id`, `time`, `status`) VALUES
(1, 1, 1, '2026-07-14 10:00:00', '借阅中'),
(2, 2, 3, '2026-07-15 16:00:00', '借阅中'),
(3, 1, 2, '2026-07-10 09:00:00', '已归还');

-- ---------- 订单 ----------
INSERT INTO `orders` (`id`, `order_no`, `rel_id`, `num`, `price`, `time`, `status`, `phone`, `address`, `user_id`) VALUES
(1, '20260718090001', 3, 1, 89.00, '2026-07-18 09:00:01', '待发货', '13800001111', '杭州市西湖区文三路 100 号', 1),
(2, '20260718103002', 1, 2, 198.00, '2026-07-18 10:30:02', '已发货', '13900002222', '杭州市余杭区仓前街道 88 号', 2),
(3, '20260717180003', 6, 1, 69.00, '2026-07-17 18:00:03', '已完成', '13800001111', '杭州市西湖区文三路 100 号', 1);

SET FOREIGN_KEY_CHECKS = 1;
