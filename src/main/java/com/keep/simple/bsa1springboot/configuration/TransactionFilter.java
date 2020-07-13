package com.keep.simple.bsa1springboot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class TransactionFilter implements Filter {

    @Value("${required.header}")
    private String header;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getHeader(header) == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing Header");
        }

        chain.doFilter(req, res);
    }
}
