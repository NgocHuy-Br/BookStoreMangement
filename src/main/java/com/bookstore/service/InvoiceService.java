package com.bookstore.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFontFactory;

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

        return invoice;
    }

    public byte[] exportInvoiceToPDF(Invoice invoice, List<InvoiceItem> items, double vatRate) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            PdfFont font = PdfFontFactory.createFont("src/main/resources/static/fonts/ARIAL.ttf",
                    PdfEncodings.IDENTITY_H);
            doc.setFont(font);

            Bookstore bookstore = invoice.getBookstore();
            Customer customer = invoice.getCustomer();

            double discountRate = invoice.getDiscountRate();
            double discountAmount = invoice.getDiscountAmount();

            // Header
            doc.add(new Paragraph("Nhà sách: " + bookstore.getName()).setBold().setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("Địa chỉ: " + bookstore.getAddress()).setItalic()
                    .setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("HÓA ĐƠN BÁN HÀNG").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("Khách hàng: " + customer.getName()));
            doc.add(new Paragraph("SĐT: " + customer.getPhone()));
            doc.add(new Paragraph("\n"));

            // Bảng sách
            Table table = new Table(UnitValue.createPercentArray(new float[] { 7, 30, 15, 15, 15, 13 }))
                    .useAllAvailableWidth();

            table.addHeaderCell("STT").setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell("Tên sách");
            table.addHeaderCell("Tác giả");
            table.addHeaderCell("Thể loại");
            table.addHeaderCell("Giá bán");
            table.addHeaderCell("Số lượng");

            double total = 0;
            int index = 1;
            for (InvoiceItem item : items) {
                table.addCell(String.valueOf(index++)).setTextAlignment(TextAlignment.CENTER);
                table.addCell(item.getBook().getTitle());
                table.addCell(item.getBook().getAuthor());
                table.addCell(item.getBook().getCategory().getName());
                table.addCell(String.format("%,.0f", item.getUnitPrice()));
                table.addCell(String.valueOf(item.getQuantity()));
                total += item.getUnitPrice() * item.getQuantity();
            }

            doc.add(table);

            // double discountAmount = total * discountRate / 100;
            double vat = (total - discountAmount) * vatRate / 100;
            double grandTotal = (total - discountAmount) + vat;

            // Tổng kết
            doc.add(new Paragraph("Tổng cộng: " + String.format("%,.0f", total) + " VND"));
            doc.add(new Paragraph("Giảm giá thành viên: " + String.format("%,.0f", discountAmount) + " VND"));
            doc.add(new Paragraph("Thuế VAT: " + vatRate + "%"));
            doc.add(new Paragraph("Thành tiền: " + String.format("%,.0f", grandTotal) + " VND"));

            LocalDateTime created = invoice.getCreatedAt();
            doc.add(new Paragraph("\n\nNgày " + created.getDayOfMonth() +
                    " Tháng " + created.getMonthValue() +
                    " Năm " + created.getYear()));
            doc.add(new Paragraph("Người lập: " + invoice.getUser().getUsername()));

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
