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
            doc.add(new Paragraph("\nHÓA ĐƠN BÁN HÀNG").setBold().setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("Mã hóa đơn: " + invoice.getId()));
            doc.add(new Paragraph("Khách hàng: " + customer.getName()));
            doc.add(new Paragraph("Số điện thoại: " + customer.getPhone()));
            doc.add(new Paragraph("\nCHI TIẾT ĐƠN HÀNG").setBold().setTextAlignment(TextAlignment.CENTER));

            // Bảng sách
            Table table = new Table(UnitValue.createPercentArray(new float[] { 7, 25, 15, 23, 15, 10 }))
                    .useAllAvailableWidth();

            table.addHeaderCell(new Cell().add(new Paragraph("STT").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Tên sách").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Tác giả").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Danh mục").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setBold()));

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
            doc.add(new Paragraph("Tổng cộng: " + String.format("%,.0f", total) + " đ").setBold());
            doc.add(new Paragraph("Giảm giá thành viên: " + String.format("%,.0f", discountAmount) + " đ").setBold());
            doc.add(new Paragraph("Thuế VAT: " + vatRate + "%").setBold());
            doc.add(new Paragraph("Thành tiền: " + String.format("%,.0f", grandTotal) + " đ").setBold());

            LocalDateTime created = invoice.getCreatedAt();
            doc.add(new Paragraph("\nNgày " + created.getDayOfMonth() +
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
