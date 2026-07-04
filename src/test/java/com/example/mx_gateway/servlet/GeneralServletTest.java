package com.example.mx_gateway.servlet;

import org.junit.jupiter.api.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeneralServletTest {

    @Test
    void initShouldSucceed() {
        GeneralServlet servlet = new GeneralServlet();
        ServletConfig config = mock(ServletConfig.class);
        when(config.getServletName()).thenReturn("generalServlet");

        assertDoesNotThrow(() -> servlet.init(config),
                "init() should complete without exception");
    }

    @Test
    void initShouldNotThrowOnNullServletName() {
        GeneralServlet servlet = new GeneralServlet();
        ServletConfig config = mock(ServletConfig.class);
        when(config.getServletName()).thenReturn(null);

        assertDoesNotThrow(() -> servlet.init(config),
                "init() should handle null servlet name gracefully");
    }
}
