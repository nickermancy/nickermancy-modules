package com.nickermancy.spring.web.servlet;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpServletSupport {

    public static Optional<HttpServletRequest> getRequest() {
        return getRequestAttributes().map(ServletRequestAttributes::getRequest);
    }

    public static Optional<HttpServletResponse> getResponse() {
        return getRequestAttributes().map(ServletRequestAttributes::getResponse);
    }

    public static Optional<ServletRequestAttributes> getRequestAttributes() {
        final var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return Optional.of((ServletRequestAttributes) requestAttributes);
        }
        return Optional.empty();
    }
}
