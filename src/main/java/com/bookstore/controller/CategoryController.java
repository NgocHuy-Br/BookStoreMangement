package com.bookstore.controller;

import com.bookstore.entity.Category;
import com.bookstore.entity.User;
import com.bookstore.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/create")
    public String showCreateCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "/category/category-create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") Category category, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null || currentUser.getBookstore() == null) {
            return "redirect:/auth/login";
        }

        category.setBookstore(currentUser.getBookstore());
        categoryService.saveCategory(category);

        // Sau khi tạo xong danh mục, quay lại trang thêm sách
        return "redirect:/book/create";
    }
}
