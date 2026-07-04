package com.example.mx_gateway.config;

import com.example.mx_gateway.servlet.GeneralServlet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ServletConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void generalServletRegistrationShouldBeConfigured() {
        ServletRegistrationBean<?> registration =
                applicationContext.getBean("generalServletRegistration", ServletRegistrationBean.class);

        assertNotNull(registration, "ServletRegistrationBean should not be null");
        assertEquals("generalServlet", registration.getServletName(),
                "Servlet name should be 'generalServlet'");

        Collection<String> urlMappings = registration.getUrlMappings();
        assertNotNull(urlMappings, "URL mappings should not be null");
        assertTrue(urlMappings.contains("/api/*"),
                "URL mappings should contain '/api/*'");
    }

    @Test
    void generalServletShouldBeOfCorrectType() {
        ServletRegistrationBean<?> registration =
                applicationContext.getBean("generalServletRegistration", ServletRegistrationBean.class);

        assertNotNull(registration.getServlet(),
                "Servlet should not be null");
        assertTrue(registration.getServlet() instanceof GeneralServlet,
                "Servlet should be instance of GeneralServlet");
    }
}
