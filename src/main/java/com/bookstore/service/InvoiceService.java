package com.bookstore.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Bookstore;
import com.bookstore.entity.Customer;
import com.bookstore.entity.CustomerSetting;
import com.bookstore.entity.Invoice;
import com.bookstore.entity.InvoiceItem;
import com.bookstore.entity.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.repository.CustomerSettingRepository;
import com.bookstore.repository.InvoiceItemRepository;
import com.bookstore.repository.InvoiceRepository;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.TextAlignment;

import com.itextpdf.layout.element.*;
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

    public Invoice createInvoice(User user, Customer customer,
            List<Long> bookIds,
            List<Integer> quantities,
            List<Double> prices) {

        Invoice invoice = new Invoice();
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setBookstore(user.getBookstore());
        invoice.setUser(user);
        invoice.setCustomer(customer);
        invoiceRepo.save(invoice);

        for (int i = 0; i < bookIds.size(); i++) {
            Book book = bookRepo.findById(bookIds.get(i)).orElse(null);
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setBook(book);
            item.setQuantity(quantities.get(i));
            item.setUnitPrice(prices.get(i));
            invoiceItemRepo.save(item);

            book.setQuantity(book.getQuantity() - quantities.get(i));
            bookRepo.save(book);
        }

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
            doc.add(new Paragraph("HÓA ĐƠN BÁN HÀNG").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("Nhà sách: " + bookstore.getName()).setBold());
            doc.add(new Paragraph("Địa chỉ: " + bookstore.getAddress()));

            doc.add(new Paragraph("\nMã hóa đơn: " + invoice.getId()));
            doc.add(new Paragraph("Ngày: " + invoice.getCreatedAt()));
            doc.add(new Paragraph("Khách hàng: " + invoice.getCustomer().getName()));

            float[] columnWidths = { 5f, 30f, 15f, 20f, 15f, 15f };
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

            table.addHeaderCell("STT");
            table.addHeaderCell("Tên sách");
            table.addHeaderCell("Tác giả");
            table.addHeaderCell("Danh mục");
            table.addHeaderCell("Giá bán");
            table.addHeaderCell("Số lượng");

            int index = 1;
            double total = 0;
            for (InvoiceItem item : items) {
                table.addCell(String.valueOf(index++));
                table.addCell(item.getBook().getTitle());
                table.addCell(item.getBook().getAuthor());
                table.addCell(item.getBook().getCategory().getName());
                table.addCell(String.format("%,.0f", item.getUnitPrice()));
                table.addCell(String.valueOf(item.getQuantity()));
                total += item.getQuantity() * item.getUnitPrice();
            }

            doc.add(table);
            double vatAmount = total * vatRate / 100;
            double grandTotal = total + vatAmount;
            doc.add(new Paragraph("Tổng tiền: " + String.format("%,.0f", total) + " VND").setBold());
            doc.add(new Paragraph("Thuế VAT: " + vatRate + "%").setBold());
            doc.add(new Paragraph("Thành tiền: " + String.format("%,.0f", grandTotal) + " VND").setBold());

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
