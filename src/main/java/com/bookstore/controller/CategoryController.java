package com.bookstore.controller;

import com.bookstore.entity.Category;
import com.bookstore.entity.User;
import com.bookstore.service.CategoryService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // GET: Hiển thị danh sách + form thêm
    @GetMapping("")
    public String listCategories(Model model,
            @RequestParam(required = false) String returnUrl,
            HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        List<Category> categories = categoryService.getCategoriesByBookstore(user.getBookstore());

        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());

        if (returnUrl != null && !returnUrl.isEmpty()) {
            model.addAttribute("returnUrl", returnUrl); // Quan trọng
        }

        return "category/category-list";
    }

    // POST: Xử lý thêm mới ngay trên cùng trang
    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") Category category,
            @RequestParam(required = false) String returnUrl,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        category.setBookstore(currentUser.getBookstore());

        categoryService.saveCategory(category);

        // ✅ Redirect lại /category với returnUrl được giữ nguyên
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:/category?returnUrl=" + returnUrl;
        } else {
            return "redirect:/category";
        }
    }

    @GetMapping("/create")
    public String showCreateCategoryForm(Model model, @RequestParam(required = false) String returnUrl) {
        model.addAttribute("category", new Category());
        model.addAttribute("returnUrl", returnUrl); // truyền vào view
        return "category/category-create";
    }

    // @PostMapping("/create")
    // public String createCategory(@ModelAttribute("category") Category category,
    // @RequestParam(required = false) String returnUrl,
    // HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");
    // category.setBookstore(currentUser.getBookstore());

    // categoryService.saveCategory(category);

    // // In ra log để debug nếu cần
    // System.out.println("👉 Redirecting to: " + returnUrl);

    // return (returnUrl != null && !returnUrl.trim().isEmpty())
    // ? "redirect:" + returnUrl.trim()
    // : "redirect:/book/create";
    // }

}
