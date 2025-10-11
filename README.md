# Akina Dynamic Thread Pool

一个基于Spring Boot的动态线程池管理框架，支持实时监控和动态调整线程池参数，提供Web管理界面和Redis作为配置中心。

## 🚀 项目特性

- **动态调整**: 支持运行时动态调整线程池的核心线程数和最大线程数
- **实时监控**: 提供线程池状态实时监控，包括活跃线程数、队列大小等关键指标
- **配置中心**: 基于Redis实现配置的集中管理和分发
- **Web管理**: 提供RESTful API接口进行线程池管理
- **自动配置**: 基于Spring Boot自动配置，开箱即用
- **多应用支持**: 支持多应用实例的线程池统一管理

## 📁 项目结构

```
akina-dynamic-thread-pool/
├── dynamic-thread-pool-common/               # 公共模块
│   ├── entity/                               # 实体类
│   │   └── ThreadPoolConfigEntity.java      # 线程池配置实体
│   └── enums/                               # 枚举类
│       └── RegistryEnumVO.java              # 注册中心枚举
├── dynamic-thread-pool-spring-boot-starter/ # Spring Boot启动器
│   ├── config/                              # 自动配置
│   ├── domain/                              # 领域服务
│   ├── registry/                            # 注册中心实现
│   └── trigger/                             # 触发器和监听器
├── dynamic-thread-pool-admin/               # 管理后台
│   ├── Application.java                     # 启动类
│   ├── trigger/                             # 控制器
│   └── type/                                # 响应类型
├── dynamic-thread-pool-test/                # 测试模块
└── docs/                                    # 文档
    └── dev-ops/                             # 运维文档
```

## 🛠️ 技术栈

- **Java 8+**
- **Spring Boot 2.7.12**
- **Redis** (配置中心)
- **Redisson** (Redis客户端)
- **Maven** (项目管理)
- **Lombok** (代码简化)

## 📦 快速开始

### 1. 环境要求

- JDK 8+
- Maven 3.6+
- Redis 3.0+

### 2. 克隆项目

```bash
git clone <repository-url>
cd akina-dynamic-thread-pool
```

### 3. 编译项目

```bash
mvn clean install
```

### 4. 配置Redis

确保Redis服务正在运行，默认连接地址为 `localhost:6379`

### 5. 启动管理后台

```bash
cd dynamic-thread-pool-admin
mvn spring-boot:run
```

管理后台将在 `http://localhost:8090` 启动

### 6. 在应用中使用

#### 6.1 添加依赖

在你的Spring Boot项目中添加依赖：

```xml
<dependency>
    <groupId>com.akina.middleware</groupId>
    <artifactId>dynamic-thread-pool-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

#### 6.2 配置Redis连接

在 `application.yml` 中添加配置：

```yaml
spring:
  application:
    name: your-app-name

# 动态线程池配置
dynamic:
  thread:
    pool:
      redis:
        sdk:
          config:
            host: localhost
            port: 6379
            password: # Redis密码，可选
            poolSize: 64
            minIdleSize: 10
            idleTimeout: 10000
            connectTimeout: 10000
            retryAttempts: 3
            retryInterval: 1000
            pingInterval: 0
            keepAlive: true
```

#### 6.3 配置线程池

```java
@Configuration
public class ThreadPoolConfig {
    
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
            2,  // 核心线程数
            10, // 最大线程数
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100)
        );
    }
}
```

## 🔧 API接口

### 查询线程池列表

```bash
curl --request GET \
  --url 'http://localhost:8090/api/v1/dynamic/thread/pool/query_thread_pool_list'
```

### 查询特定线程池配置

```bash
curl --request GET \
  --url 'http://localhost:8090/api/v1/dynamic/thread/pool/query_thread_pool_config?appName=your-app-name&threadPoolName=threadPoolExecutor'
```

### 更新线程池配置

```bash
curl --request POST \
  --url http://localhost:8090/api/v1/dynamic/thread/pool/update_thread_pool_config \
  --header 'content-type: application/json' \
  --data '{
    "appName": "your-app-name",
    "threadPoolName": "threadPoolExecutor",
    "corePoolSize": 5,
    "maximumPoolSize": 20
  }'
```

## 📊 监控指标

框架提供以下线程池监控指标：

- **核心线程数** (corePoolSize)
- **最大线程数** (maximumPoolSize)
- **当前活跃线程数** (activeCount)
- **当前池中线程数** (poolSize)
- **队列类型** (queueType)
- **当前队列任务数** (queueSize)
- **队列剩余容量** (remainingCapacity)

## 🔄 工作原理

1. **配置上报**: 应用启动时，自动扫描所有ThreadPoolExecutor Bean，并将配置信息上报到Redis
2. **配置监听**: 应用监听Redis的配置变更消息
3. **动态调整**: 当收到配置变更消息时，实时调整本地线程池参数
4. **状态监控**: 定期上报线程池运行状态到Redis，供管理后台展示

## 🎯 使用场景

- **微服务架构**: 统一管理多个服务的线程池配置
- **性能调优**: 根据业务负载动态调整线程池参数
- **运维监控**: 实时监控线程池运行状态
- **A/B测试**: 快速验证不同线程池配置的效果

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件至项目维护者

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者们！