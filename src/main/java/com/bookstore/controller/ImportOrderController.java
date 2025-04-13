package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CategoryRepository;
import com.bookstore.repository.SupplierRepository;
import com.bookstore.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/import")
public class ImportOrderController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ImportOrderService importOrderService;

    // @GetMapping("")
    // public String showImportForm(Model model, HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");

    // List<Book> books =
    // bookService.getBooksByBookstore(currentUser.getBookstore());
    // List<Supplier> suppliers = supplierService.getAllSuppliers();

    // model.addAttribute("books", books);
    // model.addAttribute("suppliers", suppliers);

    // return "import/import-create";
    // }
    // @GetMapping("")
    // public String showImportForm(Model model,
    // @RequestParam(defaultValue = "5") int rowCount,
    // HttpSession session) {
    // User user = (User) session.getAttribute("loggedInUser");
    // Bookstore bookstore = user.getBookstore();

    // model.addAttribute("books", bookService.getBooksByBookstore(bookstore));
    // model.addAttribute("suppliers", supplierService.getAllSuppliers());
    // model.addAttribute("rowCount", rowCount);
    // return "import/import-create";
    // }

    @GetMapping
    public String showImportForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null)
            return "redirect:/auth/login";

        List<Book> books = bookRepository.findByBookstore(user.getBookstore());
        // List<Supplier> suppliers = supplierRepository.findAll();
        List<Category> categories = categoryRepository.findByBookstore(user.getBookstore());

        model.addAttribute("books", books);
        // model.addAttribute("suppliers", suppliers);
        User currentUser = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = currentUser.getBookstore();
        List<Supplier> suppliers = supplierRepository.findByBookstore(bookstore);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("categories", categories);

        return "import/import-create";
    }

    @PostMapping
    public String submitImport(
            @RequestParam("bookId") List<Long> bookIds,
            @RequestParam("supplierId") List<Long> supplierIds,
            @RequestParam("quantity") List<Integer> quantities,
            @RequestParam("unitPrice") List<Double> prices,
            HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null)
            return "redirect:/auth/login";

        importOrderService.createMultipleSupplierOrder(user, bookIds, supplierIds, quantities, prices);
        return "redirect:/import";
    }

    // @PostMapping("/create")
    // public String handleImportSubmit(@RequestParam Long supplierId,
    // @RequestParam List<Long> bookIds,
    // @RequestParam List<Integer> quantities,
    // @RequestParam List<Double> prices,
    // HttpSession session) {

    // User currentUser = (User) session.getAttribute("loggedInUser");

    // importOrderService.createImportOrder(currentUser, supplierId, bookIds,
    // quantities, prices);

    // return "redirect:/import/create?success=true";
    // }
    // @PostMapping("/create")
    // public String createImportOrder(@ModelAttribute ImportOrder importOrder,
    // HttpSession session) {
    // User currentUser = (User) session.getAttribute("loggedInUser");

    // importOrder.setCreatedAt(LocalDateTime.now());
    // importOrder.setCreatedBy(currentUser);
    // importOrder.setBookstore(currentUser.getBookstore());

    // importOrderService.save(importOrder);
    // return "redirect:/import-order";
    // }

    @PostMapping("/save")
    public String saveImportOrder(HttpServletRequest request, HttpSession session,
            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        Long supplierId = Long.parseLong(request.getParameter("supplierId"));
        String[] bookIds = request.getParameterValues("bookId");
        String[] unitPrices = request.getParameterValues("unitPrice");
        String[] quantities = request.getParameterValues("quantity");

        List<Long> bookIdList = new ArrayList<>();
        List<Double> priceList = new ArrayList<>();
        List<Integer> quantityList = new ArrayList<>();

        for (int i = 0; i < bookIds.length; i++) {
            if (!bookIds[i].isEmpty()) {
                bookIdList.add(Long.parseLong(bookIds[i]));
                priceList.add(Double.parseDouble(unitPrices[i]));
                quantityList.add(Integer.parseInt(quantities[i]));
            }
        }

        importOrderService.createImportOrder(currentUser, supplierId, bookIdList, quantityList, priceList);

        // ✅ Gửi thông báo thành công
        redirectAttributes.addFlashAttribute("success", "Nhập đơn hàng thành công!");

        return "redirect:/import";
    }

}
