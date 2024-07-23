package com.biit.abcd.core.rest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().equalsIgnoreCase("OPTIONS")) {
                if (response instanceof HttpServletResponse) {
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
                }
            }
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpResponse.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        httpResponse.addHeader("Access-Control-Allow-Headers", "X-Requested-With, X-Auth-Token");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
