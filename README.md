# EasyMeeting 在线视频会议系统

## 📋 项目简介

EasyMeeting 是一个基于 Spring Boot + Netty + WebRTC 的在线视频会议系统，支持实时音视频通话、文字聊天、文件分享等功能。

## ✨ 核心功能

- ✅ **用户管理**: 注册、登录、个人信息管理、管理员功能
- ✅ **会议管理**: 快速会议、预约会议、会议邀请、成员管理
- ✅ **实时通信**: WebSocket 实时消息推送、WebRTC 点对点音视频通话
- ✅ **聊天功能**: 文本消息、图片/视频文件上传、消息历史查询
- ✅ **文件管理**: 文件上传下载、流式播放、缩略图生成
- ✅ **好友系统**: 好友申请、联系人管理

## 🛠️ 技术栈

- **后端框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0 + MyBatis
- **缓存**: Redis + Redisson
- **消息队列**: Redis Pub/Sub 或 RabbitMQ
- **WebSocket**: Netty 4.1.50
- **媒体处理**: FFmpeg
- **ID 生成**: 雪花算法

## 📦 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- FFmpeg (可选，用于媒体处理)
- RabbitMQ (可选，用于消息队列)

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/print636/easymeeting.git
cd easymeeting
```

### 2. 配置数据库

创建数据库并执行 SQL 脚本：

```sql
CREATE DATABASE easymeeting DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置文件

复制示例配置文件并修改：

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

编辑 `application.properties`，配置数据库、Redis 等信息：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/easymeeting?...
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# Redis 配置
spring.redis.host=127.0.0.1
spring.redis.port=6379

# 项目文件目录
project.folder=/path/to/your/project/folder/
```

### 4. 启动项目

```bash
mvn clean install
mvn spring-boot:run
```

项目启动后：
- Web API 端口: `6060`
- WebSocket 端口: `6061`

## 📁 项目结构

```
easymeeting/
├── src/main/java/com/easymeeting/
│   ├── controller/      # REST API 控制器
│   ├── service/         # 业务服务层
│   ├── mappers/         # MyBatis 数据访问层
│   ├── entity/          # 实体类（PO/DTO/VO/Query）
│   ├── utils/           # 工具类
│   ├── config/          # 配置类
│   ├── redis/           # Redis 组件
│   ├── websocket/       # WebSocket 相关
│   └── exception/       # 异常处理
├── src/main/resources/
│   └── com/easymeeting/mappers/  # MyBatis XML 映射文件
└── pom.xml
```

## 🔧 配置说明

### 消息中间件选择

支持两种消息中间件，通过配置切换：

```properties
# 使用 Redis
messaging.handle.channel=redis

# 或使用 RabbitMQ
messaging.handle.channel=rabbitmq
```

### 文件存储

文件按月份分目录存储：

```
project.folder/
├── file/
│   ├── 202401/          # 2024年1月的文件
│   ├── 202402/          # 2024年2月的文件
│   └── avatar/          # 用户头像
└── temp/                # 临时文件
```

## 📖 API 文档

### 用户相关

- `POST /api/account/register` - 用户注册
- `POST /api/account/login` - 用户登录
- `POST /api/account/updateUserInfo` - 更新用户信息
- `POST /api/account/updatePassword` - 修改密码

### 会议相关

- `POST /api/meeting/quickMeeting` - 快速会议
- `POST /api/meeting/joinMeeting` - 加入会议
- `POST /api/meeting/exitMeeting` - 退出会议
- `POST /api/meeting/finishMeeting` - 结束会议

### 聊天相关

- `POST /api/chat/sendMessage` - 发送消息
- `POST /api/chat/loadMessage` - 加载消息历史
- `POST /api/chat/uploadFile` - 上传文件

## 🔐 安全说明

- 项目使用 Token 认证机制
- 配置文件 `application.properties` 包含敏感信息，已添加到 `.gitignore`
- 请使用 `application.properties.example` 作为配置模板

## 📝 开发说明

### 代码规范

- 遵循 Java 编码规范
- 使用 Lombok 简化代码
- 统一异常处理机制
- 全局拦截器处理权限验证

### 数据库设计

- 聊天消息表按月分表存储
- 使用雪花算法生成唯一 ID
- 支持软删除和状态管理

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 MIT 许可证。

## 👥 作者

- **print636** - [GitHub](https://github.com/print636)

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

---

⭐ 如果这个项目对你有帮助，请给个 Star！

