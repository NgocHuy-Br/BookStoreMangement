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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @Controller
// @RequestMapping("/customer")
// public class CustomerController {

//     @Autowired
//     private CustomerService customerSettingService;

//     // @GetMapping
//     // public String showSettingForm(HttpSession session, Model model) {
//     // User user = (User) session.getAttribute("loggedInUser");
//     // Bookstore bookstore = user.getBookstore();

//     // CustomerSetting setting = customerSettingService.getByBookstore(bookstore);
//     // if (setting == null) {
//     // setting = new CustomerSetting();
//     // setting.setBookstore(bookstore);
//     // }

//     // model.addAttribute("setting", setting);
//     // return "customer/customer-list";
//     // }

//     @GetMapping("")
//     public String showCustomerSettingPage(Model model, HttpSession session) {
//         User currentUser = (User) session.getAttribute("loggedInUser");
//         Bookstore bookstore = currentUser.getBookstore();

//         // Tìm setting theo bookstore
//         CustomerSetting setting = customerSettingService.findByBookstore(bookstore);

//         if (setting == null) {
//             setting = new CustomerSetting();
//             setting.setBookstore(bookstore); // Gán để khi lưu không bị lỗi
//         }

//         model.addAttribute("customerSetting", setting);
//         model.addAttribute("customers", customerService.findByBookstore(bookstore));

//         return "customer/customer-list"; // hoặc "admin/customer" nếu bạn đặt vậy
//     }

//     @PostMapping
//     public String saveSetting(@ModelAttribute("setting") CustomerSetting setting) {
//         customerSettingService.save(setting);
//         return "redirect:/customer-list?success";
//     }
// }

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
        model.addAttribute("keyword", keyword); // ✅ truyền keyword để giữ lại ô tìm kiếm
        return "customer/customer-list";
    }

    @PostMapping("/setting/save")
    public String updateSetting(@ModelAttribute("customerSetting") CustomerSetting setting, HttpSession session) {
        customerService.updateSetting(setting);
        session.setAttribute("customerMessage", "Cập nhật cấu hình thành công.");
        return "redirect:/customer";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, HttpSession session) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null && customerService.canDeleteCustomer(customer)) {
            customerService.deleteCustomer(customer);
            session.setAttribute("customerMessage", "Xóa khách hàng thành công.");
        } else {
            session.setAttribute("customerMessage", "Không xóa được vì khách hàng đã có mua hàng trước đó.");
        }
        return "redirect:/customer";
    }
}
