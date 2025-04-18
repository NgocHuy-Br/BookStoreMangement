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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String listCategories(Model model, HttpSession session,
            @RequestParam(required = false) String returnUrl) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Category category = new Category();
        category.setBookstore(user.getBookstore());

        List<Category> categories = categoryService.getCategoriesByBookstore(user.getBookstore());
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("returnUrl", returnUrl);

        return "category/category-list"; // hoặc category.jsp tùy bạn đặt tên
    }

    // POST: Xử lý thêm mới ngay trên cùng trang
    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") Category category,
            @RequestParam(required = false) String returnUrl,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        category.setBookstore(currentUser.getBookstore());

        categoryService.saveCategory(category);

        //Redirect lại /category với returnUrl được giữ nguyên
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

    
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
            @RequestParam(required = false) String returnUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        Category category = categoryService.getById(id);

        if (category != null && category.getBookstore().getId().equals(currentUser.getBookstore().getId())) {
            if (categoryService.isCategoryUsed(id)) {
                redirectAttributes.addFlashAttribute("error", "Có sách nằm trong danh mục này, không cho phép xóa!");
            } else {
                categoryService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công.");
            }
        }

        return "redirect:/category" + (returnUrl != null ? "?returnUrl=" + returnUrl : "");
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
            @RequestParam(required = false) String returnUrl,
            Model model,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        Category category = categoryService.getById(id);

        if (category == null || !category.getBookstore().getId().equals(currentUser.getBookstore().getId())) {
            return "redirect:/category";
        }

        model.addAttribute("category", category);
        model.addAttribute("returnUrl", returnUrl);
        return "category/category-edit";
    }

    @PostMapping("/edit")
    public String updateCategory(@ModelAttribute("category") Category category,
            @RequestParam(required = false) String returnUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        category.setBookstore(currentUser.getBookstore());

        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");

        return "redirect:/category" + (returnUrl != null ? "?returnUrl=" + returnUrl : "");
    }

}
