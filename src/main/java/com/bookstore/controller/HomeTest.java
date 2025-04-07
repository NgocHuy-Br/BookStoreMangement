package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeTest {

    @GetMapping("/")
    public String home() {
        return "hello"; // maps to /WEB-INF/views/hello.jsp
    }

}
