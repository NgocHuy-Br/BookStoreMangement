package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.service.InvoiceService;

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
import java.util.*;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @GetMapping("")
    public String showInvoiceForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Bookstore bookstore = user.getBookstore();
        List<Book> books = bookRepository.findByBookstore(bookstore);
        List<Customer> customers = customerRepository.findByBookstore(bookstore);

        model.addAttribute("books", books);
        model.addAttribute("customers", customers);
        return "invoice/invoice-create";
    }

    @PostMapping("/save")
    public String saveInvoice(HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        Long customerId = Long.parseLong(request.getParameter("customerId"));
        Customer customer = customerRepository.findById(customerId).orElse(null);

        String[] bookIds = request.getParameterValues("bookId");
        String[] unitPrices = request.getParameterValues("unitPrice");
        String[] quantities = request.getParameterValues("quantity");
        double vat = Double.parseDouble(request.getParameter("vat"));

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

        Invoice invoice = invoiceService.createInvoice(user, customer, bookIdList, quantityList, priceList);
        session.setAttribute("vatForInvoice_" + invoice.getId(), vat);
        redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công!");
        redirectAttributes.addFlashAttribute("lastInvoiceId", invoice.getId());

        return "redirect:/invoice";
    }

    @GetMapping("/pdf/{id}")
    public void exportInvoiceToPDF(@PathVariable Long id, HttpServletResponse response, HttpSession session) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null)
            return;

        List<InvoiceItem> items = invoiceItemRepository.findByInvoice(invoice);
        double vat = 0.0;
        Object vatObj = session.getAttribute("vatForInvoice_" + id);
        if (vatObj != null)
            vat = (double) vatObj;

        byte[] pdfBytes = invoiceService.exportInvoiceToPDF(invoice, items, vat);
        if (pdfBytes == null)
            return;

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=invoice_" + id + ".pdf");
            response.setContentLength(pdfBytes.length);
            OutputStream os = response.getOutputStream();
            os.write(pdfBytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
