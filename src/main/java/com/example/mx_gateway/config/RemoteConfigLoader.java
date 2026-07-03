package com.example.mx_gateway.config;

import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 远程配置加载器，在 Spring 上下文初始化前加载远程配置。
 * 通过 META-INF/spring.factories 自动注册。
 */
public class RemoteConfigLoader implements EnvironmentPostProcessor {

    private final Log log;

    public RemoteConfigLoader(DeferredLogFactory deferredLogFactory) {
        log = deferredLogFactory.getLog(RemoteConfigLoader.class);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("RemoteConfigLoader: 开始加载远程配置...");

        // TODO: 远程配置加载实现
        // 1. 连接远程配置中心
        // 2. 拉取配置项
        // 3. 合并到 environment.propertySources

        log.info("RemoteConfigLoader: 远程配置加载完成");
    }
}