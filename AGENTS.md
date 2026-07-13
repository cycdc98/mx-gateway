# AGENTS.md — mx-gateway

> AI Agent 项目手册。每次修改项目结构、技术决策或发现新的约定/规则时，应同步更新此文件。

---

## 1. 项目概述

- **项目名**: `mx-gateway`
- **定位**: 基于 Spring Boot 的网关服务
- **当前阶段**: 早期开发阶段，已具备骨架代码及定时任务基础设施，尚未实现核心网关功能（路由、过滤器、限流、鉴权等）

---

## 2. 技术栈

| 项目        | 版本 / 说明                        |
| ----------- | ---------------------------------- |
| Spring Boot | 2.7.18                             |
| Java        | 1.8                                |
| 构建工具    | Maven                              |
| 打包方式    | **WAR**（部署到外部 Servlet 容器） |
| Web 框架    | Spring MVC                         |
| 测试框架    | JUnit 5 (spring-boot-starter-test) |

### Maven 依赖

| 依赖                         | scope    | 说明                                             |
| ---------------------------- | -------- | ------------------------------------------------ |
| `spring-boot-starter-web`    | compile  | Spring MVC + 嵌入式 Tomcat（已排除默认 logback） |
| `spring-boot-starter-log4j2` | compile  | Log4j2 日志框架                                  |
| `spring-boot-starter-tomcat` | provided | 外部 Tomcat 提供 Servlet API                     |
| `spring-boot-starter-test`   | test     | JUnit 5 测试支持                                 |

---

## 3. 项目结构

```
mx-gateway/
├── pom.xml                          # Maven 构建配置
├── AGENTS.md                        # 本文件（AI Agent 手册）
├── src/
│   ├── main/
│   │   ├── java/com/example/mx_gateway/
│   │   │   ├── MxGatewayApplication.java     # 启动类（含 @EnableScheduling）
│   │   │   ├── config/
│   │   │   │   ├── RemoteConfigLoader.java        # 远程配置加载器 (EnvironmentPostProcessor)
│   │   │   │   └── ServletConfig.java             # Servlet 注册配置
│   │   │   ├── schedulingtasks/
│   │   │   │   └── ScheduledTasks.java        # 定时任务组件
│   │   │   └── servlet/
│   │   │       └── GeneralServlet.java        # 通用 Servlet
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── spring.factories               # EnvironmentPostProcessor 注册
│   │       ├── application.yaml                   # 应用配置
│   │       ├── log4j2.xml                         # Log4j2 日志配置
│   │       ├── static/                            # 静态资源
│   │       └── templates/                         # 模板文件
│   └── test/
│       └── java/com/example/mx_gateway/
│           ├── MxGatewayApplicationTests.java     # 启动类测试
│           └── schedulingtasks/
│               └── ScheduledTasksTest.java        # 定时任务测试
└── target/                                    # Maven 构建输出（已 gitignore）
```

---

## 4. 包约定

| 包路径                                          | 职责                                                   |
| ----------------------------------------------- | ------------------------------------------------------ |
| `com.example.mx_gateway`                        | 根包：启动类、全局配置                                 |
| `com.example.mx_gateway.config`                 | 配置类 (`@Configuration`) + `EnvironmentPostProcessor` |
| `com.example.mx_gateway.schedulingtasks`        | 定时任务组件（`@Scheduled`）                           |
| `com.example.mx_gateway.servlet`                | Servlet 定义                                           |
| `com.example.mx_gateway.filter` _(待建)_        | 网关过滤器（鉴权、日志等）                             |
| `com.example.mx_gateway.route` _(待建)_         | 路由转发逻辑                                           |
| `com.example.mx_gateway.service` _(待建)_       | 业务服务层                                             |

> **规则**: 新增功能按职责分包放置，保持与现有结构一致。

---

## 5. 配置约定

- 配置文件使用 **YAML** 格式：`application.yaml`
- 当前仅配置了 `spring.application.name: mx-gateway`
- 多环境配置请使用 `application-{profile}.yaml` 命名
- **日志框架**: Log4j2，配置文件 `log4j2.xml`（XML 格式），日志路径为 `logs/mx-gateway/`，日志文件为 `mx-gateway.log`

---

## 6. 编码约定

- **编码**: UTF-8
- **缩进**: 4 空格（不使用 Tab）
- **类命名**: PascalCase（如 `GeneralServlet`）
- **包命名**: 全小写，使用下划线分隔（如 `mx_gateway`），与 Spring Boot Initializr 默认生成保持一致
- **注解使用**:
  - 配置类用 `@Configuration`
  - 避免循环依赖（见"已知问题"）
- **Servlet**: 继承 `HttpServlet`，**统一通过 `ServletRegistrationBean` 在 `ServletConfig` 中注册**（不使用 `@WebServlet`）
- **日志**: 统一使用 SLF4J 门面（`org.slf4j.Logger` / `LoggerFactory`），禁止直接使用 Log4j2 API。极早期组件（`EnvironmentPostProcessor` 等）除外，必须使用 `DeferredLogFactory` 获取日志实例
- **定时任务**: 使用 `@Scheduled` 注解声明定时方法，定时任务类放在 `schedulingtasks` 包下，使用 `@Component` 注册为 Spring Bean。启动类需添加 `@EnableScheduling` 启用调度功能

