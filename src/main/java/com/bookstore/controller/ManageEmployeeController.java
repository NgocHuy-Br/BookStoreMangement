package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/employee")
public class ManageEmployeeController {

    @Autowired
    private UserService userService;

    // Hiển thị danh sách nhân viên, với chức năng tìm kiếm theo username
    @GetMapping("")
    public String showEmployeeList(Model model,
            @RequestParam(required = false) String keyword,
            HttpSession session) {
        // Kiểm tra session: chỉ cho phép admin
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            return "redirect:/auth/login";
        }

        List<User> employees;
        if (keyword != null && !keyword.isEmpty()) {
            employees = userService.searchEmployeesByUsernameAndBookstore(keyword, currentUser.getBookstore());
        } else {
            employees = userService.getEmployeesByBookstore(currentUser.getBookstore());
        }

        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        return "employee/employee-list";
    }

    // Hiển thị form chỉnh sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User employee = userService.getUserById(id);
        model.addAttribute("employee", employee);
        return "employee/employee-edit";
    }

    @PostMapping("/edit")
    public String updateEmployee(@ModelAttribute("employee") User employee,
            Model model,
            RedirectAttributes redirectAttributes) {
        User existingUser = userService.getUserById(employee.getId());

        if (userService.isUsernameTakenByAnotherUser(employee.getUsername(), employee.getId())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại !");
            model.addAttribute("employee", employee);
            return "employee/employee-edit";
        }

        existingUser.setUsername(employee.getUsername());
        existingUser.setPassword(employee.getPassword());
        userService.saveEmployee(existingUser);

        redirectAttributes.addFlashAttribute("message", "Cập nhật nhân viên thành công !");
        return "redirect:/admin/employee";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            return "redirect:/auth/login";
        }

        User employee = userService.getUserById(id);

        if (userService.hasRelatedData(employee)) {
            redirectAttributes.addFlashAttribute("message", "Không thể xóa nhân viên vì đã có đơn hàng liên quan !");
        } else {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "Xóa nhân viên thành công !");
        }

        return "redirect:/admin/employee";
    }

    @GetMapping("/create")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new User());
        return "employee/employee-create";
    }

    @PostMapping("/create")
    public String addEmployee(@ModelAttribute("employee") User employee,
            HttpSession session,
            Model model) {
        User admin = (User) session.getAttribute("loggedInUser");

        if (userService.isUsernameExists(employee.getUsername())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại !");
            model.addAttribute("employee", employee);
            return "employee/employee-create";
        }

        employee.setRole("EMPLOYEE");
        employee.setBookstore(admin.getBookstore());
        userService.saveEmployee(employee);

        // Truyền lại employee rỗng và thông báo
        model.addAttribute("employee", new User());
        model.addAttribute("success", "Tạo tài khoản nhân viên thành công !");
        return "employee/employee-create";
    }
}
