package com.example.mx_gateway.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class GeneralServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(GeneralServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("GeneralServlet initialized, servletName={}", config.getServletName());
    }
}
