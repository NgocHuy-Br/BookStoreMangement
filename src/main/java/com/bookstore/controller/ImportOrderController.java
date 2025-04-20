package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.service.ImportOrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/import")
public class ImportOrderController {

    @Autowired
    private ImportOrderRepository importOrderRepository;
    @Autowired
    private ImportOrderItemRepository importItemRepo;
    @Autowired
    private SupplierRepository supplierRepo;
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private ImportOrderService importOrderService;

    @GetMapping
    public String showImportOrders(
            Model model,
            HttpSession session,
            @RequestParam(name = "supplier", required = false) String supplierName,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        User user = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = user.getBookstore();

        List<ImportOrder> orders = importOrderService.filterOrders(bookstore, supplierName, from, to);
        double total = importOrderService.calculateTotalWithVAT(orders);

        model.addAttribute("orders", orders);
        model.addAttribute("totalValue", total);
        model.addAttribute("supplier", supplierName);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "import/import-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        List<Book> books = bookRepo.findByBookstore(user.getBookstore());
        List<Supplier> suppliers = supplierRepo.findByBookstore(user.getBookstore());
        model.addAttribute("books", books);
        model.addAttribute("suppliers", suppliers);
        return "import/import-create";
    }

    @PostMapping("/save")
    public String saveImportOrder(HttpServletRequest request, HttpSession session, Model model) {
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
                priceList, vat);

        // Lấy lại dữ liệu cần hiển thị trong form
        List<Book> books = bookRepo.findByBookstore(currentUser.getBookstore());
        List<Supplier> suppliers = supplierRepo.findByBookstore(currentUser.getBookstore());

        model.addAttribute("success", "Nhập đơn hàng thành công!");
        model.addAttribute("lastImportId", order.getId()); // để hiển thị nút xuất PDF
        model.addAttribute("books", books);
        model.addAttribute("suppliers", suppliers);

        return "import/import-create"; // quay lại đúng giao diện form đã điền
    }

    @GetMapping("/pdf/{id}")
    public void exportImportPdf(@PathVariable Long id, HttpSession session, HttpServletResponse response) {
        ImportOrder order = importOrderRepository.findById(id).orElse(null);
        if (order == null)
            return;

        List<ImportOrderItem> items = importItemRepo.findByImportOrder(order);

        // double vat = order.getVatRate();
        double vat = order.getVatRate() != null ? order.getVatRate() : 0.0;// Lấy trực tiếp từ entity
        
        byte[] pdfBytes = importOrderService.exportImportOrderToPDF(order, items, vat);

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=import_order_" + id + ".pdf");
            OutputStream os = response.getOutputStream();
            os.write(pdfBytes);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
