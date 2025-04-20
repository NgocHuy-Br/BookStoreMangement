package com.bookstore.controller;

import com.bookstore.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        Object userObj = session.getAttribute("loggedInUser");
        if (userObj == null) {
            return "redirect:/auth/login";
        }

        User user = (User) userObj;
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "common/dashboard";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/employee/dashboard")
    public String employeeDashboard(HttpSession session) {
        Object userObj = session.getAttribute("loggedInUser");
        if (userObj == null) {
            return "redirect:/auth/login";
        }

        User user = (User) userObj;
        if ("EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            return "common/dashboard";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
