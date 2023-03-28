package com.dev.gware.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(1)
public class CustomCorsFilter implements Filter {

    private final static String LOCAL_HOST = "http://localhost:8080";
    private final static String AWS_S3_HOST = "http://front-end-spa.s3-website.ap-northeast-2.amazonaws.com";
    private final static String SERVICE_DOMAIN = "http://www.typefi.io";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String origin = request.getHeader("Origin");
        String method = request.getMethod();

        log.info("[CustomCorsFilter doFilter] Domain: {}, Method: {}", origin, method);

        if (validOrigin(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods","*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        }

        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean validOrigin(String origin) {
        return SERVICE_DOMAIN.equals(origin) || LOCAL_HOST.equals(origin) || AWS_S3_HOST.equals(origin);
    }
}
