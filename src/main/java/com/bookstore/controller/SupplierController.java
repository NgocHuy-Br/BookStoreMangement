package com.bookstore.controller;

import com.bookstore.entity.Supplier;
import com.bookstore.entity.User;
import com.bookstore.repository.SupplierRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    // Giao diện thêm nhà cung cấp
    @GetMapping("/create")
    public String showCreateForm(Model model, @RequestParam(required = false) String returnUrl) {
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("returnUrl", returnUrl);
        return "/supplier/supplier-create";
    }

    // Xử lý form tạo nhà cung cấp
    // @PostMapping("/create")
    // public String createSupplier(@ModelAttribute Supplier supplier,
    // @RequestParam(required = false) String returnUrl) {
    // supplierRepository.save(supplier);
    // return "redirect:" + (returnUrl != null ? returnUrl : "/import");
    // }
    // @PostMapping("/create")
    // public String createSupplier(@ModelAttribute Supplier supplier,
    // @RequestParam(required = false) String returnUrl,
    // HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");
    // supplier.setBookstore(currentUser.getBookstore());
    // supplierRepository.save(supplier);
    // return "redirect:" + (returnUrl != null ? returnUrl : "/import");
    // }
    @PostMapping("/create")
    public String createSupplier(@ModelAttribute Supplier supplier,
            @RequestParam(required = false) String returnUrl,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        supplier.setBookstore(currentUser.getBookstore());
        supplierRepository.save(supplier);

        // Làm sạch returnUrl nếu bị lỗi như "/import,/import"
        if (returnUrl != null && returnUrl.contains(",")) {
            returnUrl = returnUrl.split(",")[0];
        }

        return "redirect:" + (returnUrl != null ? returnUrl : "/import");
    }

}
