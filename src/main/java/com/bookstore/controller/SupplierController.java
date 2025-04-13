package com.bookstore.controller;

import com.bookstore.entity.Supplier;
import com.bookstore.entity.User;
import com.bookstore.repository.SupplierRepository;
import com.bookstore.service.ImportOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

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
    // @PostMapping("/create")
    // public String createSupplier(@ModelAttribute Supplier supplier,
    // @RequestParam(required = false) String returnUrl,
    // HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");
    // supplier.setBookstore(currentUser.getBookstore());
    // supplierRepository.save(supplier);

    // // Làm sạch returnUrl nếu bị lỗi như "/import,/import"
    // if (returnUrl != null && returnUrl.contains(",")) {
    // returnUrl = returnUrl.split(",")[0];
    // }

    // return "redirect:" + (returnUrl != null ? returnUrl : "/import");
    // }
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
            model.addAttribute("error", "Tên nhà cung cấp đã tồn tại!");
            model.addAttribute("returnUrl", returnUrl);
            return "/supplier/supplier-create";
        }

        supplierRepository.save(supplier);

        // Làm sạch returnUrl nếu bị lỗi như "/import,/import"
        if (returnUrl != null && returnUrl.contains(",")) {
            returnUrl = returnUrl.split(",")[0];
        }

        // ✅ Thêm thông báo thành công vào redirect
        redirectAttributes.addFlashAttribute("success", "Thêm nhà cung cấp thành công!");

        return "redirect:" + (returnUrl != null ? returnUrl : "/import");
    }

    // @PostMapping("/save")
    // public String saveImportOrder(HttpServletRequest request, HttpSession
    // session, Model model) {
    // User currentUser = (User) session.getAttribute("loggedInUser");

    // String[] bookIds = request.getParameterValues("bookId");
    // String[] supplierIds = request.getParameterValues("supplierId");
    // String[] quantities = request.getParameterValues("quantity");
    // String[] prices = request.getParameterValues("price");

    // // Convert về List
    // List<Long> bookIdList = new ArrayList<>();
    // List<Long> supplierIdList = new ArrayList<>();
    // List<Integer> quantityList = new ArrayList<>();
    // List<Double> priceList = new ArrayList<>();

    // for (int i = 0; i < bookIds.length; i++) {
    // if (bookIds[i] == null || bookIds[i].isEmpty())
    // continue;

    // bookIdList.add(Long.parseLong(bookIds[i]));
    // supplierIdList.add(Long.parseLong(supplierIds[i]));
    // quantityList.add(Integer.parseInt(quantities[i]));
    // priceList.add(Double.parseDouble(prices[i]));
    // }

    // // Gọi service để lưu
    // importOrderService.createImportOrder(currentUser, supplierIdList, bookIdList,
    // quantityList, priceList);

    // model.addAttribute("success", "Nhập hàng thành công!");

    // // Quay lại chính trang /import
    // return "redirect:/import";
    // }

}