---

## 7. 启动与运行

```bash
# 开发环境直接运行
mvn spring-boot:run

# 打包为 WAR
mvn clean package -DskipTests

# 运行全部测试
mvn test

# 运行单个测试类
mvn test -Dtest=MxGatewayApplicationTests

# 运行单个测试方法
mvn test -Dtest=MxGatewayApplicationTests#contextLoads
```

启动类：`com.example.mx_gateway.MxGatewayApplication`，继承 `SpringBootServletInitializer`，可同时支持独立运行和部署到外部 Tomcat。

---

## 8. 已知问题 & 待办

| #   | 问题                   | 文件                      | 说明                                                                                              |
| --- | ---------------------- | ------------------------- | ------------------------------------------------------------------------------------------------- |
| 1   | **未实现业务方法**     | `GeneralServlet.java`     | 已通过 `ServletRegistrationBean` 注册到 `/api/*`，`init` 已实现，但尚无 `doGet/doPost` 等业务方法 |
| 2   | **远程配置加载待实现** | `RemoteConfigLoader.java` | `postProcessEnvironment` 仅打印日志，远程配置拉取/合并逻辑待补充                                  |

---

## 9. 启动流程与架构

项目启动按以下顺序执行，部分步骤发生在 Spring 上下文初始化**之前**：

### 阶段 1: EnvironmentPostProcessor（最早）

- `META-INF/spring.factories` 注册的 `RemoteConfigLoader` 最先执行
- 此时日志系统未初始化，必须使用 `DeferredLogFactory` 获取 `DeferredLog`，日志会在 Log4j2 就绪后自动回放
- 此阶段可修改 `ConfigurableEnvironment` 的 `propertySources`（远程配置合并点）

### 阶段 2: Spring 上下文初始化与日志就绪

- `MxGatewayApplication`（`@SpringBootApplication`）启动 Spring 容器
- 自动扫描 `com.example.mx_gateway` 包下的所有 Bean
- **Log4j2 在此阶段就绪**，此前使用 `DeferredLog` 的组件日志被回放

### 阶段 3: Servlet 注册

- `ServletConfig` 通过 `ServletRegistrationBean` 将 `GeneralServlet` 注册到 `/api/*`
- `setLoadOnStartup(1)` 确保 Servlet 在容器启动时立即初始化

### 阶段 4: 定时任务启动

- `@EnableScheduling` 启用 Spring 定时任务调度器
- 自动扫描 `schedulingtasks` 包下的 `@Scheduled` 方法并注册到调度线程池
- 当前仅 `ScheduledTasks.reportCurrentTime()` 一个定时任务（每 5 秒执行）

### 双部署模式

- **独立运行**：`main()` 使用嵌入式 Tomcat
- **外部容器部署**：继承 `SpringBootServletInitializer`，打包为 WAR 部署到外部 Tomcat（`spring-boot-starter-tomcat` 设为 `provided` scope）

---

## 10. 关键机制

### spring.factories SPI

`META-INF/spring.factories` 是 Spring Boot SPI 引导文件，用于在 ApplicationContext 创建前注册扩展组件。格式为 `接口全限定名=实现类全限定名`。当前仅注册了 `EnvironmentPostProcessor`，后续可按需添加 `ApplicationContextInitializer`、`ApplicationListener` 等。

### DeferredLog 模式

用于在日志系统初始化前记录日志的 Spring Boot 模式。组件通过构造器注入 `DeferredLogFactory`，调用 `factory.getLog(Class)` 获取 `DeferredLog` 实例。`DeferredLog` 将日志暂存内存，待日志系统就绪后一次性回放，避免早期日志丢失。**仅在 `EnvironmentPostProcessor` 等极早期组件中使用，其余代码统一使用 SLF4J。**

### Log4j2 配置要点

- `monitorInterval="60"`：每 60 秒自动检测 `log4j2.xml` 变更并热加载，无需重启
- 日志滚动策略：按天（`TimeBasedTriggeringPolicy`）+ 按大小（100MB `SizeBasedTriggeringPolicy`），最多保留 30 个历史文件
- 项目包 `com.example.mx_gateway` 级别为 **INFO**，框架包 `org.springframework` 级别为 **INFO**，Root 为 **INFO**

### Spring Scheduling 定时任务

- 启动类通过 `@EnableScheduling` 启用调度功能，Spring 会创建默认单线程调度器
- 定时任务方法使用 `@Scheduled` 注解，支持 `fixedDelay`、`fixedRate`、`cron` 等模式
- 当前使用 `@Scheduled(fixedDelay = 5000)`，表示上次执行结束后等待 5 秒再执行
- 调度线程池默认大小为 1，多个 `@Scheduled` 方法会串行执行；如需并行，需自定义 `TaskScheduler` Bean
- 定时任务统一放在 `schedulingtasks` 包下，使用 `@Component` 注册
