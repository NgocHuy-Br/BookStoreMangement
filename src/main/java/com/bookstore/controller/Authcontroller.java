package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.AuthService;
import com.bookstore.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthService authService;

    // @GetMapping("/")
    // public String homeRedirect() {
    // return "redirect:/auth/login";
    // }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("user") User formUser, Model model, HttpSession session) {
        User user = userRepo.findByUsername(formUser.getUsername()).orElse(null);
        if (user != null && user.getPassword().equals(formUser.getPassword())) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register-admin";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("user") User user,
            @RequestParam String bookstoreName,
            @RequestParam String bookstoreAddress,
            Model model) {

        String msg = authService.register(user, bookstoreName, bookstoreAddress);

        if (msg.contains("thành công")) {
            model.addAttribute("message", msg);
            return "auth/login";
        } else {
            model.addAttribute("message", msg);
            return "auth/register-admin";
        }
    }
}
