# AGENTS.md — mx-gateway

> AI Agent 项目手册。每次修改项目结构、技术决策或发现新的约定/规则时，应同步更新此文件。

---

## 1. 项目概述

- **项目名**: `mx-gateway`
- **定位**: 基于 Spring Boot 的网关服务
- **当前阶段**: 极早期开发阶段，仅有骨架代码，尚未实现任何网关功能（路由、过滤器、限流、鉴权等）

---

## 2. 技术栈

| 项目         | 版本 / 说明              |
| ------------ | ------------------------ |
| Spring Boot  | 2.7.18                   |
| Java         | 1.8                      |
| 构建工具     | Maven                    |
| 打包方式     | **WAR**（部署到外部 Servlet 容器） |
| Web 框架     | Spring MVC               |
| 测试框架     | JUnit 5 (spring-boot-starter-test) |

### Maven 依赖

| 依赖                            | scope      | 说明                  |
| ------------------------------- | ---------- | --------------------- |
| `spring-boot-starter-web`       | compile    | Spring MVC + 嵌入式 Tomcat（已排除默认 logback） |
| `spring-boot-starter-log4j2`    | compile    | Log4j2 日志框架 |
| `spring-boot-starter-tomcat`    | provided   | 外部 Tomcat 提供 Servlet API |
| `spring-boot-starter-test`      | test       | JUnit 5 测试支持      |

---

## 3. 项目结构

```
mx-gateway/
├── pom.xml                          # Maven 构建配置
├── AGENTS.md                        # 本文件（AI Agent 手册）
├── src/
│   ├── main/
│   │   ├── java/com/example/mx_gateway/
│   │   │   ├── MxGatewayApplication.java     # 启动类
│   │   │   ├── config/
│   │   │   │   ├── RemoteConfigLoader.java        # 远程配置加载器 (EnvironmentPostProcessor)
│   │   │   │   └── ServletConfig.java             # Servlet 注册配置
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
│           └── MxGatewayApplicationTests.java # 测试类
└── target/                                    # Maven 构建输出（已 gitignore）
```

---

## 4. 包约定

| 包路径                                        | 职责                         |
| --------------------------------------------- | ---------------------------- |
| `com.example.mx_gateway`                      | 根包：启动类、全局配置       |
| `com.example.mx_gateway.config`               | 配置类 (`@Configuration`) + `EnvironmentPostProcessor` |
| `com.example.mx_gateway.servlet`              | Servlet 定义                 |
| `com.example.mx_gateway.filter` *(待建)*      | 网关过滤器（鉴权、日志等）   |
| `com.example.mx_gateway.route`  *(待建)*      | 路由转发逻辑                 |
| `com.example.mx_gateway.service` *(待建)*     | 业务服务层                   |

> **规则**: 新增功能按职责分包放置，保持与现有结构一致。

---

## 5. 配置约定

- 配置文件使用 **YAML** 格式：`application.yaml`
- 当前仅配置了 `spring.application.name: mx-gateway`
- 多环境配置请使用 `application-{profile}.yaml` 命名
- **日志框架**: Log4j2，配置文件 `log4j2.xml`（XML 格式），通过 `${spring:spring.application.name}` 动态解析日志路径

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

---

## 7. 启动与运行

```bash
# 开发环境直接运行
mvn spring-boot:run

# 打包为 WAR
mvn clean package -DskipTests

# 运行测试
mvn test
```

启动类：`com.example.mx_gateway.MxGatewayApplication`，继承 `SpringBootServletInitializer`，可同时支持独立运行和部署到外部 Tomcat。

---

## 8. 已知问题 & 待办

| # | 问题 | 文件 | 说明 |
|---|------|------|------|
| 1 | **未实现业务方法** | `GeneralServlet.java` | 已通过 `ServletRegistrationBean` 注册到 `/api/*`，`init` 已实现，但尚无 `doGet/doPost` 等业务方法 |
| 2 | **远程配置加载待实现** | `RemoteConfigLoader.java` | `postProcessEnvironment` 仅打印日志，远程配置拉取/合并逻辑待补充 |

---

## 9. 变更记录

| 日期       | 变更说明                                     |
| ---------- | -------------------------------------------- |
| 2026-07-03 | 初始化 AGENTS.md，记录项目骨架状态和已知问题  |
| 2026-07-04 | `ServletConfig` 通过 `ServletRegistrationBean` 注册 `GeneralServlet`，映射路径 `/api/*`，启动加载顺序 1 |
| 2026-07-04 | 日志框架切换为 Log4j2（排除默认 logback），配置文件 `log4j2.xml`，项目包级别 WARN，框架级别 INFO |
| 2026-07-04 | 新增 `RemoteConfigLoader`（`EnvironmentPostProcessor`），使用 `DeferredLog`，通过 `spring.factories` 自动注册 |
