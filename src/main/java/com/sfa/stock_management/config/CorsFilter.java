package com.sfa.stock_management.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Value("${cors.allow.domain:*}")
    private String origin;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Allow", "OPTIONS");
        response.setHeader("Access-Control-Allow-Methods", "HEAD,GET,POST,OPTIONS,PUT,DELETE,PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
//		allowed all headers due to PNM frontend
//		response.setHeader("Access-Control-Allow-Headers", "*");

        response.setHeader("Access-Control-Allow-Headers",
                "Access-Control-Allow-Origin, Origin, Accept, X-ACCESS_TOKEN, "
                        + "Authorization, Content-Type, X-Requested-With, X-Custom-Header, "
                        + "Content-Range, Content-Disposition, Content-Description, "
                        + "Access-Control-Request-Method, Access-Control-Request-Headers, "
                        + "Pragma, Cache-Control, If-Modified-Since, isAdminUser, auth-head, "
                        + "moduleKey, app_version, device_token");

        response.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
        // "X-ACCESS_TOKEN", "Access-Control-Allow-Origin", "Authorization", "Origin",
        // "x-requested-with",
        // "Content-Type", "Content-Range", "Content-Disposition", "Content-Description"
    }

    public void init(FilterConfig filterConfig) {

    }

    public void destroy() {
    }
}