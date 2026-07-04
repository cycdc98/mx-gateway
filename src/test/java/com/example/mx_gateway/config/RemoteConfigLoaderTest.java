package com.example.mx_gateway.config;

import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RemoteConfigLoaderTest {

    @Test
    void postProcessEnvironmentShouldSucceed() {
        DeferredLogFactory logFactory = mock(DeferredLogFactory.class);
        Log log = mock(Log.class);
        when(logFactory.getLog(RemoteConfigLoader.class)).thenReturn(log);
        when(log.isInfoEnabled()).thenReturn(true);

        RemoteConfigLoader loader = new RemoteConfigLoader(logFactory);
        ConfigurableEnvironment environment = new MockEnvironment();
        SpringApplication application = mock(SpringApplication.class);

        assertDoesNotThrow(() -> loader.postProcessEnvironment(environment, application),
                "postProcessEnvironment should complete without exception");
    }

    @Test
    void postProcessEnvironmentShouldHandleMinimalInput() {
        DeferredLogFactory logFactory = mock(DeferredLogFactory.class);
        Log log = mock(Log.class);
        when(logFactory.getLog(RemoteConfigLoader.class)).thenReturn(log);
        when(log.isInfoEnabled()).thenReturn(true);

        RemoteConfigLoader loader = new RemoteConfigLoader(logFactory);
        ConfigurableEnvironment environment = new MockEnvironment();
        SpringApplication application = null;

        assertDoesNotThrow(() -> loader.postProcessEnvironment(environment, application),
                "postProcessEnvironment should handle null application");
    }
}
