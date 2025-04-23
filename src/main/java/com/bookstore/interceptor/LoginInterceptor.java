package com.bookstore.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        String path = request.getRequestURI();

        if (path.startsWith("/auth") ||
                path.startsWith("/css") ||
                path.startsWith("/js") ||
                path.startsWith("/images") ||
                path.startsWith("/fonts") ||
                path.equals("/favicon.ico")) {
            return true;
        }

        // Nếu chưa login thì redirect về login và thông báo
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("/auth/login?requireLogin=true");
            return false;
        }

        return true;
    }

}
