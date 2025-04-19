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

public class ImportOrderPdfUtil {

    public static byte[] exportImportOrderPDF(ImportOrder order, List<ImportOrderItem> items, double vatRate) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont("src/main/resources/static/fonts/ARIAL.ttf",
                    PdfEncodings.IDENTITY_H);
            doc.setFont(font);

            Bookstore store = order.getBookstore();
            doc.add(new Paragraph("Nhà sách: " + store.getName()).setBold().setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("Địa chỉ: " + store.getAddress()).setItalic().setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("\nPHIẾU NHẬP HÀNG").setBold().setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("Mã đơn: " + order.getId()));
            doc.add(new Paragraph("Nhà cung cấp: " + order.getSupplier().getName()));
            doc.add(new Paragraph("Ngày tạo: " + order.getCreatedAt().toLocalDate()));

            doc.add(new Paragraph("\nCHI TIẾT ĐƠN HÀNG").setBold());

            Table table = new Table(UnitValue.createPercentArray(new float[] { 5, 30, 15, 15, 15, 15 }))
                    .useAllAvailableWidth();
            table.addHeaderCell("STT").setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell("Tên sách");
            table.addHeaderCell("Tác giả");
            table.addHeaderCell("Danh mục");
            table.addHeaderCell("Đơn giá");
            table.addHeaderCell("Số lượng");

            double total = 0;
            int i = 1;
            for (ImportOrderItem item : items) {
                table.addCell(String.valueOf(i++)).setTextAlignment(TextAlignment.CENTER);
                table.addCell(item.getBook().getTitle());
                table.addCell(item.getBook().getAuthor());
                table.addCell(item.getBook().getCategory().getName());
                table.addCell(String.format("%,.0f", item.getUnitPrice()));
                table.addCell(String.valueOf(item.getQuantity()));
                total += item.getUnitPrice() * item.getQuantity();
            }

            doc.add(table);

            double vat = total * vatRate / 100;
            double grand = total + vat;
            doc.add(new Paragraph("\nTổng cộng: " + String.format("%,.0f", total) + " VND"));
            doc.add(new Paragraph("Thuế VAT: " + vatRate + "%"));
            doc.add(new Paragraph("Thành tiền: " + String.format("%,.0f", grand) + " VND"));

            LocalDateTime created = order.getCreatedAt();
            doc.add(new Paragraph("\nNgày " + created.getDayOfMonth() + " Tháng " + created.getMonthValue() + " Năm "
                    + created.getYear()));
            doc.add(new Paragraph("Người lập: " + order.getCreatedBy().getUsername()));

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
