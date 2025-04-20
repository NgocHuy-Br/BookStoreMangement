package com.bookstore.controller;

import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Customer;
import com.bookstore.entity.CustomerSetting;
import com.bookstore.entity.User;
import com.bookstore.service.CustomerService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public String showCustomerPage(@RequestParam(required = false) String keyword,
            Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = currentUser.getBookstore();

        CustomerSetting setting = customerService.getOrCreateSetting(bookstore);
        List<Customer> customers = (keyword != null && !keyword.isBlank())
                ? customerService.searchByName(bookstore, keyword)
                : customerService.getCustomersByBookstore(bookstore);

        model.addAttribute("customerSetting", setting);
        model.addAttribute("customers", customers);
        model.addAttribute("keyword", keyword); // truyền keyword để giữ lại ô tìm kiếm
        return "customer/customer-list";
    }

    @GetMapping("/create")
    public String showCreateCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/customer-create";
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute Customer customer, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        customer.setBookstore(currentUser.getBookstore());
        customerService.save(customer);
        session.setAttribute("customerMessage", "Thêm khách hàng thành công !");
        return "redirect:/customer";
    }

    @PostMapping("/setting/save")
    public String updateSetting(@ModelAttribute("customerSetting") CustomerSetting setting,
            Model model,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = currentUser.getBookstore();

        // Tìm lại cấu hình hiện có để lấy ID cũ
        CustomerSetting existingSetting = customerService.getOrCreateSetting(bookstore);
        setting.setId(existingSetting.getId());
        setting.setBookstore(bookstore); // phải gán lại vì form không gửi bookstore

        // Lưu
        customerService.updateSetting(setting);

        model.addAttribute("message", "Cập nhật cài đặt thành công !");
        List<Customer> customers = customerService.getCustomersByBookstore(bookstore);

        model.addAttribute("customerSetting", setting);
        model.addAttribute("customers", customers);
        model.addAttribute("keyword", "");

        return "customer/customer-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, HttpSession session) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null && customerService.canDeleteCustomer(customer)) {
            customerService.deleteCustomer(customer);
            session.setAttribute("customerMessage", "Xóa khách hàng thành công !");
        } else {
            session.setAttribute("customerMessage", "Không xóa được vì khách hàng đã có mua hàng trước đó !");
        }
        return "redirect:/customer";
    }

    @GetMapping("/update/{id}")
    public String showEditCustomerForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/customer";
        }
        model.addAttribute("customer", customer);
        return "customer/customer-edit";
    }

    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customer") Customer customer, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        customer.setBookstore(currentUser.getBookstore()); // bảo toàn liên kết với bookstore
        customerService.save(customer);
        session.setAttribute("customerMessage", "Cập nhật khách hàng thành công !");
        return "redirect:/customer";
    }

}
