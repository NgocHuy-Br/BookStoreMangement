package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
            employees = userService.searchEmployeesByUsername(keyword);
        } else {
            // employees = userService.getAllEmployees();
            employees = userService.getEmployeesByBookstore(currentUser.getBookstore());
        }
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        return "admin/employee-list";
    }

    // // Hiển thị form để thêm nhân viên mới
    // @GetMapping("/add")
    // public String showCreateForm(Model model, HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");
    // if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole()))
    // {
    // return "redirect:/auth/login";
    // }
    // model.addAttribute("employee", new User());
    // return "admin/employee-create";
    // }

    // // Xử lý tạo nhân viên mới (role mặc định là EMPLOYEE)
    // @PostMapping("/add")
    // public String createEmployee(@ModelAttribute("employee") User employee) {
    // employee.setRole("EMPLOYEE");
    // userService.save(employee);
    // return "redirect:/admin/employee";
    // }

    // Hiển thị form chỉnh sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User employee = userService.getUserById(id);
        model.addAttribute("employee", employee);
        return "admin/employee-edit";
    }

    // Xử lý cập nhật thông tin nhân viên
    @PostMapping("/edit")
    public String updateEmployee(@ModelAttribute("employee") User employee) {
        // Không update role và bookstore
        User existingUser = userService.getUserById(employee.getId());
        existingUser.setUsername(employee.getUsername());
        existingUser.setPassword(employee.getPassword());

        userService.saveEmployee(existingUser);
        return "redirect:/admin/employee";
    }

    // Xác nhận và thực hiện xóa nhân viên
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            return "redirect:/auth/login";
        }
        userService.deleteUser(id);
        return "redirect:/admin/employee";
    }

    @GetMapping("/create")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new User());
        return "admin/employee-create";
    }

    // @PostMapping("/create")
    // public String addEmployee(@ModelAttribute("employee") User employee,
    // HttpSession session) {
    // User admin = (User) session.getAttribute("loggedInUser");
    // employee.setRole("EMPLOYEE");
    // employee.setBookstore(admin.getBookstore());

    // userService.saveEmployee(employee);
    // return "redirect:/admin/employee";
    // }
    @PostMapping("/create")
    public String addEmployee(@ModelAttribute("employee") User employee,
            HttpSession session, Model model) {
        User admin = (User) session.getAttribute("loggedInUser");

        if (userService.isUsernameExists(employee.getUsername())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            model.addAttribute("employee", employee);
            return "admin/employee-create";
        }

        employee.setRole("EMPLOYEE");
        employee.setBookstore(admin.getBookstore());
        userService.saveEmployee(employee);
        return "redirect:/admin/employee";
    }

}
