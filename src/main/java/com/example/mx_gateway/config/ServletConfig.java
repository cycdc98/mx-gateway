package com.example.mx_gateway.config;

import com.example.mx_gateway.servlet.GeneralServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<GeneralServlet> generalServletRegistration() {
        ServletRegistrationBean<GeneralServlet> registration =
                new ServletRegistrationBean<>(new GeneralServlet(), "/api/*");
        registration.setName("generalServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
