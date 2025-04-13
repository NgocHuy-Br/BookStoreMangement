package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CategoryRepository;
import com.bookstore.repository.ImportOrderRepository;
import com.bookstore.repository.ImportOrderItemRepository;
import com.bookstore.repository.SupplierRepository;
import com.bookstore.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
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
    private ImportOrderRepository importOrderRepository;

    @Autowired
    private ImportOrderItemRepository importOrderItemRepository;

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
        String vatStr = request.getParameter("vat");

        double vat = 0.0;
        if (vatStr != null && !vatStr.trim().isEmpty()) {
            vat = Double.parseDouble(vatStr);
        }

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

        ImportOrder order = importOrderService.createImportOrder(currentUser, supplierId, bookIdList, quantityList,
                priceList);

        // Lưu VAT vào session
        session.setAttribute("vatForOrder_" + order.getId(), vat);
        redirectAttributes.addFlashAttribute("success", "Nhập đơn hàng thành công!");
        redirectAttributes.addFlashAttribute("lastImportId", order.getId());

        return "redirect:/import";
    }

    @GetMapping("/pdf/{id}")
    public void exportImportOrderToPDF(@PathVariable Long id, HttpServletResponse response, HttpSession session) {
        ImportOrder order = importOrderRepository.findById(id).orElse(null);
        if (order == null)
            return;

        List<ImportOrderItem> items = importOrderItemRepository.findByImportOrder(order);
        double vat = 0.0;

        Object vatObj = session.getAttribute("vatForOrder_" + id);
        if (vatObj != null)
            vat = (double) vatObj;

        byte[] pdfBytes = importOrderService.exportImportOrderToPDF(order, items, vat);
        if (pdfBytes == null)
            return;

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=import_order_" + id + ".pdf");
            response.setContentLength(pdfBytes.length);
            OutputStream os = response.getOutputStream();
            os.write(pdfBytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
