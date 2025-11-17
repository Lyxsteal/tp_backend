package com.example.ms_solicitud.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class UserHeaderInterceptor implements HandlerInterceptor {

    public static final String USER_ATTR = "AUTH_USER";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String id = request.getHeader("X-User-Id");
        String username = request.getHeader("X-Username");
        String roles = request.getHeader("X-Roles");

        AuthenticatedUser user = new AuthenticatedUser(
                id,
                username,
                roles != null ? Arrays.asList(roles.split(",")) : null
        );

        request.setAttribute(USER_ATTR, user);
        return true;
    }
}
