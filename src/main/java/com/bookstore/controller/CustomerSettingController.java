package com.bookstore.controller;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.CustomerSetting;
import com.bookstore.entity.User;
import com.bookstore.service.CustomerSettingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerSettingController {

    @Autowired
    private CustomerSettingService customerSettingService;

    @GetMapping
    public String showSettingForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = user.getBookstore();

        CustomerSetting setting = customerSettingService.getByBookstore(bookstore);
        if (setting == null) {
            setting = new CustomerSetting();
            setting.setBookstore(bookstore);
        }

        model.addAttribute("setting", setting);
        return "customer/customer-list";
    }

    @PostMapping
    public String saveSetting(@ModelAttribute("setting") CustomerSetting setting) {
        customerSettingService.save(setting);
        return "redirect:/customer-list?success";
    }
}
