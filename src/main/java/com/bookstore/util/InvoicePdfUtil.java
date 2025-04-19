package com.bookstore.util;

import com.bookstore.entity.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

public class InvoicePdfUtil {

    public static byte[] exportInvoicePDF(Invoice invoice, List<InvoiceItem> items, double vatRate) {
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
}
