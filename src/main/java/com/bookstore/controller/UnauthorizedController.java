package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UnauthorizedController {

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        model.addAttribute("error", "Bạn chưa đăng nhập. Vui lòng nhấn nút đăng nhập để vào hệ thống Quản lý nhà sách");
        return "auth/error-unauthorized";
    }
}
