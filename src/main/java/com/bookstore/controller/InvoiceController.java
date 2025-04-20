package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.service.InvoiceService;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private InvoiceRepository invoiceRepo;
    @Autowired
    private InvoiceItemRepository invoiceItemRepo;
    @Autowired
    private CustomerSettingRepository settingRepo;
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("")
    public String showInvoiceList(
            Model model,
            HttpSession session,
            @RequestParam(name = "customer", required = false) String customerName,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        User user = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = user.getBookstore();

        List<Invoice> invoices = invoiceService.filterInvoices(bookstore, customerName, from, to);
        double totalValue = invoiceService.calculateTotal(invoices);
        // Tính tổng lợi nhuận từ các hóa đơn đang hiển thị
        double totalProfit = invoices.stream()
                .mapToDouble(Invoice::getProfit)
                .sum();

        // Tính totalAfterVAT của từng invoice và bỏ vào Map
        Map<Long, Double> invoiceTotals = new HashMap<>();
        for (Invoice invoice : invoices) {
            double total = invoiceService.calculateTotalForInvoice(invoice);
            invoiceTotals.put(invoice.getId(), total);
        }

        model.addAttribute("invoices", invoices);
        model.addAttribute("invoiceTotals", invoiceTotals);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("totalProfit", totalProfit);
        model.addAttribute("customer", customerName);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "invoice/invoice-list";
    }

    @GetMapping("/create")
    public String showInvoiceForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = user.getBookstore();

        List<Customer> customers = customerRepo.findByBookstore(bookstore);
        CustomerSetting setting = settingRepo.findByBookstore(bookstore).orElse(null);
        int requiredPoints = (setting != null) ? setting.getRequiredPointsForMembership() : 0;
        double discountRate = (setting != null) ? setting.getDiscountRate() : 0;

        model.addAttribute("books", bookRepo.findByBookstore(bookstore));
        model.addAttribute("categories", categoryRepo.findByBookstore(bookstore));
        model.addAttribute("customers", customers);
        model.addAttribute("discountRate", discountRate);
        model.addAttribute("requiredPoints", requiredPoints);
        return "invoice/invoice-create";
    }

    @PostMapping("/save")
    public String saveInvoice(HttpServletRequest request, HttpSession session,
            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        Long customerId = Long.parseLong(request.getParameter("customerId"));

        String[] bookIds = request.getParameterValues("bookId");
        String[] unitPrices = request.getParameterValues("unitPrice");
        String[] quantities = request.getParameterValues("quantity");
        double vat = Double.parseDouble(request.getParameter("vat"));

        List<Long> bookIdList = new ArrayList<>();
        List<Double> priceList = new ArrayList<>();
        List<Integer> quantityList = new ArrayList<>();

        for (int i = 0; i < bookIds.length; i++) {
            bookIdList.add(Long.parseLong(bookIds[i]));
            priceList.add(Double.parseDouble(unitPrices[i]));
            quantityList.add(Integer.parseInt(quantities[i]));
        }

        try {
            Invoice invoice = invoiceService.createInvoice(currentUser, customerId, bookIdList, quantityList, priceList,
                    vat);
            session.setAttribute("vat_invoice_" + invoice.getId(), vat);
            redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công!");
            redirectAttributes.addFlashAttribute("lastInvoiceId", invoice.getId());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/invoice/create";
    }

    @GetMapping("/pdf/{id}")
    public void exportInvoiceToPDF(@PathVariable Long id, HttpSession session, HttpServletResponse response) {
        Invoice invoice = invoiceRepo.findById(id).orElse(null);
        if (invoice == null)
            return;

        List<InvoiceItem> items = invoiceItemRepo.findByInvoice(invoice);
        double vat = invoice.getVatRate();

        byte[] pdfBytes = invoiceService.exportInvoiceToPDF(invoice, items, vat);
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=invoice_" + id + ".pdf");
            OutputStream os = response.getOutputStream();
            os.write(pdfBytes);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
