package com.bookstore.controller;

import com.bookstore.entity.Supplier;
import com.bookstore.entity.User;
import com.bookstore.repository.SupplierRepository;
import com.bookstore.service.ImportOrderService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    private final ImportOrderService importOrderService;

    @Autowired
    private SupplierRepository supplierRepository;

    SupplierController(ImportOrderService importOrderService) {
        this.importOrderService = importOrderService;
    }

    // Giao diện thêm nhà cung cấp
    @GetMapping("/create")
    public String showCreateForm(Model model, @RequestParam(required = false) String returnUrl) {
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("returnUrl", returnUrl);
        return "/supplier/supplier-create";
    }

    @PostMapping("/create")
    public String createSupplier(@ModelAttribute Supplier supplier,
            @RequestParam(required = false) String returnUrl,
            HttpSession session, RedirectAttributes redirectAttributes,
            Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        supplier.setBookstore(currentUser.getBookstore());

        // Kiểm tra trùng tên trong cùng Bookstore
        boolean isDuplicate = supplierRepository.existsByNameIgnoreCaseAndBookstore(supplier.getName(),
                currentUser.getBookstore());

        if (isDuplicate) {
            model.addAttribute("error", "Tên nhà cung cấp đã tồn tại !");
            model.addAttribute("returnUrl", returnUrl);
            return "/supplier/supplier-create";
        }

        supplierRepository.save(supplier);

        // Làm sạch returnUrl ở bị lỗi như "/import,/import"
        if (returnUrl != null && returnUrl.contains(",")) {
            returnUrl = returnUrl.split(",")[0];
        }

        // Thêm thông báo thành công vào redirect
        redirectAttributes.addFlashAttribute("success", "Thêm nhà cung cấp thành công !");

        return "redirect:" + (returnUrl != null ? returnUrl : "/import/create") + "/create";
    }
}
