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

    // @GetMapping("/delete/{id}")
    // public String deleteCategory(@PathVariable Long id,
    // @RequestParam(required = false) String returnUrl,
    // HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");

    // // Chỉ xóa nếu category thuộc bookstore của admin đang đăng nhập (bảo mật)
    // Category category = categoryService.getById(id);
    // if (category != null &&
    // category.getBookstore().getId().equals(currentUser.getBookstore().getId())) {
    // categoryService.deleteById(id);
    // }

    // // Quay lại với returnUrl nếu có
    // if (returnUrl != null && !returnUrl.isEmpty()) {
    // return "redirect:/category?returnUrl=" + returnUrl;
    // } else {
    // return "redirect:/category";
    // }
    // }
    // @GetMapping("/delete/{id}")
    // public String deleteCategory(@PathVariable Long id,
    // @RequestParam(required = false) String returnUrl,
    // HttpSession session,
    // RedirectAttributes redirectAttributes) {
    // User currentUser = (User) session.getAttribute("loggedInUser");
    // Category category = categoryService.getById(id);

    // if (category != null &&
    // category.getBookstore().getId().equals(currentUser.getBookstore().getId())) {
    // if (categoryService.isCategoryUsed(id)) {
    // redirectAttributes.addFlashAttribute("error", "Không thể xóa vì danh mục đang
    // được sử dụng.");
    // } else {
    // categoryService.deleteById(id);
    // redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công.");
    // }
    // }

    // return "redirect:/category" + (returnUrl != null ? "?returnUrl=" + returnUrl
    // : "");
    // }
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
