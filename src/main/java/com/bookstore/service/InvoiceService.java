package com.bookstore.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.util.InvoicePdfUtil;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepo;
    @Autowired
    private InvoiceItemRepository invoiceItemRepo;
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private CustomerSettingRepository settingRepo;

    public Invoice createInvoice(User user, Long customerId,
            List<Long> bookIds, List<Integer> quantities,
            List<Double> prices, double vat) {

        Invoice invoice = new Invoice();
        invoice.setBookstore(user.getBookstore());
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUser(user);
        Customer customer = customerRepo.findById(customerId).orElse(null);
        invoice.setCustomer(customer);
        invoice.setVatRate(vat);

        // Kiểm tra tồn kho trước
        for (int i = 0; i < bookIds.size(); i++) {
            Book book = bookRepo.findById(bookIds.get(i)).orElse(null);
            if (book == null || book.getInventory() == null || book.getInventory() < quantities.get(i)) {
                throw new IllegalArgumentException("Số lượng bán vượt quá tồn kho cho sách: "
                        + (book != null ? book.getTitle() : "Không xác định"));
            }
        }

        invoiceRepo.save(invoice);

        double total = 0;
        for (int i = 0; i < bookIds.size(); i++) {
            Book book = bookRepo.findById(bookIds.get(i)).orElse(null);
            InvoiceItem item = new InvoiceItem();
            item.setBook(book);
            item.setInvoice(invoice);
            item.setQuantity(quantities.get(i));
            item.setUnitPrice(prices.get(i));
            invoiceItemRepo.save(item);

            total += prices.get(i) * quantities.get(i);

            // Trừ tồn kho
            book.setInventory(book.getInventory() - quantities.get(i));
            bookRepo.save(book);
        }

        // Áp dụng giảm giá
        Bookstore bookstore = user.getBookstore();
        CustomerSetting setting = settingRepo.findByBookstore(bookstore).orElse(null);
        double discountRate = 0;

        if (setting != null && customer.getLoyaltyPoints() >= setting.getRequiredPointsForMembership()) {
            discountRate = setting.getDiscountRate();
        }

        double discountAmount = total * discountRate / 100;
        invoice.setDiscountRate(discountRate);
        invoice.setDiscountAmount(discountAmount);

        double totalAfterVAT = (total - discountAmount) * (1 + vat / 100);
        int loyaltyPoints = (int) Math.round(totalAfterVAT / 1000.0);
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + loyaltyPoints);
        customerRepo.save(customer);

        double totalCost = 0;
        for (int i = 0; i < bookIds.size(); i++) {
            Book book = bookRepo.findById(bookIds.get(i)).orElse(null);
            // double avgImportPrice = book.getAverageImportPrice();
            double avgImportPrice = book.getAverageImportPrice() != null ? book.getAverageImportPrice() : 0.0;

            int qty = quantities.get(i);
            totalCost += avgImportPrice * qty;
        }
        double profit = totalAfterVAT - (totalCost * (1 + vat / 100));
        invoice.setProfit(profit);

        invoiceRepo.save(invoice);

        return invoice;
    }

    public byte[] exportInvoiceToPDF(Invoice invoice, List<InvoiceItem> items, double vatRate) {
        return InvoicePdfUtil.exportInvoicePDF(invoice, items, vatRate);
    }

    public List<Invoice> filterInvoices(Bookstore bookstore, String customerName, LocalDate from, LocalDate to) {
        return invoiceRepo.findAll().stream()
                .filter(inv -> inv.getBookstore().getId().equals(bookstore.getId()))
                .filter(inv -> customerName == null || customerName.isBlank()
                        || inv.getCustomer().getName().toLowerCase().contains(customerName.toLowerCase()))
                .filter(inv -> {
                    LocalDate date = inv.getCreatedAt().toLocalDate();
                    return (from == null || !date.isBefore(from)) && (to == null || !date.isAfter(to));
                })
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())) // mới nhất lên trước
                .collect(Collectors.toList());
    }

    public double calculateTotal(List<Invoice> invoices) {
        double total = 0;
        for (Invoice inv : invoices) {
            List<InvoiceItem> items = invoiceItemRepo.findByInvoice(inv);
            double subtotal = items.stream().mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();
            double discount = inv.getDiscountAmount();

            double vatRate = inv.getVatRate();
            double vat = (subtotal - discount) * vatRate / 100;

            total += (subtotal - discount + vat);
        }
        return total;
    }

    public double calculateTotalForInvoice(Invoice invoice) {
        List<InvoiceItem> items = invoiceItemRepo.findByInvoice(invoice);
        double subtotal = items.stream().mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();
        double discount = invoice.getDiscountAmount();
        double vatRate = invoice.getVatRate();
        double vat = (subtotal - discount) * vatRate / 100;

        return subtotal - discount + vat;
    }

}
